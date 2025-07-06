const BASE_URL =
  "https://chat-service-1002278726079.us-central1.run.app/api/rs/chat";

export const API_ENDPOINTS = {
  conversations: `${BASE_URL}/conversations`,
  chatById: (chatId) => `${BASE_URL}/${chatId}`,
  sendMessage: (chatId) => `${BASE_URL}/send/${chatId}`,
};

export const getAuthHeader = () => ({
  headers: {
    Authorization: localStorage.getItem("jwtToken") || "XXX",
  },
});
