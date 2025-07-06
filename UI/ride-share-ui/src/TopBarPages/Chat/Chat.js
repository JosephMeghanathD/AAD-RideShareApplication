import { useState } from "react";
import Conversations from "./Conversations/Conversations.js";
import ChatBox from "./ChatBox/ChatBox.js";

const Chat = () => {
  const [chatId, setChatID] = useState(
    new URLSearchParams(window.location.search).get("activechat")
  );

  return (
    <div className="flex h-[calc(100vh-4rem)] bg-gray-100 dark:bg-gray-900 font-sans">
      <div className="w-full md:w-1/3 lg:w-1/4 flex flex-col bg-white dark:bg-gray-800 shadow-lg">
        <Conversations setChatID={setChatID} activeChatID={chatId} />
      </div>

      <div className="hidden md:flex w-2/3 lg:w-3/4 flex-col border-l border-gray-200 dark:border-gray-700">
        <ChatBox chatID={chatId} key={chatId} />
      </div>
    </div>
  );
};

export default Chat;
