import { NativeModules } from 'react-native';
import NativeUtil from './native';
import { ChatType, MessageType } from '../constant/IMConstant';

const ChatManager = NativeModules.ChatManager;

const getConversation = (conversationId, type, ifCreate) =>
    NativeUtil(ChatManager.getConversation, {conversationId, type, ifCreate});
const sendMessage = (conversationId, chatType, messageType, to, body, messageExt) =>
    NativeUtil(ChatManager.sendMessage, {conversationId, chatType, messageType, to, body, messageExt});

export const createSingleConversation = (imId) => getConversation(imId, ChatType.single, true);
export const createGroupConversation = (groupId) => getConversation(groupId, ChatType.group, true);
export const getAllConversations = () =>
    NativeUtil(ChatManager.getAllConversations, undefined);
export const deleteConversation = (conversationId) =>
    NativeUtil(ChatManager.deleteConversation, {conversationId});
export const sendText = (conversationId, chatType, text) =>
    sendMessage(conversationId, chatType, MessageType.text, conversationId, {text}, {});
export const sendImage = (conversationId, chatType, path) =>
    sendMessage(conversationId, chatType, MessageType.image, conversationId, {path}, {});
export const sendLocation = (conversationId, chatType, latitude, longitude, address) =>
    sendMessage(conversationId, chatType, MessageType.location, conversationId, {latitude, longitude, address}, {});
export const sendVoice = (conversationId, chatType, path, duration) =>
    sendMessage(conversationId, chatType, MessageType.voice, conversationId, {path, duration}, {});
export const sendVideo = (conversationId, chatType, path) =>
    sendMessage(conversationId, chatType, MessageType.video, conversationId, {path}, {});
export const sendFile = (conversationId, chatType, path) =>
    sendMessage(conversationId, chatType, MessageType.file, conversationId, {path}, {});
export const sendObject = (conversationId, chatType, object) =>
    sendMessage(conversationId, chatType, MessageType.text, conversationId, {text: ""}, object);
export const loadMessages = (conversationId, chatType, fromId, count, searchDirection) =>
    NativeUtil(ChatManager.loadMessages, {conversationId, chatType, fromId, count, searchDirection});