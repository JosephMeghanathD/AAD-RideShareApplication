import React from 'react';

const Conversation = ({ chat, currentUser, setChatID, activeChatID }) => {
  // Determine the other user in the conversation
  const otherUser = currentUser === chat.userID1.name ? chat.userID2 : chat.userID1;
  const isActive = otherUser.userName === activeChatID;

  // Get the latest message and format its timestamp
  const latestMessage = chat.messages && chat.messages.length > 0
    ? [...chat.messages]
      .sort((a, b) => b.timeStamp - a.timeStamp)
      .find(msg => msg.message && msg.message.trim() !== '')
    : null; const timestamp = latestMessage
      ? new Date(latestMessage.timeStamp * 1000).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      : '';

  return (
    <div
      onClick={() => setChatID(otherUser.userName)}
      className={`flex items-center p-3 rounded-xl cursor-pointer transition-colors duration-200 ease-in-out
                ${isActive
          ? 'bg-blue-600 text-white'
          : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700'
        }`
      }
    >
      {/* Avatar Placeholder */}
      <div className="relative flex-shrink-0 mr-4">
        <div className="w-12 h-12 rounded-full bg-gray-300 dark:bg-gray-600 flex items-center justify-center">
          <span className={`font-bold text-xl ${isActive ? 'text-blue-600' : 'text-gray-500'}`}>
            {otherUser.name.charAt(0).toUpperCase()}
          </span>
        </div>
        {/* Future: Add online status indicator
                <span className="absolute bottom-0 right-0 block h-3 w-3 rounded-full bg-green-400 border-2 border-white dark:border-gray-800"></span> 
                */}
      </div>

      <div className="flex-grow overflow-hidden">
        <div className="flex justify-between items-center">
          <h3 className="font-bold text-md truncate">{otherUser.name}</h3>
          <p className={`text-xs flex-shrink-0 ml-2 ${isActive ? 'text-blue-200' : 'text-gray-500 dark:text-gray-400'}`}>
            {timestamp}
          </p>
        </div>
        <p className={`text-sm truncate ${isActive ? 'text-blue-100' : 'text-gray-600 dark:text-gray-400'}`}>
          {latestMessage ? latestMessage.message : 'No messages yet'}
        </p>
      </div>
    </div>
  );
}

export default Conversation;