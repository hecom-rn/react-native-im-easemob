import { NativeModules } from 'react-native';
import NativeUtil from './native';
import { ChatType, MessageDirection, MessageType } from '../constant/IMConstant';

const ChatManager = NativeModules.ChatManager;

/**
 * 获取单个会话。
 * @param conversationId 会话ID
 * @param type 会话类型
 * @param ifCreate 是否创建
 */
export const getConversation = (conversationId, type, ifCreate) =>
    NativeUtil(ChatManager.getConversation, {conversationId, type, ifCreate});

/**
 * 创建单聊会话。
 * @param imId 单聊用户的ImID
 */
export const createSingleConversation = (imId) => getConversation(imId, ChatType.single, true);

/**
 * 创建群聊会话。
 * @param groupId 群的ID
 */
export const createGroupConversation = (groupId) => getConversation(groupId, ChatType.group, true);

/**
 * 获取全部会话列表。
 */
export const getAllConversations = () => NativeUtil(ChatManager.getAllConversations, undefined);

/**
 * 删除指定会话。
 * @param {string} conversationId 会话ID
 * @param {boolean} ifClearAllMessage 是否清空消息
 */
export const deleteConversation = (conversationId, ifClearAllMessage = true) =>
    NativeUtil(ChatManager.deleteConversation, {
        conversationId,
        ifClearAllMessage
    });

/**
 * 获取会话的消息列表。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param fromId 指定开始的消息ID
 * @param count 加载数量
 * @param searchDirection 向上/向下加载
 */
export const loadMessages = (conversationId, chatType, fromId, count, searchDirection) =>
    NativeUtil(ChatManager.loadMessages, {conversationId, chatType, fromId, count, searchDirection});

/**
 * 删除消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param messageId 删除的消息ID
 */
export const deleteMessage = (conversationId, chatType, messageId) =>
    NativeUtil(ChatManager.deleteMessage, {conversationId, chatType, messageId});

/**
 * 撤回消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param messageId 撤销的消息ID
 */
export const recallMessage = (conversationId, chatType, messageId) =>
    NativeUtil(ChatManager.recallMessage, {conversationId, chatType, messageId});

/**
 * 发送通用消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param messageType 消息类型
 * @param body 消息体
 * @param messageExt 附件内容
 */
export const sendMessage = (conversationId, chatType, messageType, body, messageExt) =>
    NativeUtil(
        ChatManager.sendMessage,
        {conversationId, chatType, messageType, to: conversationId, body, messageExt}
    );

/**
 * 更新消息Ext字段
 * @param message 消息对象
 */
export const updateMessageExt = (message) =>
    NativeUtil(
        ChatManager.updateMessage,
        message
    );

/**
 * 插入历史消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param fromUserId 发送者Id
 * @param messageType 消息类型
 * @param body 消息体
 * @param timestamp 时间戳
 * @param localTime 本地时间戳
 * @param messageExt 附件内容
 */
export const insertHistoryMessage = (conversationId, chatType, fromUserId, messageType, body, timestamp, localTime, direction, messageExt) =>
    NativeUtil(
        ChatManager.insertMessage,
        {
            conversationId,
            chatType,
            messageType,
            from: fromUserId,
            to: conversationId,
            timestamp,
            localTime,
            direction,
            body,
            messageExt,
        }
    );

/**
 * 插入系统消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param text 消息提示文本
 * @param timestamp 时间戳
 * @param localTime 本地时间戳
 * @param messageExt 附件内容
 */
export const insertSystemMessage = (conversationId, chatType, text, timestamp, localTime, messageExt) =>
    NativeUtil(
        ChatManager.insertMessage,
        {
            conversationId,
            chatType,
            messageType: MessageType.text,
            to: conversationId,
            timestamp,
            localTime,
            direction: MessageDirection.send,
            body: {text},
            messageExt: {
                ...messageExt,
                isSystemMessage: true,
            },
        }
    );

/**
 * 发送文本消息
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param text 文本消息
 * @param ext 附加内容
 */
export const sendText = (conversationId, chatType, text, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.text, {text}, ext);

/**
 * 发送图片消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param path 图片文件路径
 * @param ext 附加内容
 */
export const sendImage = (conversationId, chatType, path, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.image, {path}, ext);

/**
 * 发送位置消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param latitude 纬度
 * @param longitude 经度
 * @param address 位置的地址
 * @param name 位置的名称
 * @param ext 附加内容
 */
export const sendLocation = (conversationId, chatType, latitude, longitude, address, name, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.location, {latitude, longitude, address}, {name, ...ext});

/**
 * 发送语音消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param path 语音文件路径
 * @param duration 语音长度(秒)
 * @param ext 附加内容
 */
export const sendVoice = (conversationId, chatType, path, duration, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.voice, {path, duration}, ext);

/**
 * 发送视频消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param path 视频文件路径
 * @param thumbPath 视频缩略图文件路径
 * @param duration 视频长度(秒)
 * @param ext 附加内容
 */
export const sendVideo = (conversationId, chatType, path, thumbPath = '', duration = 0, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.video, {path, thumbPath, duration}, ext);

/**
 * 发送文件消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param path 文件路径
 * @param ext 附加内容
 */
export const sendFile = (conversationId, chatType, path, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.file, {path}, ext);

/**
 * 发送透传消息。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 * @param action 透传类型
 * @param ext 附加内容
 */
export const sendCmd = (conversationId, chatType, action, ext = {}) =>
    sendMessage(conversationId, chatType, MessageType.cmd, {action}, ext);

/**
 * 设置会话消息全部已读。
 * @param conversationId 会话ID
 * @param chatType 聊天类型
 */
export const markAllMessagesAsRead = (conversationId, chatType) =>
    NativeUtil(ChatManager.markAllMessagesAsRead, {conversationId, chatType});
