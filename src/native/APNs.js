import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const APNs = NativeModules.APNs;
const isIos = Platform.OS === 'ios';

/**
 * 从服务端获取全局APNs配置(iOS only)。
 */
export const getPushOptionsFromServer = () => isIos ? NativeUtil(APNs.getPushOptionsFromServer) : Promise.resolve({});

/**
 * 单独设置APNs昵称(iOS only)。
 * @param {string} name 新昵称
 */
export const setApnsNickname = (name) => isIos ? NativeUtil(APNs.setApnsNickname, {name}) : Promise.resolve();

/**
 * 设置APNs推送是否显示详情(iOS only)。
 * @param {boolean} showDetail 是否显示详情，为false表示只显示"您有一条新消息"
 */
export const setApnsDisplayStyle = (showDetail) => isIos ? NativeUtil(APNs.setApnsDisplayStyle, {showDetail}) : Promise.resolve();

/**
 * 指定群组是否接收APNs(iOS only)。
 * @param {string} groupId 群ID
 * @param {boolean} ignore 是否忽略通知
 */
export const ignoreGroupPush = (groupId, ignore) => ignoreGroupsPush([groupId], ignore);

/**
 * 批量指定群组是否接收APNs(iOS only)。
 * @param {string[]} groupIds 群ID列表
 * @param {boolean} ignore 是否忽略通知
 */
export const ignoreGroupsPush = (groupIds, ignore) => isIos ? NativeUtil(APNs.ignoreGroupsPush, {groupIds, ignore}) : Promise.resolve({});

/**
 * 获取不接收APNs的群组ID列表(iOS only)。
 */
export const getIgnoredGroupIds = () => isIos ? NativeUtil(APNs.getIgnoredGroupIds) : Promise.resolve([]);

/**
 * 设置推送免打扰设置的状态。
 * @param {boolean} status true表示打开免打扰设置，false表示关闭免打扰设置
 * @param {number} startH 开始的小时数，0-24，默认为0
 * @param {number} endH 结束的小时数，0-24，默认为24
 */
export const setNoDisturbStatus = (status, startH = 0, endH = 24) => isIos ? NativeUtil(APNs.setNoDisturbStatus, {status, startH, endH}) : Promise.resolve();
