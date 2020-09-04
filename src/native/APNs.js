import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const APNs = NativeModules.APNs;
const isIos = Platform.OS === 'ios';

/**
 * 从服务端获取全局APNs配置。
 * @result {displayName:string, displayStyle:number, noDisturbingEndH:number, noDisturbingStartH:number, noDisturbStatus:0|1}
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
 * @param {boolean} ignore 是否忽略通知
 */
export const ignoreGroupPush = (groupId, ignore) => ignoreGroupsPush([groupId], ignore);

/**
 * 批量指定群组是否接收APNs。
 * @param {string[]} groupIds 群ID列表
 * @param {boolean} ignore 是否忽略通知
 */
export const ignoreGroupsPush = (groupIds, ignore) => NativeUtil(APNs.ignoreGroupsPush, {
    groupIds,
    ignore
});

/**
 * 获取不接收APNs的群组ID列表(iOS only)。
 */
export const getIgnoredGroupIds = () => NativeUtil(APNs.getIgnoredGroupIds);

/**
 * 设置推送免打扰设置的状态。
 * @param {boolean} status true表示打开免打扰设置，false表示关闭免打扰设置
 * @param {number} startH 开始的小时数，0-24，默认为0
 * @param {number} endH 结束的小时数，0-24，默认为24
 */
export const setNoDisturbStatus = (status, startH = 0, endH = 24) => NativeUtil(APNs.setNoDisturbStatus, {
    status,
    startH,
    endH
});
