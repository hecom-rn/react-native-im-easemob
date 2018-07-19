import { NativeModules, Platform } from 'react-native';

const ChatManagerDelegate = NativeModules.ChatManagerDelegate;
const isAndroid = Platform.OS === 'android';

export const setMessageDidReceive = proxy(ChatManagerDelegate.setMessageDidReceive);
export const setCmdMessageDidReceive = proxy(ChatManagerDelegate.setCmdMessageDidReceive);
export const setConversationListDidUpdate = proxy(ChatManagerDelegate.setConversationListDidUpdate);

function proxy(native_method) {
    return callback => native_method(
        (...result) => isAndroid ? callback(...result) : callback(...result.map(item => JSON.parse(item))));
}