import { NativeModules, Platform } from 'react-native';
import NativeUtil from '../native';

const ClientModule = NativeModules.ClientModule;
const isIos = Platform.OS === 'ios';

export const login = (username, password) => isIos ?
    NativeUtil(ClientModule.login, {username, password}) : NativeModules.RNEaseMobModule.login(username, password);

// TODO 补充iOS端接口后删除
export const logout = () => isIos ? Promise.resolve() : NativeModules.RNEaseMobModule.logout();