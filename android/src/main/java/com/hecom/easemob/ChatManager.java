package com.hecom.easemob;

import android.media.MediaMetadataRetriever;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.Map;

/**
 * Created by kevin.bai on 2018/7/12.
 */

public class ChatManager extends ReactContextBaseJavaModule {
    private static final String CONVERSATION_TYPE_CHAT = "0";
    private static final String CONVERSATION_TYPE_GROUP = "1";

    private static final int MESSAGE_TYPE_TEXT = 1;
    private static final int MESSAGE_TYPE_IMAGE = 2;
    private static final int MESSAGE_TYPE_VIDEO = 3;
    private static final int MESSAGE_TYPE_LOCATION = 4;
    private static final int MESSAGE_TYPE_VOICE = 5;
    private static final int MESSAGE_TYPE_FILE = 6;
    private static final int MESSAGE_TYPE_CMD = 7;

    ChatManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ChatManager";
    }

    @ReactMethod
    public void getConversation(ReadableMap param, Promise promise) {
        if (!param.hasKey("conversationId")) {
            promise.reject("-1", "必须包含会话id");
            return;
        }
        String conversationId = param.getString("conversationId");
        EMConversation.EMConversationType type = EMConversation.EMConversationType.Chat;
        boolean ifCreate = false;
        if (param.hasKey("type")) {
            type = convertConversationType(param.getString("type"));
        }
        if (param.hasKey("ifCreate")) {
            ifCreate = param.getBoolean("ifCreate");
        }

        EMConversation conversation = EMClient.getInstance().chatManager()
                .getConversation(conversationId, type, ifCreate);
        promise.resolve(convertConversation(conversation));
    }

    @ReactMethod
    public void getAllConversations(Promise promise) {
        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
        WritableArray result = Arguments.createArray();
        for (String key : map.keySet()) {
            result.pushMap(convertConversation(map.get(key)));
        }
        promise.resolve(result);
    }

    private boolean checkParamKey(ReadableMap params, String key, Promise promise) {
        if (!params.hasKey(key)) {
            promise.reject("-1", "必须包含" + key);
            return true;
        }
        return false;
    }

    @ReactMethod
    public void sendMessage(ReadableMap params, Promise promise) {
        if (checkParamKey(params, "conversationId", promise)
                || checkParamKey(params, "chatType", promise)
                || checkParamKey(params, "messageType", promise)
                || checkParamKey(params, "to", promise)
                || checkParamKey(params, "body", promise)) {
            return;
        }
        String id = params.getString("conversationId");
        EMMessage.ChatType type = convertChatType(params.getString("chatType"));
        EMMessage.Type messageType = convertMessageType(params.getInt("messageType"));
        String to = params.getString("to");
        String content = params.getString("body");
        EMMessage message;
        if (messageType == EMMessage.Type.IMAGE) {
            message = EMMessage.createImageSendMessage(content, false, to);
        } else if (messageType == EMMessage.Type.VOICE) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(content);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int mDuration = Integer.valueOf(duration);
            message = EMMessage.createVoiceSendMessage(content, mDuration, to);
        } else {
            message = EMMessage.createTxtSendMessage(content, to);
        }
        message.setChatType(type);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private EMMessage.Type convertMessageType(int type) {
        switch (type) {
            case MESSAGE_TYPE_TEXT:
            default:
                return EMMessage.Type.TXT;
            case MESSAGE_TYPE_IMAGE:
                return EMMessage.Type.IMAGE;
            case MESSAGE_TYPE_VIDEO:
                return EMMessage.Type.VIDEO;
            case MESSAGE_TYPE_LOCATION:
                return EMMessage.Type.LOCATION;
            case MESSAGE_TYPE_VOICE:
                return EMMessage.Type.VOICE;
            case MESSAGE_TYPE_FILE:
                return EMMessage.Type.FILE;
            case MESSAGE_TYPE_CMD:
                return EMMessage.Type.CMD;
        }
    }

    private WritableMap convertConversation(EMConversation conversation) {
        WritableMap result = Arguments.createMap();
        result.putString("conversationId", conversation.conversationId());
        result.putMap("latestMessage", convertMessage(conversation.getLastMessage()));
        result.putInt("unreadMessagesCount", conversation.getUnreadMsgCount());
        result.putString("chatType", toConversationType(conversation.getType()));
        return result;
    }

    private WritableMap convertMessage(EMMessage message) {
        WritableMap result = Arguments.createMap();
        result.putString("conversationId", message.conversationId());
        result.putString("from", message.getFrom());
        result.putString("to", message.getTo());
        result.putString("msgId", message.getMsgId());
        result.putInt("timestamp", (int) message.getMsgTime());
        result.putMap("body", convertBody(message.getBody()));
        return result;
    }

    private WritableMap convertBody(EMMessageBody body) {
        WritableMap result = Arguments.createMap();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody text = (EMTextMessageBody) body;
            result.putString("text", text.getMessage());
        } else if (body instanceof EMImageMessageBody) {
            EMImageMessageBody image = (EMImageMessageBody) body;
            result.putString("remoteUrl", image.getRemoteUrl());
        }
        return result;
    }

    private EMMessage.ChatType convertChatType(String type) {
        if (CONVERSATION_TYPE_CHAT.equals(type)) {
            return EMMessage.ChatType.Chat;
        } else if (CONVERSATION_TYPE_GROUP.equals(type)) {
            return EMMessage.ChatType.GroupChat;
        } else {
            return EMMessage.ChatType.Chat;
        }
    }

    private String toConversationType(EMConversation.EMConversationType type) {
        switch (type) {
            case Chat:
            default:
                return CONVERSATION_TYPE_CHAT;
            case GroupChat:
                return CONVERSATION_TYPE_GROUP;
        }
    }

    private EMConversation.EMConversationType convertConversationType(String type) {
        if (CONVERSATION_TYPE_CHAT.equals(type)) {
            return EMConversation.EMConversationType.Chat;
        } else if (CONVERSATION_TYPE_GROUP.equals(type)) {
            return EMConversation.EMConversationType.GroupChat;
        } else {
            return EMConversation.EMConversationType.Chat;
        }
    }
}
