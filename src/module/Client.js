import { NativeModules } from 'react-native';
import NativeUtil from '../native';

const ClientModule = NativeModules.ClientModule;

export const login = (username, password) => NativeUtil(ClientModule.login, {username, password});