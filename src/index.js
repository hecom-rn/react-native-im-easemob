import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';
import guid from './guid';
import NativeUtil from './native';
import * as ChatManager from './module/ChatManager';
import * as Client from './module/Client';

export {
    ChatManager,
    Client,
};

const RNEaseMobModule = NativeModules.RNEaseMobModule;
const event = Platform.OS === 'ios' ? new NativeEventEmitter(RNEaseMobModule) : DeviceEventEmitter;
const handlers = {};

export function initWithAppKey(appKey, options = {}) {
    return NativeUtil(RNEaseMobModule.init, {appKey, ...options});
}

export function registerEventHandler(func) {
    const handlerId = guid();
    handlers[handlerId] = event.addListener('RNEaseMob', func);
    return handlerId;
}

export function unregisterEventHandler(handlerId) {
    if (handlers && handlers[handlerId]) {
        handlers[handlerId].remove();
        delete handlers[handlerId];
    }
}

export const EaseMobEventTypes = {
    chat_manager: {
        type: 'chat_manager',
        cmdMessage: 'cmd_message'
    },
    client: {
        type: 'client',
        login: 'login',
    },
};