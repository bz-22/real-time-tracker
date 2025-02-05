import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../LiveScores.css';  // Import the CSS file

const LiveScores = () => {
  const [scores, setScores] = useState(null);
  const [filteredScores, setFilteredScores] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [teams, setTeams] = useState([]);  // State to hold the teams data

  // Fetch teams (with logos) from your backend
  useEffect(() => {
    axios.get('http://192.168.1.10:8080/teams/47')
      .then(response => {
        console.log(response.data);  // Log the data received
        setTeams(response.data);  // Save the team data in state
      })
      .catch(error => {
        console.error('Error fetching teams:', error);
      });
  }, []);

  // Fetch live scores from your backend
  useEffect(() => {
    axios.get('http://192.168.1.10:8080/api/scores/fixtures/47')
      .then(response => {
        const matches = response.data.response.matches;
        setScores(matches);
        setFilteredScores(matches); // Initially show all matches
      })
      .catch(error => {
        console.error('Error fetching live scores:', error);
      });
  }, []);

  const handleDateChange = (event) => {
    const selectedDate = event.target.value;
    setSelectedDate(selectedDate);

    // Filter matches by the selected date
    if (selectedDate) {
      const filtered = scores.filter((match) =>
        new Date(match.status.utcTime).toLocaleDateString() === new Date(selectedDate).toLocaleDateString()
      );
      setFilteredScores(filtered);
    } else {
      setFilteredScores(scores); // If no date is selected, show all matches
    }
  };

  // Helper function to get the logo URL for a team by its ID
  const getTeamLogoUrl = (teamId) => {
    const team = teams.find((t) => t.team_id === Number(teamId));  // Compare by 'team_id'
    return team ? team.logoUrl : '';  // Use 'logoUrl' from the response data
  };
  

  return (
    <div className="container">
      <h2>Live Football Scores</h2>
      
      {/* Date Picker */}
      <input
        type="date"
        value={selectedDate}
        onChange={handleDateChange}
        className="datePicker"  // Apply datePicker class from CSS
      />
      
      {filteredScores === null ? (
        <p>Loading live scores...</p>
      ) : (
        <div>
          {filteredScores.length === 0 ? (
            <p>No matches found for the selected date.</p>
          ) : (
            filteredScores.map((match) => (
              <div key={match.id} className="matchContainer">
                <h3 className="matchTitle">
                <img
  src={getTeamLogoUrl(match.home.id)}  // Pass team ID to get the logo
  alt={match.home.name}
  className="teamLogo"
/>

                  {match.home.name}  -  {match.away.name}
                  <img
  src={getTeamLogoUrl(match.away.id)}  // Pass team ID to get the logo
  alt={match.away.name}
  className="teamLogo"
/>

                </h3>
                <p>Status: {match.status?.reason?.long || "Unknown"}</p>
                <p>Score: {match.home.score} - {match.away.score}</p>
                <p>
                  <strong>Match Time:</strong> {new Date(match.status.utcTime).toLocaleString()}
                </p>
                <hr />
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default LiveScores;
