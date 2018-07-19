import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const Client = NativeModules.Client;
const isIos = Platform.OS === 'ios';

export const initWithAppKey = (appKey, options = {}) => NativeUtil(Client.init, {appKey, ...options});

// Android Only
export function notifyJSDidLoad() {
    return !isIOS && Client.notifyJSDidLoad();
}

export const login = (username, password) => isIos ?
    NativeUtil(Client.login, {username, password}) : Client.login(username, password);

export const logout = () =>
    NativeUtil(Client.logout, undefined);
