import { NativeModules, Platform } from 'react-native';
import NativeUtil from '../native';

const ClientModule = NativeModules.ClientModule;
const isIos = Platform.OS === 'ios';

export const login = (username, password) => isIos ?
    NativeUtil(ClientModule.login, {username, password}) : NativeModules.RNEaseMobModule.login(username, password);

export const logout = () => NativeModules.RNEaseMobModule.logout();