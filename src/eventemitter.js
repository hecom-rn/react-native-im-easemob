import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';
import guid from './guid';

const isIOS = Platform.OS === 'ios';
const RNEaseMobModule = NativeModules.Client;
const event = isIOS ? new NativeEventEmitter(RNEaseMobModule) : DeviceEventEmitter;
const handlers = {};

// Android Only
export function notifyJSDidLoad() {
    return !isIOS && RNEaseMobModule.notifyJSDidLoad();
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