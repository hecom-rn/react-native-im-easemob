import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const Client = NativeModules.Client;
const isIos = Platform.OS === 'ios';

/**
 * 初始化环信SDK。
 * @param appKey 环信APP Key
 * @param options 其他选项
 *   Android：
 *     isAutoLogin：是否自动登陆
 *     miAppKey：小米推送appKey
 *     miAppSecret：小米推送appSecret
 */
export const initWithAppKey = (appKey, options = {}) => NativeUtil(Client.init, {appKey, ...options});

/**
 * 通知Native，JS已经加载完毕
 * Android Only
 */
export const notifyJSDidLoad = () => !isIos && Client.notifyJSDidLoad();

/**
 * 聊天用户登陆。
 * @param {string} username 用户名称
 * @param {string} password 密码
 * @param {boolean} autoLogin 是否自动登陆，iOS only，Android在init方法中传入options
 */
export const login = (username, password, autoLogin = false) => NativeUtil(Client.login, {username, password, autoLogin});

/**
 * 聊天用户登出。
 */
export const logout = () => NativeUtil(Client.logout, undefined);
