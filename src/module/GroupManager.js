/**
 * Created by zzg on 2018/7/11.
 */
import { NativeModules } from 'react-native';
import NativeUtil from '../native';

const GroupManager = NativeModules.GroupManager;

export const createGroup = (subject, description, invitees, message, setting) =>
    NativeUtil(GroupManager.createGroup, {subject, description, invitees, message, setting});
