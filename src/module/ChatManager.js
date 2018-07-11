import { NativeModules } from 'react-native';
import NativeUtil from '../native';

const ChatManager = NativeModules.ChatManager;

export const getConversation = (conversationId, type, ifCreate) =>
    NativeUtil(ChatManager.getConversation, {conversationId, type, ifCreate});

export const getAllConversations = () => ChatManager.getAllConversations();