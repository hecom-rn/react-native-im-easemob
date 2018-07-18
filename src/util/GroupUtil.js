import * as GroupManager from '../native/GroupManager';

export function createGroup(imIds) {
    return GroupManager.createGroup('', '', imIds, '', {});
}
