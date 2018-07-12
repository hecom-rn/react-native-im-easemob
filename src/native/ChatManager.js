import { NativeModules } from 'react-native';
import NativeUtil from './native';

const ChatManager = NativeModules.ChatManager;

export const getConversation = (conversationId, type, ifCreate) =>
    NativeUtil(ChatManager.getConversation, {conversationId, type, ifCreate});

export const getAllConversations = () =>
    NativeUtil(ChatManager.getAllConversations, {});

export const sendMessage = (conversationId, chatType, messageType, to, body, messageExt) =>
    NativeUtil(ChatManager.sendMessage, {conversationId, chatType, messageType, to, body, messageExt});