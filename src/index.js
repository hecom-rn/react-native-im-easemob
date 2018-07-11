import * as EventEmitter from './eventemitter';
import * as ChatManager from './module/ChatManager';
import * as Client from './module/Client';
import * as GroupManager from './module/GroupManager';

export {
    EventEmitter,
    ChatManager,
    GroupManager,
    Client,
};

export const EaseMobEventTypes = {
    chat_manager: {
        type: 'chat_manager',
        cmdMessage: 'cmd_message'
    },
    client: {
        type: 'client',
        logout: 'logout',
        login: 'login',
        loginOnOtherDevice: 'loginOnOtherDevice',
        disconnectChatServer: 'disconnectChatServer',
        noNetwork: 'noNetwork',
    },
};