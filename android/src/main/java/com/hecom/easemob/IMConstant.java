package com.hecom.easemob;

import android.support.annotation.StringDef;

/**
 * 定义所有发送到JS的Event
 * Created by kevin.bai on 2018/6/13.
 */

public class IMConstant {
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

    public static class MessageSearchDirection {
        public static final int UP = 0;
        public static final int DOWN = 1;
    }

    public static class EMMessageDirection {
        public static final int SEND = 0;
        public static final int RECEIVE = 1;
    }

    public static final String CHAT_MANAGER_DELEGATE = "ChatManagerDelegate";

    @StringDef({CHAT_MANAGER_DELEGATE})
    public @interface Type {
    }

    public static final String MESSAGE_DID_RECEIVE = "messageDidReceive";
    public static final String CMD_MESSAGE_DID_RECEIVE = "cmdMessageDidReceive";
    public static final String CONVERSATION_LIST_DID_UPDATE = "conversationListDidUpdate";

    @StringDef({MESSAGE_DID_RECEIVE, CMD_MESSAGE_DID_RECEIVE, CONVERSATION_LIST_DID_UPDATE})
    public @interface SubType {

    }
}
