import { useEffect, useState } from 'react';
import axios from 'axios';
import ErrorToast from '../../../BasicElements/ErrorToast';
import './ChatBox.css';

const ChatBox = ({ chatID }) => {
  const [data, setData] = useState({ messages: [] }); // Initialize with messages array
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [messageText, setMessageText] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      setError(null);
  
      try {
        const response = await axios.get(
          `http://localhost:8082/api/rs/chat/${chatID}`,
          {
            headers: {
              'Authorization': localStorage.getItem("jwtToken") || "XXX",
            },
          }
        );
        setData(response.data);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        setError(error);
        console.error('Error fetching conversations:', error);
      } finally {
        setIsLoading(false);
      }
    };
  
    // Initial fetch
    fetchData();
  
    // Set up interval to fetch data every 5 seconds
    const intervalId = setInterval(() => {
      fetchData();
    }, 5000);
  
    // Clean up interval on component unmount
    return () => clearInterval(intervalId);
  }, [chatID]);
  

  const sendMessage = async () => {
    try {
      const response = await axios.post(
        `http://localhost:8082/api/rs/chat/send/${chatID}`,
        {message: messageText},
        {
          headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX",
          },
        }
      );
      // Assuming the response contains updated data, you can update the state
      setData(response.data);
      setMessageText(''); // Clear message input after sending
    } catch (error) {
      if (error.response.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      setError(error);
      console.error('Error sending message:', error);
    }
  };
  if (chatID) {
  return (
    <div className="chat-box-container">
      <div className="header">
        <h1>{chatID}</h1>
      </div>
      <div className="chat-messages-div">
        <div className="chat-messages">
          {isLoading && <p>Loading...</p>}
          {data.messages && data.messages.map((message) => (
            (message.fromUserId.name === localStorage.getItem("currentUser")) ?
              <div className='right-content'>
                {message.message}
                <p className='small-info'>
                  <p>{message.fromUserId.name}</p>
                  <p>{new Date(message.timeStamp * 1000).toLocaleString()}</p>
                </p>
              </div>
              :
              <div className='left-content'>
                <p className='small-info'>
                  <p>{message.fromUserId.name}</p>
                  <p>{new Date(message.timeStamp * 1000).toLocaleString()}</p>
                </p>
                {message.message}
              </div>
          ))}
        </div>
      </div>
      <div className="send-message-div">
        <input
          type="text"
          className="textField"
          placeholder="Enter text"
          value={messageText}
          onChange={(e) => setMessageText(e.target.value)}
        />
        <button className="button" onClick={sendMessage}>Send</button>
      </div>
      {error && <ErrorToast message={error.message} />}
    </div>
  );
} else {
    return (
        <div>
            <p>
                Select A chat...
            </p>
        </div>
    )
}
};

export default ChatBox;
