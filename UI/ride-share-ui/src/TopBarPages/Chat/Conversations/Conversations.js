import Conversation from "./Conversation";
import { useEffect, useState } from "react";
import axios from "axios";

const Conversations = ({ setChatID, activeChatID }) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "https://chat-service-1002278726079.us-central1.run.app/api/rs/chat/conversations",
          {
            headers: {
              Authorization: localStorage.getItem("jwtToken") || "XXX",
            },
          }
        );
        setData(response.data);
      } catch (error) {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        console.error("Error fetching conversations:", error);
      }
    };

    fetchData();
    const intervalId = setInterval(fetchData, 5000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <div className="h-full flex flex-col">
      <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex-shrink-0">
        <h2 className="text-2xl font-bold text-gray-800 dark:text-white">
          Chats
        </h2>
      </div>

      <div className="flex-1 overflow-y-auto">
        <div className="p-2 flex flex-col gap-1">
          {data.map((chat) => (
            <Conversation
              key={chat.userID1.userName + chat.userID2.userName}
              chat={chat}
              currentUser={localStorage.getItem("currentUser")}
              setChatID={setChatID}
              activeChatID={activeChatID}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Conversations;
