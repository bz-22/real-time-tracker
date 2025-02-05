require('dotenv').config(); // Load environment variables

const express = require('express');
const axios = require('axios');
const mysql = require('mysql2/promise'); // Use promise-based MySQL
const app = express();
const port = 8080;

// MySQL connection pool setup
const pool = mysql.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Fetch and store leagues and teams dynamically
app.get('/fetch-and-store', async (req, res) => {
  const leagueIds = [42, 47]; // Add more league IDs if needed
  const connection = await pool.getConnection(); // Get a connection from the pool

  try {
    for (const leagueId of leagueIds) {
      console.log(`Fetching data for League ID: ${leagueId}`);

      // Fetch the list of teams for the league
      const response = await axios.get(
        `https://free-api-live-football-data.p.rapidapi.com/football-get-list-all-team?leagueid=${leagueId}`,
        {
          headers: {
            'X-RapidAPI-Host': 'free-api-live-football-data.p.rapidapi.com',
            'X-RapidAPI-Key': process.env.RAPIDAPI_KEY
          }
        }
      );

      const teams = response.data.response.list;

      // Insert league if it doesn't exist
      await connection.execute(
        'INSERT IGNORE INTO leagues (id, name) VALUES (?, ?)',
        [leagueId, `League ${leagueId}`]
      );
      console.log(`League ${leagueId} inserted or already exists`);

      // Insert teams and their league relationships
      for (const team of teams) {
        const { id: teamId, name: teamName, logo: logoUrl } = team;

        // Insert team if it doesn't exist
        await connection.execute(
          'INSERT IGNORE INTO teams (id, name, logoUrl) VALUES (?, ?, ?)',
          [teamId, teamName, logoUrl]
        );
        console.log(`Team ${teamName} inserted or already exists`);

        // Insert into team_leagues if the relationship doesn't exist
        await connection.execute(
          'INSERT IGNORE INTO team_leagues (team_id, league_id) VALUES (?, ?)',
          [teamId, leagueId]
        );
        console.log(`Team ${teamName} linked to League ${leagueId}`);
      }
    }

    res.send('All leagues, teams, and relationships stored successfully');
  } catch (error) {
    console.error('Error fetching and storing data:', error);
    res.status(500).send('Error fetching and storing data');
  } finally {
    connection.release(); // Release the connection back to the pool
  }
});

// Start the server
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
