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
 * 注册新用户。
 * @param {string} username 用户名称
 * @param {string} password 密码
 */
export const register = (username, password) => NativeUtil(Client.registerUser, {username, password});

/**
 * 聊天用户登陆。
 * @param {string} username 用户名称
 * @param {string} password 密码
 * @param {boolean} autoLogin 是否自动登陆，iOS only，Android在init方法中传入options
 */
export const login = (username, password, autoLogin = false) => NativeUtil(Client.login, {
    username,
    password,
    autoLogin
});

/**
 * 聊天用户登出。
 */
export const logout = () => NativeUtil(Client.logout, undefined);

/**
 * 检查是否连接到聊天服务器
 */
export const isConnected = () => {
    const {result = false} = NativeUtil(Client.isConnected) || {};
    return result;
};

/**
 * 返回是否登录过 登录成功过没调logout方法，这个方法的返回值一直是true 如果需要判断当前是否连接到服务器，请使用{@link Client.isConnected()}方法
 */
export const isLoggedIn = () => {
    const {result = false} = NativeUtil(Client.isLoggedIn) || {};
    return result;
};