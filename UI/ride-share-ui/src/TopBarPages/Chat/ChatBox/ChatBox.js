import { useEffect, useState } from 'react';
import axios from 'axios';

const ChatBox = ({ chatID }) => {
  const [data, setData] = useState({ messages: [] }); // Initialize with messages array
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `http://localhost:8080/api/rs/chat/${chatID}`,
          {
            headers: {
              'Authorization': localStorage.getItem("jwtToken") || "XXX",
            },
          }
        );
        setData(response.data);
      } catch (error) {
        setError(error);
        console.error('Error fetching conversations:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [chatID]); // Run effect only when chatID changes

  return (
    <div className="chat-box-div">
      <p>{chatID}</p>
      {isLoading && <p>Loading...</p>}
      {error && <p>Error: {error.message}</p>}
      {data.messages && data.messages.map((message) => (
        <div>{message.message}</div> // Assuming a unique 'id' property
      ))}
    </div>
  );
};

export default ChatBox;
