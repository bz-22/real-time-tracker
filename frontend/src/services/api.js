
export const fetchScores = () => {
    return fetch('http://localhost:8080/api/scores/fixtures')
      .then((response) => response.json())
      .catch((error) => console.error('Error fetching scores:', error));
  };
  