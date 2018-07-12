import { Listener } from 'react-native-hecom-common';
import ListenerType from '../constant/ListenerType';
import * as GroupManager from '../native/GroupManager';

export function createGroup(imIds) {
    return GroupManager.createGroup('', '', imIds, '', {})
        .then(group => {
            Listener.trigger([
                ListenerType.group.type,
                ListenerType.group.subTypes.create,
            ], {
                groupId: group.groupId,
            });
            return group;
        });
}
