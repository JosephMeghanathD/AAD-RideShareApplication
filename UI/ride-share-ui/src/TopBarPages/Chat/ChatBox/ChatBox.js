import { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import ErrorToast from '../../../BasicElements/ErrorToast';

const ChatBox = ({ chatID }) => {
  const [data, setData] = useState({ messages: [] });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [messageText, setMessageText] = useState('');
  const [name, setName] = useState('');
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [data.messages]);

  useEffect(() => {
    if (!chatID) return;

    const fetchData = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await axios.get(
          `https://chat-service-1002278726079.us-central1.run.app/api/rs/chat/${chatID}`,
          { headers: { 'Authorization': localStorage.getItem("jwtToken") || "XXX" } }
        );
        if (response.data) {
          const otherUser = response.data.userID1.name === localStorage.getItem("currentUser") ? response.data.userID2.name : response.data.userID1.name;
          setName(otherUser);
          response.data.messages.sort((a, b) => a.timeStamp - b.timeStamp);
          setData(response.data);
        }
      } catch (err) {
        if (err.response && err.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        setError(err);
        console.error('Error fetching chat:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
    const intervalId = setInterval(fetchData, 5000);
    return () => clearInterval(intervalId);
  }, [chatID]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!messageText.trim()) return;

    try {
      const response = await axios.post(
        `https://chat-service-1002278726079.us-central1.run.app/api/rs/chat/send/${chatID}`,
        { message: messageText },
        { headers: { 'Authorization': localStorage.getItem("jwtToken") || "XXX" } }
      );
      response.data.messages.sort((a, b) => a.timeStamp - b.timeStamp);
      setData(response.data);
      setMessageText('');
    } catch (err) {
      if (err.response && err.response.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      setError(err);
      console.error('Error sending message:', err);
    }
  };

  if (!chatID) {
    return (
      <div className="flex flex-col items-center justify-center h-full text-gray-500 dark:text-gray-400 bg-white dark:bg-gray-800">
        <svg className="w-24 h-24 text-gray-300 dark:text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>
        <h2 className="mt-6 text-2xl font-semibold text-gray-700 dark:text-gray-200">Select a Chat</h2>
        <p className="mt-2 text-center max-w-sm">Choose from your conversations on the left to start messaging.</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col h-full bg-white dark:bg-gray-800">
      {/* Header */}
      <div className="flex items-center p-4 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
        <h1 className="text-xl font-bold text-gray-800 dark:text-white">{name}</h1>
      </div>

      {/* Messages Area */}
      <div className="flex-1 overflow-y-auto p-4">
        <div className="flex flex-col gap-4">
          {isLoading && !data.messages.length && <p className="text-center text-gray-500">Loading chat...</p>}
          {data.messages && data.messages.map((message) => {
            const isCurrentUser = message.fromUserId.name === localStorage.getItem("currentUser");
            return (
              <div key={message.timeStamp} className={`flex items-end gap-2 ${isCurrentUser ? 'justify-end' : 'justify-start'}`}>
                <div className={`max-w-md lg:max-w-xl px-4 py-2.5 rounded-2xl ${isCurrentUser ? 'bg-blue-600 text-white rounded-br-lg' : 'bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-200 rounded-bl-lg'}`}>
                  <p className="text-sm break-words">{message.message}</p>
                  <div className={`text-xs mt-1 text-right ${isCurrentUser ? 'text-blue-200' : 'text-gray-500 dark:text-gray-400'}`}>
                    {new Date(message.timeStamp * 1000).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  </div>
                </div>
              </div>
            );
          })}
          <div ref={messagesEndRef} />
        </div>
      </div>

      {/* --- FINAL Input Area (No Frame) --- */}
      <form
        onSubmit={sendMessage}
        className="flex-shrink-0 p-3 flex items-center gap-3 bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700"
      >
        <input
          type="text"
          className="flex-1 h-12 bg-gray-100 dark:bg-gray-700 border-transparent rounded-xl px-5 focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 transition-all duration-200"
          placeholder="Type a message..."
          value={messageText}
          onChange={(e) => setMessageText(e.target.value)}
        />
        <button type="submit" className="bg-blue-600 text-white rounded-full p-3 w-12 h-12 flex items-center justify-center hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-800 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" className="w-6 h-6 transform rotate-90">
            <path d="M10.894 2.553a1 1 0 00-1.788 0l-7 14a1 1 0 001.169 1.409l5-1.429A1 1 0 009 15.571V11a1 1 0 112 0v4.571a1 1 0 00.725.962l5 1.428a1 1 0 001.17-1.408l-7-14z" />
          </svg>
        </button>
      </form>
      {error && <ErrorToast message={error.message} />}
    </div>
  );
};

export default ChatBox;