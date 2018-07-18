import * as ChatManager from '../native/ChatManager';
import { ChatType, MessageType } from '../constant/IMConstant';

export const createSingleConversation = (imId) => ChatManager.getConversation(imId, ChatType.single, true);
export const createGroupConversation = (groupId) => ChatManager.getConversation(groupId, ChatType.group, true);
export const getAllConversations = () => ChatManager.getAllConversations();
export const sendText = (conversationId, chatType, text) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.text, conversationId, {text}, {});
export const sendImage = (conversationId, chatType, path) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.image, conversationId, {path}, {});
export const sendLocation = (conversationId, chatType, latitude, longitude, address) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.location, conversationId, {latitude, longitude, address}, {});
export const sendVoice = (conversationId, chatType, path, duration) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.voice, conversationId, {path, duration}, {});
export const sendVideo = (conversationId, chatType, path) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.video, conversationId, {path}, {});
export const sendFile = (conversationId, chatType, path) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.file, conversationId, {path}, {});
export const sendObject = (conversationId, chatType, object) =>
    ChatManager.sendMessage(conversationId, chatType, MessageType.text, conversationId, {text: ""}, object);
