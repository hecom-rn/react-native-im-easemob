package com.im.easemob;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupReadAck;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageReactionChange;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.adapter.EMAGroup;

import java.util.List;

import static com.im.easemob.IMConstant.CHAT_MANAGER_DELEGATE;
import static com.im.easemob.IMConstant.CLIENT_DELEGATE;
import static com.im.easemob.IMConstant.CMD_MESSAGE_DID_RECEIVE;
import static com.im.easemob.IMConstant.CONNECTION_STATE_DID_CHANGE;
import static com.im.easemob.IMConstant.CONVERSATION_LIST_DID_UPDATE;
import static com.im.easemob.IMConstant.DID_LEAVE_GROUP;
import static com.im.easemob.IMConstant.GROUP_MANAGER_DELEGATE;
import static com.im.easemob.IMConstant.GROUP_OWNER_DID_UPDATE;
import static com.im.easemob.IMConstant.MESSAGE_DID_RECEIVE;
import static com.im.easemob.IMConstant.USER_ACCOUNT_DID_LOGIN_FROM_OTHER_DEVICE;
import static com.im.easemob.IMConstant.USER_ACCOUNT_DID_REMOVE_FROM_SERVER;
import static com.im.easemob.IMConstant.USER_DID_JOIN_GROUP;
import static com.im.easemob.IMConstant.USER_DID_LEAVE_GROUP;

/**
 * 处理环信的监听事件
 * Created by kevin.bai on 2018/7/30.
 */
public class EasemobListener implements EMGroupChangeListener, EMMessageListener, EMConversationListener,
        EMConnectionListener, EMMultiDeviceListener, EMContactListener {
    private final Context mContext;

    EasemobListener(Context context) {
        mContext = context;
    }

    /******************** ConnectionListener ********************/

    @Override
    public void onConnected() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        EasemobHelper.getInstance()
                .sendEvent(CLIENT_DELEGATE, CONNECTION_STATE_DID_CHANGE, IMConstant.ConnectionState.CONNECTED);
    }

    @Override
    public void onDisconnected(int error) {
        if (error == EMError.USER_REMOVED) {
            // 显示帐号已经被移除
            EasemobHelper.getInstance().sendEvent(CLIENT_DELEGATE, USER_ACCOUNT_DID_REMOVE_FROM_SERVER);
        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            // 显示帐号在其他设备登录
            EasemobHelper.getInstance().sendEvent(CLIENT_DELEGATE, USER_ACCOUNT_DID_LOGIN_FROM_OTHER_DEVICE);
        } else {
            EasemobHelper.getInstance()
                    .sendEvent(CLIENT_DELEGATE, CONNECTION_STATE_DID_CHANGE, IMConstant.ConnectionState.DISCONNEDTED);
//            if (NetUtils.hasNetwork(mContext)) {
//                //连接不到聊天服务器
//            } else {
//                //当前网络不可用，请检查网络设置
//            }
        }
    }

    @Override
    public void onTokenExpired() {
        EMConnectionListener.super.onTokenExpired();
    }

    @Override
    public void onTokenWillExpire() {
        EMConnectionListener.super.onTokenWillExpire();
    }

    @Override
    public void onLogout(int errorCode) {
        EMConnectionListener.super.onLogout(errorCode);
    }

    /******************** ConversationListener ********************/

    @Override
    public void onConversationUpdate() {
        EasemobHelper.getInstance().sendEvent(CHAT_MANAGER_DELEGATE, CONVERSATION_LIST_DID_UPDATE);

    }

    @Override
    public void onConversationRead(String s, String s1) {

    }

    /******************** GroupListener ********************/

    @Override
    public void onInvitationReceived(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onRequestToJoinAccepted(String s, String s1, String s2) {

    }

    @Override
    public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onInvitationAccepted(String s, String s1, String s2) {

    }

    @Override
    public void onInvitationDeclined(String s, String s1, String s2) {

    }

    @Override
    public void onUserRemoved(String s, String s1) {
        sendLeaveGroupEvent(s, s1);
    }

    private void sendLeaveGroupEvent(String groupId, String groupSubject) {
        Log.i("sendLeaveGroupEvent", "groupId = " + groupId + ", groupSubject = " + groupSubject);
        WritableMap map = Arguments.createMap();
        WritableMap group = Arguments.createMap();
        group.putString("groupId", groupId);
        map.putMap("group", group);
        map.putInt("reason", EMAGroup.EMGroupLeaveReason.BE_KICKED.ordinal());
        EasemobHelper.getInstance().sendEvent(GROUP_MANAGER_DELEGATE, DID_LEAVE_GROUP, map);

    }

    @Override
    public void onGroupDestroyed(String s, String s1) {
        sendLeaveGroupEvent(s, s1);
    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

    }

    @Override
    public void onMuteListAdded(String s, List<String> list, long l) {

    }

    @Override
    public void onMuteListRemoved(String s, List<String> list) {

    }

    @Override
    public void onWhiteListAdded(String s, List<String> list) {

    }

    @Override
    public void onWhiteListRemoved(String s, List<String> list) {

    }

    @Override
    public void onAllMemberMuteStateChanged(String s, boolean b) {

    }

    @Override
    public void onAdminAdded(String s, String s1) {

    }

    @Override
    public void onAdminRemoved(String s, String s1) {

    }

    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
        WritableMap map = Arguments.createMap();
        map.putString("groupId", groupId);
        map.putString("newOwner", newOwner);
        map.putString("oldOwner", oldOwner);
        EasemobHelper.getInstance().sendEvent(GROUP_MANAGER_DELEGATE, GROUP_OWNER_DID_UPDATE, map);
    }

    @Override
    public void onMemberJoined(String groupId, String member) {
        WritableMap map = Arguments.createMap();
        map.putString("groupId", groupId);
        map.putString("username", member);
        EasemobHelper.getInstance().sendEvent(GROUP_MANAGER_DELEGATE, USER_DID_JOIN_GROUP, map);
    }

    @Override
    public void onMemberExited(String groupId, String member) {
        WritableMap map = Arguments.createMap();
        map.putString("groupId", groupId);
        map.putString("username", member);
        EasemobHelper.getInstance().sendEvent(GROUP_MANAGER_DELEGATE, USER_DID_LEAVE_GROUP, map);
    }

    @Override
    public void onAnnouncementChanged(String s, String s1) {

    }

    @Override
    public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

    }

    @Override
    public void onSharedFileDeleted(String s, String s1) {

    }

    @Override
    public void onSpecificationChanged(EMGroup group) {
        EMGroupChangeListener.super.onSpecificationChanged(group);
    }

    @Override
    public void onStateChanged(EMGroup group, boolean isDisabled) {
        EMGroupChangeListener.super.onStateChanged(group, isDisabled);
    }

    /******************** MessageListener ********************/

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        EasemobHelper.getInstance().sendEvent(CHAT_MANAGER_DELEGATE, MESSAGE_DID_RECEIVE, EasemobConverter
                .convertList(list));
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        EasemobHelper.getInstance()
                .sendEvent(IMConstant.CHAT_MANAGER_DELEGATE, CMD_MESSAGE_DID_RECEIVE, EasemobConverter
                        .convertList(list));
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onGroupMessageRead(List<EMGroupReadAck> groupReadAcks) {
        EMMessageListener.super.onGroupMessageRead(groupReadAcks);
    }

    @Override
    public void onReadAckForGroupMessageUpdated() {
        EMMessageListener.super.onReadAckForGroupMessageUpdated();
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onReactionChanged(List<EMMessageReactionChange> messageReactionChangeList) {
        EMMessageListener.super.onReactionChanged(messageReactionChangeList);
    }

    @Override
    public void onContactAdded(String s) {

    }

    @Override
    public void onContactDeleted(String s) {

    }

    @Override
    public void onContactInvited(String s, String s1) {

    }

    @Override
    public void onFriendRequestAccepted(String s) {

    }

    @Override
    public void onFriendRequestDeclined(String s) {

    }

    @Override
    public void onContactEvent(int i, String s, String s1) {

    }

    @Override
    public void onGroupEvent(int i, String s, List<String> list) {

    }

    @Override
    public void onChatThreadEvent(int event, String target, List<String> usernames) {
        EMMultiDeviceListener.super.onChatThreadEvent(event, target, usernames);
    }

    @Override
    public void onCircleChannelEvent(int event, String channelId, List<String> usernames) {
        EMMultiDeviceListener.super.onCircleChannelEvent(event, channelId, usernames);
    }

    @Override
    public void onMessageRemoved(String conversationId, String deviceId) {
        EMMultiDeviceListener.super.onMessageRemoved(conversationId, deviceId);
    }
}
