import { Listener } from 'react-native-hecom-common';
import ListenerType from '../constant/ListenerType';
import * as ChatManager from '../native/ChatManager';
import { ChatType, MessageType } from '../constant/IMConstant';

function createConversation(conversationId, type) {
    return ChatManager.getConversation(conversationId, type, true)
        .then(conversation => {
            Listener.trigger([
                ListenerType.chat.type,
                ListenerType.chat.subTypes.create,
            ], {
                conversationId: conversation.conversationId,
            });
            return conversation;
        });
}

function sendMessage(conversationId, chatType, messageType, to, body, messageExt) {
    ChatManager.sendMessage(conversationId, chatType, messageType, to, body, messageExt)
        .then(message => {
            Listener.trigger([
                ListenerType.chat.type,
                ListenerType.chat.subTypes.send_message,
            ], {
                messageId: message.messageId,
            });
            return message;
        });
}

export const createSingleConversation = (imId) => createConversation(imId, ChatType.single);
export const createGroupConversation = (groupId) => createConversation(groupId, ChatType.group);
export const getAllConversations = () => ChatManager.getAllConversations();
export const sendText = (conversationId, chatType, to, text) =>
    sendMessage(conversationId, chatType, MessageType.text, to, {text}, {});