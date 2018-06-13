package com.hecom.easemob;

/**
 * 定义所有发送到JS的Event
 * Created by kevin.bai on 2018/6/13.
 */

public class EventName {
    public static final String CONNECTION_CONNECTED = "EMConnectionConnectedEvent";
    public static final String CONNECTION_DISCONNECTED = "EMConnectionDisconnectedEvent";
    public static final String MESSAGE_RECEIVED = "EMMessageReceivedEvent";
    public static final String CMD_MESSAGE_RECEIVED = "EMCmdMessageReceivedEvent";
    public static final String MESSAGE_READ = "EMMessageReadEvent";
    public static final String MESSAGE_DELIVERED = "EMMessageDeliveredEvent";
    public static final String MESSAGE_CHANGED = "EMMessageChangedEvent";
}
