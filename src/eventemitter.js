import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';

const event = Platform.OS === 'ios' ? new NativeEventEmitter(NativeModules.Client) : DeviceEventEmitter;
const handlers = {};
let handlerObj = null;

/**
 * 初始化原生事件监听。
 */
export function init() {
    if (handlerObj) {
        handlerObj.remove();
    }
    handlerObj = event.addListener('RNEaseMob', (body) => {
        const {type, subType, data} = body;
        if (handlers[type] && handlers[type][subType]) {
            handlers[type][subType](data);
        }
    });
}

/**
 * 设置消息接收事件回调。
 */
export const setMessageDidReceive = setCallback('ChatManagerDelegate', 'messageDidReceive');

/**
 * 设置CMD控制消息接收事件回调。
 */
export const setCmdMessageDidReceive = setCallback('ChatManagerDelegate', 'cmdMessageDidReceive');

/**
 * 设置会话列表更新事件回调。
 */
export const setConversationListDidUpdate = setCallback('ChatManagerDelegate', 'conversationListDidUpdate');

function setCallback(type, subType) {
    return function (callback) {
        if (!handlers[type]) {
            handlers[type] = {};
        }
        handlers[type][subType] = callback;
    };
}