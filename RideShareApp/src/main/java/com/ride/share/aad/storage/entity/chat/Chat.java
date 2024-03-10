package com.ride.share.aad.storage.entity.chat;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;
import com.ride.share.aad.utils.entity.ChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.ride.share.aad.storage.service.CassandraStorageService.getCqlSession;
import static com.ride.share.aad.utils.entity.ChatUtils.getChatID;

public class Chat {
    public static final String CHAT_TABLE = "chats";
    public static final int CHAT_TTL = 2 * 86400;
    private static final Logger logger = LoggerFactory.getLogger(Chat.class);
    String userID1, userID2, chatId;
    List<ChatMessage> messages = new ArrayList<>();
    transient ChatDAO chatDAO;

    {
        ChatDAO chatDAO = new ChatDAO();
        chatDAO.createTable();
    }

    public Chat() {
        chatDAO = new ChatDAO();
    }

    public Chat(String chatId) {
        this();
        this.chatId = chatId;
        chatDAO.mapToEntity(chatId, this);
    }

    public Chat(String userID1, String userID2) {
        this(getChatID(userID1, userID2));
        this.userID1 = userID1;
        this.userID2 = userID2;
    }

    public Chat(String userID1, String userID2, List<ChatMessage> messages) {
        this(userID1, userID2);
        this.messages = messages;
    }

    public static List<Chat> getAllChatsOfUser(String userId) {
        PreparedStatement prepare = getCqlSession().prepare("SELECT * FROM " + CHAT_TABLE +
                " WHERE userid1 = '" + userId + "' ALLOW FILTERING;");
        List<Chat> allChats = ChatUtils.getAllChats(prepare);
        prepare = getCqlSession().prepare("SELECT * FROM " + CHAT_TABLE +
                " WHERE userid2 = '" + userId + "' ALLOW FILTERING;");
        allChats.addAll(ChatUtils.getAllChats(prepare));
        return allChats;
    }

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public Chat addMessage(ChatMessage message) {
        messages.add(message);
        return this;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Chat save() {
        chatDAO.insert(chatId, chatId, userID1, userID2, ChatUtils.toMessagesJsonStr(messages));
        return this;
    }

    public Chat delete() {
        chatDAO.delete(chatId);
        return this;
    }

    public Chat update() {
        chatDAO.update(chatId, messages);
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Chat.class.getSimpleName() + "[", "]").add("userID1='" + userID1 + "'").add("userID2='" + userID2 + "'").add("chatId='" + chatId + "'").toString();
    }

    public Chat mapToEntity() {
        chatDAO.mapToEntity(chatId, this);
        return this;
    }

    public class ChatDAO extends AbstractCassandraDAO<Chat> {
        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " + CHAT_TABLE + " (chatId TEXT PRIMARY KEY, userId1 TEXT, userId2 TEXT, messages TEXT)");

        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement UPDATE_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        public static List<Chat> getAllChats() {
            List<Chat> chatList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + CHAT_TABLE).bind();
            return mapChats(boundStatement, chatList);
        }

        public static List<Chat> getChatsByUserId(String userId) {
            List<Chat> chatList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + CHAT_TABLE + " WHERE userId1 = ? OR userId2 = ?").bind(userId, userId);
            return mapChats(boundStatement, chatList);
        }

        private static List<Chat> mapChats(BoundStatement boundStatement, List<Chat> chatList) {
            Row row = null;
            try {
                for (Row currentRow : getCqlSession().execute(boundStatement)) {
                    row = currentRow;
                    chatList.add(ChatUtils.mapToEntity(row, null));
                }
            } catch (Exception e) {
                logger.error("Failed to get chat for row: {}", row, e);
            }
            return chatList;
        }

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + CHAT_TABLE + " (chatId, userId1, userId2, messages) VALUES (?, ?, ?, ?) USING TTL " + CHAT_TTL);
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + CHAT_TABLE + " SET messages = ? WHERE chatId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + CHAT_TABLE + " WHERE chatId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + CHAT_TABLE + " WHERE chatId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Chat mapToEntity(String key, Chat chat) {
            Row row = get(key).one();

            if (row != null) {
                return ChatUtils.mapToEntity(row, chat);
            }
            return null;
        }
    }
}
