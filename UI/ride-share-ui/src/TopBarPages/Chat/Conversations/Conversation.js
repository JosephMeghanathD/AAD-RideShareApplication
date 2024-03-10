import '../Chat.css'
import './Conversations.css';

const Conversation = ({chat, currentUser, setChatID}) => {
    return (
        <div className="conversation" onClick={() => setChatID(currentUser === chat.userID1 ? chat.userID2 : chat.userID1)}>
          <div className="conversation__header">
            <h3 className="conversation__name">
              {currentUser === chat.userID1 ? chat.userID2 : chat.userID1}
            </h3>
            <p className="conversation__timestamp">
              {new Date(chat.messages[0].timeStamp * 1000).toLocaleString()}
            </p>
          </div>
          <div className="conversation__body">
            <p className="conversation__message">{chat.messages[0].message}</p>
          </div>
        </div>
      );
}
export default Conversation;