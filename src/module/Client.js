import { NativeModules, Platform } from 'react-native';
import NativeUtil from '../native';

const ClientModule = NativeModules.ClientModule;

export const login = (username, password) => Platform.OS === 'ios' ?
    NativeUtil(ClientModule.login, {username, password}) : NativeModules.RNEaseMobModule.login(username, password);