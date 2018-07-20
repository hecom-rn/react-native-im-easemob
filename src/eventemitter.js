import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';

const event = Platform.OS === 'ios' ? new NativeEventEmitter(NativeModules.Client) : DeviceEventEmitter;
const handlers = {};
let handlerObj = null;

export const setMessageDidReceive = setCallback('ChatManagerDelegate', 'messageDidReceive');
export const setCmdMessageDidReceive = setCallback('ChatManagerDelegate', 'cmdMessageDidReceive');
export const setConversationListDidUpdate = setCallback('ChatManagerDelegate', 'conversationListDidUpdate');

export function init(func) {
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

function setCallback(type, subType) {
    return function (callback) {
        if (!handlers[type]) {
            handlers[type] = {};
        }
        handlers[type][subType] = callback;
    };
}