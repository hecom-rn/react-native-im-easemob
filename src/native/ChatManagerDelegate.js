import { NativeModules } from 'react-native';

const ChatManagerDelegate = NativeModules.ChatManagerDelegate;

export const setMessageDidReceive = ChatManagerDelegate.setMessageDidReceive;
export const setCmdMessageDidReceive = ChatManagerDelegate.setCmdMessageDidReceive;
export const setConversationListDidUpdate = ChatManagerDelegate.setConversationListDidUpdate;