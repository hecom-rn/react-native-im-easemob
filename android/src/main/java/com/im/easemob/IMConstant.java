package com.im.easemob;

import androidx.annotation.StringDef;

/**
 * 定义所有发送到JS的Event
 * Created by kevin.bai on 2018/6/13.
 */

public class IMConstant {
    public static final class MessageKey{
        public static final String CONVERSATION_ID = "conversationId";
        public static final String CHAT_TYPE = "chatType";
        public static final String FROM_ID = "fromId";
        public static final String MESSAGE_ID = "messageId";
    }

    public static class ChatType {
        public static final int SINGLE = 0;
        public static final int GROUP = 1;
    }

    public static class MessageType {
        public static final int TEXT = 1;
        public static final int IMAGE = 2;
        public static final int VIDEO = 3;
        public static final int LOCATION = 4;
        public static final int VOICE = 5;
        public static final int FILE = 6;
        public static final int CMD = 7;
    }

    public static class MessageStatus {
        public static final int PENDING = 0; // 发送未开始
        public static final int DELIVERING = 1; // 正在发送
        public static final int SUCCEED = 2; // 发送成功
        public static final int FAILED = 3; // 发送失败
    }

    public static class MessageSearchDirection {
        public static final int UP = 0;
        public static final int DOWN = 1;
    }

    public static class ConnectionState {
        public static final int CONNECTED = 0; // 已连接
        public static final int DISCONNEDTED = 1; // 未连接
    }

    public static class EMMessageDirection {
        public static final int SEND = 0;
        public static final int RECEIVE = 1;
    }

    public static final String CHAT_MANAGER_DELEGATE = "ChatManagerDelegate";
    public static final String GROUP_MANAGER_DELEGATE = "GroupManagerDelegate";
    public static final String CLIENT_DELEGATE = "ClientDelegate";

    @StringDef({CHAT_MANAGER_DELEGATE, GROUP_MANAGER_DELEGATE, CLIENT_DELEGATE})
    public @interface Type {
    }

    public static final String MESSAGE_DID_RECEIVE = "messageDidReceive";
    public static final String CMD_MESSAGE_DID_RECEIVE = "cmdMessageDidReceive";
    public static final String CONVERSATION_LIST_DID_UPDATE = "conversationListDidUpdate";

    public static final String USER_DID_JOIN_GROUP = "userDidJoinGroup";
    public static final String USER_DID_LEAVE_GROUP = "userDidLeaveGroup";
    public static final String GROUP_OWNER_DID_UPDATE = "groupOwnerDidUpdate";

    public static final String USER_ACCOUNT_DID_REMOVE_FROM_SERVER = "userAccountDidRemoveFromServer";
    public static final String USER_ACCOUNT_DID_LOGIN_FROM_OTHER_DEVICE = "userAccountDidLoginFromOtherDevice";
    public static final String CONNECTION_STATE_DID_CHANGE = "connectionStateDidChange";
    public static final String AUTO_LOGIN_DID_COMPLETE = "autoLoginDidComplete";

    @StringDef({MESSAGE_DID_RECEIVE, CMD_MESSAGE_DID_RECEIVE, CONVERSATION_LIST_DID_UPDATE, USER_DID_LEAVE_GROUP,
            USER_DID_JOIN_GROUP, GROUP_OWNER_DID_UPDATE, USER_ACCOUNT_DID_LOGIN_FROM_OTHER_DEVICE,
            USER_ACCOUNT_DID_REMOVE_FROM_SERVER, CONNECTION_STATE_DID_CHANGE, AUTO_LOGIN_DID_COMPLETE})
    public @interface SubType {

    }
}
