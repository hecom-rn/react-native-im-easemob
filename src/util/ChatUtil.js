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
    return ChatManager.sendMessage(conversationId, chatType, messageType, to, body, messageExt)
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
