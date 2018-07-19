import { NativeModules, Platform } from 'react-native';

const isIOS = Platform.OS === 'ios';
const RNEaseMobModule = NativeModules.Client;

// Android Only
export function notifyJSDidLoad() {
    return !isIOS && RNEaseMobModule.notifyJSDidLoad();
}