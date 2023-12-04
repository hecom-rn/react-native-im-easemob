import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const APNs = NativeModules.APNs;
const isIos = Platform.OS === 'ios';

/**
 * 从服务端获取全局APNs配置。
 * @result silentModeStartTime: { hours, minutes}, silentModeEndTime: { hours, minutes },
 * Android平台没有displayStyle字段
 * iOS平台的nickname已废弃
 */
export const getPushOptionsFromServer = () => NativeUtil(APNs.getPushOptionsFromServer);

/**
 * 单独设置APNs昵称。
 * @param {string} name 新昵称
 */
export const setApnsNickname = (name) => NativeUtil(APNs.setApnsNickname, {name});

/**
 * 设置APNs推送是否显示详情(iOS only)。
 * @param {boolean} showDetail 是否显示详情，为false表示只显示"您有一条新消息"
 */
export const setApnsDisplayStyle = (showDetail) => isIos ? NativeUtil(APNs.setApnsDisplayStyle, {showDetail}) : Promise.resolve();

/**
 * 指定群组是否接收APNs。
 * @param {string} groupId 群ID
 * @param {string} groupType 会话类型：singleChat（单聊）、groupChat（群聊）和 chatRoom（聊天室）。
 * @param {boolean} ignore 是否忽略通知
 */
export const setIgnoreGroupPush = (groupId, groupType, ignore) => NativeUtil(APNs.setIgnoreGroupPush, {
    groupId,
    groupType,
    ignore
});

/**
 * 判断是否开启勿扰   
 * @param {string} groupId 会话 ID
 * @param {string} groupType 会话类型：singleChat（单聊）、groupChat（群聊）和 chatRoom（聊天室）。
 * @returns {boolean} isIgnored 
 */
export const getIgnoreGroupPush = (groupId) => NativeUtil(APNs.getIgnoreGroupPush, { groupId, groupType });

/**
 * 设置推送免打扰设置的状态。
 * 当开始时间和结束时间的hours和minutes都为0时候表示关闭免打扰时间段
 * @param {*} { status, startH = 0, startM = 0, endH = 0, endM = 0 }
 */
export const setNoDisturbStatus = ({ startH = 0, startM = 0, endH = 0, endM = 0 }) =>
    NativeUtil(APNs.setNoDisturbStatus, {
        startH,
        startM,
        endH,
        endM,
    });
