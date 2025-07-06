import { useState } from 'react';
import Conversations from './Conversations/Conversations.js';
import ChatBox from './ChatBox/ChatBox.js';

const Chat = () => {
    // Get initial active chat from URL, if present
    const [chatId, setChatID] = useState(new URLSearchParams(window.location.search).get('activechat'));

    return (
        // The height is calculated to fill the viewport *below* a 4rem (64px) navbar.
        // Adjust "4rem" if your navbar has a different height.
        <div className="flex h-[calc(100vh-4rem)] bg-gray-100 dark:bg-gray-900 font-sans">
            {/* Conversations Panel */}
            <div className="w-full md:w-1/3 lg:w-1/4 flex flex-col bg-white dark:bg-gray-800 shadow-lg">
                <Conversations setChatID={setChatID} activeChatID={chatId} />
            </div>

            {/* ChatBox Panel */}
            <div className="hidden md:flex w-2/3 lg:w-3/4 flex-col border-l border-gray-200 dark:border-gray-700">
                {/* Adding a key ensures the ChatBox component remounts and fetches new data when the chatID changes */}
                <ChatBox chatID={chatId} key={chatId} />
            </div>
        </div>
    );
}

export default Chat;