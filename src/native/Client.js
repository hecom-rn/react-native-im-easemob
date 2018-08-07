import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const Client = NativeModules.Client;
const isIos = Platform.OS === 'ios';

/**
 * 初始化环信SDK。
 * @param appKey 环信APP Key
 * @param options 其他选项
 */
export const initWithAppKey = (appKey, options = {}) => NativeUtil(Client.init, {appKey, ...options});

/**
 * // TODO
 */
export const notifyJSDidLoad = () => !isIos && Client.notifyJSDidLoad();

/**
 * 聊天用户登陆。
 * @param username 用户名称
 * @param password 密码
 */
export const login = (username, password) => isIos ? NativeUtil(Client.login, {username, password}) : Client.login(username, password); // TODO 去掉isIOS，包括上面isIOS定义和Platform导入

/**
 * 聊天用户登出。
 */
export const logout = () => NativeUtil(Client.logout, undefined);
