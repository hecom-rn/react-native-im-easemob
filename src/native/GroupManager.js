import { NativeModules, Platform } from 'react-native';
import NativeUtil from './native';

const GroupManager = NativeModules.GroupManager;
const isAndroid = Platform.OS === 'android';

/**
 * 创建群组。
 * @param imIds 群组成员ImID列表
 */
export const createGroup = (imIds) =>
    NativeUtil(GroupManager.createGroup, {subject: '', description: '', invitees: imIds, message: '', setting: {}});

/**
 * 获取我创建/加入的群聊列表，不包括成员列表。
 */
export const getJoinedGroups = () =>
    NativeUtil(GroupManager.getJoinedGroups, undefined);

/**
 * 获取群成员列表。
 * @param groupId 群组ID
 * @param cursor 迭代的指针
 * @param pageSize 页大小
 */
export const getGroupMemberList = (groupId, cursor = '', pageSize = 10) =>
    NativeUtil(GroupManager.getGroupMemberList, {groupId, cursor, pageSize});

/**
 * 获取群组详情，包括成员列表。
 * @param groupId 群组ID
 */
export const getGroupSpecification = (groupId) =>
    NativeUtil(GroupManager.getGroupSpecification, {groupId});

/**
 * 添加群成员。
 * @param groupId 群组ID
 * @param members 成员ImID列表
 */
export const addOccupants = (groupId, members) =>
    NativeUtil(GroupManager.addOccupants, {groupId, members});

/**
 * 删除群成员。
 * @param groupId 群组ID
 * @param members 成员ImID列表
 */
export const removeOccupants = (groupId, members) =>
    NativeUtil(GroupManager.removeOccupants, {groupId, members});

/**
 * 修改群组名称。
 * @param groupId 群组ID
 * @param subject 新名称
 */
export const changeGroupSubject = (groupId, subject) =>
    NativeUtil(GroupManager.changeGroupSubject, {groupId, subject});

/**
 * 退出群组
 * @param groupId 群组ID
 */
export const leaveGroup = (groupId) =>
    NativeUtil(GroupManager.leaveGroup, {groupId});

/**
 * 解散群。
 * @param groupId 群组ID
 */
export const destroyGroup = (groupId) =>
    NativeUtil(GroupManager.destroyGroup, {groupId});

/**
 * 更新群组附加内容。
 * @param groupId 群组ID
 * @param ext 附加内容
 */
export const updateGroupExt = (groupId, ext) =>
    NativeUtil(GroupManager.updateGroupExt,  isAndroid ? {groupId, ext: JSON.stringify(ext)} : {groupId, ext});

/**
 * 转移群主。
 * @param groupId 群主ID
 * @param newOwner 新群主ImID
 */
export const updateGroupOwner = (groupId, newOwner) =>
    NativeUtil(GroupManager.updateGroupOwner, {groupId, newOwner});
