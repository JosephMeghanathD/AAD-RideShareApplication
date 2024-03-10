import './Conversations.css';
import Conversation from './Conversation';
import { useEffect, useState } from 'react';
import axios from 'axios';

const Conversations = ({setChatID}) => {
    const [data, setData] = useState([]);
    useEffect(() => {
        const fetchData = async () => {
          
          try {
            const response = await axios.get('http://localhost:8080/api/rs/chat/conversations', {
              headers: {
                'Authorization': localStorage.getItem("jwtToken") || "XXX"
              }
            });
            setData(response.data);
          } catch (error) {
            console.error('Error fetching conversations:', error);
          }
        };
    
        fetchData();
      }, []);
    return (
		<div className="conversations-container">
			{data.map((chatId) => (
                <Conversation chat={chatId} currentUser={localStorage.getItem("currentUser")} setChatID={setChatID}/>
            ))}
		</div>
	);
}

export default Conversations;

