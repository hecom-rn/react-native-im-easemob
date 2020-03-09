import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';

const event = Platform.OS === 'ios' ? new NativeEventEmitter(NativeModules.Client) : DeviceEventEmitter;
const handlers = {};
let handlerObj = null;

/**
 * 初始化原生事件监听。
 */
export function init() {
    if (handlerObj) {
        handlerObj.remove();
    }
    handlerObj = event.addListener('RNEaseMob', (body) => {
        const {type, subType, data} = body;
        if (handlers[type] && handlers[type][subType]) {
            handlers[type][subType](data);
        }
    });
}

const ChatManager = 'ChatManagerDelegate';
const GroupManager = 'GroupManagerDelegate';
const Client = 'ClientDelegate';

/**
 * 设置消息接收事件回调。
 */
export const setMessageDidReceive = setCallback(ChatManager, 'messageDidReceive');

/**
 * 设置CMD控制消息接收事件回调。
 */
export const setCmdMessageDidReceive = setCallback(ChatManager, 'cmdMessageDidReceive');

/**
 * 设置离开群组事件回调。
 */
export const setDidLeaveGroup = setCallback(GroupManager, 'didLeaveGroup');

/**
 * 设置会话列表更新事件回调。
 */
export const setConversationListDidUpdate = setCallback(ChatManager, 'conversationListDidUpdate');

/**
 * 设置群成员加入回调。
 */
export const setUserDidJoinGroup = setCallback(GroupManager, 'userDidJoinGroup');

/**
 * 设置群成员删除回调。
 */
export const setUserDidLeaveGroup = setCallback(GroupManager, 'userDidLeaveGroup');

/**
 * 设置群主更换回调。
 */
export const setGroupOwnerDidUpdate = setCallback(GroupManager, 'groupOwnerDidUpdate');

/**
 * 设置自动登陆回调。
 * callback方法接收一个对象{code, message}，如果存在code，则表示错误，否则表示成功，message是错误消息。
 */
export const setAutoLoginDidComplete = setCallback(Client, 'autoLoginDidComplete');

/**
 * 设置网络连接状态改变回调。
 * callback方法接收一个IMConstant中的ConnectionState状态值。
 */
export const setConnectionStateDidChange = setCallback(Client, 'connectionStateDidChange');

/**
 * 设置账号在其它设备登录时回调。
 * callback方法不接收参数。
 */
export const setUserAccountDidLoginFromOtherDevice = setCallback(Client, 'userAccountDidLoginFromOtherDevice');

/**
 * 设置账号被从服务器端删除回调。
 * callback方法不接收参数。
 */
export const setUserAccountDidRemoveFromServer = setCallback(Client, 'userAccountDidRemoveFromServer');

function setCallback(type, subType) {
    return function (callback) {
        if (!handlers[type]) {
            handlers[type] = {};
        }
        handlers[type][subType] = callback;
    };
}