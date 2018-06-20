package com.hecom.easemob;

/**
 * 定义所有发送到JS的Event
 * Created by kevin.bai on 2018/6/13.
 */

class EventName {
    static final String TYPE_CHAT_MANAGER = "chat_manager";
    static final String TYPE_CLIENT = "client";

    static final String CMD_MESSAGE_RECEIVED = "cmd_message";
    static final String LOGIN_ON_OTHER_DEVICE = "loginOnOtherDevice";
    static final String DISCONNECT_CHAT_SERVER = "disconnectChatServer";
    static final String NO_NETWORK = "noNetwork";
}
