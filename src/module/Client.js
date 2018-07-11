import { NativeModules, Platform } from 'react-native';
import NativeUtil from '../native';

const Client = NativeModules.Client;
const isIos = Platform.OS === 'ios';

export const login = (username, password) => isIos ?
    NativeUtil(Client.login, {username, password}) : Client.login(username, password);

export const logout = () => Client.logout();
