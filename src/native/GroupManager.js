import { NativeModules } from 'react-native';
import NativeUtil from './native';

const GroupManager = NativeModules.GroupManager;

export const createGroup = (imIds) =>
    NativeUtil(GroupManager.createGroup, {subject:'', description:'', invitees:imIds, message:'', setting:{}});
