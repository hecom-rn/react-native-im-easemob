package com.hecom.easemob;

import android.content.Context;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.List;

import static com.hecom.easemob.IMConstant.CHAT_MANAGER_DELEGATE;
import static com.hecom.easemob.IMConstant.CMD_MESSAGE_DID_RECEIVE;
import static com.hecom.easemob.IMConstant.CONVERSATION_LIST_DID_UPDATE;
import static com.hecom.easemob.IMConstant.MESSAGE_DID_RECEIVE;

/**
 * 处理环信的监听事件
 * Created by kevin.bai on 2018/7/30.
 */
public class EasemobListener implements EMGroupChangeListener, EMMessageListener, EMConversationListener,
        EMConnectionListener {
    private final Context mContext;

    EasemobListener(Context context) {
        mContext = context;
    }

    @Override
    public void onConnected() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    @Override
    public void onDisconnected(int i) {
//        if (i == EMError.USER_REMOVED) {
//            // 账号在服务端被删除
//        } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                    sendEvent(TYPE_CLIENT, LOGIN_ON_OTHER_DEVICE);
//        } else {
//            if (NetUtils.hasNetwork(mContext)) {
//                        sendEvent(TYPE_CLIENT, DISCONNECT_CHAT_SERVER);
//            } else {
//                        sendEvent(TYPE_CLIENT, NO_NETWORK);
//            }
//        }
    }

    @Override
    public void onCoversationUpdate() {
        EasemobHelper.getInstance().sendEvent(CHAT_MANAGER_DELEGATE, CONVERSATION_LIST_DID_UPDATE);
    }

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

    }

    @Override
    public void onGroupDestroyed(String s, String s1) {

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
    public void onAdminAdded(String s, String s1) {

    }

    @Override
    public void onAdminRemoved(String s, String s1) {

    }

    @Override
    public void onOwnerChanged(String s, String s1, String s2) {

    }

    @Override
    public void onMemberJoined(String s, String s1) {

    }

    @Override
    public void onMemberExited(String s, String s1) {

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
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
