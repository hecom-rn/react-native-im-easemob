import { NativeModules } from 'react-native';
import NativeUtil from './native';

const GroupManager = NativeModules.GroupManager;

export const createGroup = (imIds) =>
    NativeUtil(GroupManager.createGroup, {subject: '', description: '', invitees: imIds, message: '', setting: {}});
export const getJoinedGroups = () =>
    NativeUtil(GroupManager.getJoinedGroups, undefined);
export const getGroupMemberList = (groupId, cursor = '', pageSize = 10) =>
    NativeUtil(GroupManager.getGroupMemberList, {group, cursor, pageSize});
export const getGroupSpecification = (groupId) =>
    NativeUtil(GroupManager.getGroupSpecification, {group});

