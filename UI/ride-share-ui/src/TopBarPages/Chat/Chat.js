import { useState } from 'react';
import './Chat.css';
import Conversations from './Conversations/Conversations.js';
import ChatBox from './ChatBox/ChatBox.js';


const Chat = () => {
    const [chatId, setChatID] = useState("")
    return (
		<div className="chat-box-div">
			<div className="border conversations-div">
                <Conversations setChatID={setChatID}/>
            </div>
            <div className="divider-div">
            </div>
            <div className="border chat-div">
                <ChatBox chatID={chatId}/>
            </div>
		</div>
	);
}

export default Chat;

