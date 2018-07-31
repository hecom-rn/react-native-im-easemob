package com.hecom.easemob;

import android.media.MediaMetadataRetriever;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin.bai on 2018/7/12.
 */

public class ChatManager extends ReactContextBaseJavaModule {

    ChatManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ChatManager";
    }

    @ReactMethod
    public void getConversation(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, "conversationId", promise)) {
            return;
        }
        String conversationId = params.getString("conversationId");
        EMConversation.EMConversationType type = EMConversation.EMConversationType.Chat;
        boolean ifCreate = false;
        if (params.hasKey("type")) {
            type = EasemobConverter.toConversationType(params.getInt("type"));
        }
        if (params.hasKey("ifCreate")) {
            ifCreate = params.getBoolean("ifCreate");
        }

        EMConversation conversation = EMClient.getInstance().chatManager()
                .getConversation(conversationId, type, ifCreate);
        promise.resolve(EasemobConverter.convert(conversation));
    }

    @ReactMethod
    public void getAllConversations(Promise promise) {
        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
        WritableArray result = Arguments.createArray();
        for (String key : map.keySet()) {
            result.pushMap(EasemobConverter.convert(map.get(key)));
        }
        promise.resolve(result);
    }

    @ReactMethod
    public void deleteConversation(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, "conversationId", promise)) {
            return;
        }
        String conversationId = params.getString("conversationId");
        boolean ifClearAllMessage = false;
        if (params.hasKey("ifClearAllMessage")) {
            ifClearAllMessage = params.getBoolean("ifClearAllMessage");
        }
        promise.resolve(EMClient.getInstance().chatManager().deleteConversation(conversationId, ifClearAllMessage));
    }

    @ReactMethod
    public void loadMessages(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"conversationId"}, promise)) {
            return;
        }
        String conversationId = params.getString("conversationId");
        EMConversation.EMConversationType type = EMConversation.EMConversationType.Chat;
        if (params.hasKey("chatType")) {
            type = EasemobConverter.toConversationType(params.getInt("chatType"));
        }
        String fromId = "";
        if (params.hasKey("fromId")) {
            fromId = params.getString("fromId");
        }
        int count = 20;
        if (params.hasKey("count")) {
            count = params.getInt("count");
        }
        List<EMMessage> list = EMClient.getInstance().chatManager().getConversation(conversationId, type)
                .loadMoreMsgFromDB(fromId, count);
        promise.resolve(EasemobConverter.convertList(list));
    }

    @ReactMethod
    public void sendMessage(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"chatType", "messageType", "to", "body"}, promise)) {
            return;
        }
        ReadableMap ext = null;
        if (params.hasKey("messageExt")) {
            if (params.getType("messageExt") == ReadableType.Map) {
                ext = params.getMap("messageExt");
            } else {
                promise.reject("-1", "messageExt字段必须是对象");
                return;
            }
        }
        EMMessage.ChatType type = EasemobConverter.toChatType(params.getInt("chatType"));
        EMMessage.Type messageType = EasemobConverter.toMessageType(params.getInt("messageType"));
        String to = params.getString("to");
        ReadableMap body = params.getMap("body");

        EMMessage message;
        if (messageType == EMMessage.Type.IMAGE) {
            message = EMMessage.createImageSendMessage(body.getString("path"), false, to);
        } else if (messageType == EMMessage.Type.LOCATION) {
            message = EMMessage.createLocationSendMessage(
                    body.getDouble("latitude"), body.getDouble("longitude"),
                    body.getString("address"), to);
        } else if (messageType == EMMessage.Type.VIDEO) {
            message = EMMessage.createVideoSendMessage(body.getString("path"),
                    body.getString("thumbPath"), body.getInt("duration"), to);
        } else if (messageType == EMMessage.Type.FILE) {
            message = EMMessage.createFileSendMessage(body.getString("path"), to);
        } else if (messageType == EMMessage.Type.VOICE) {
            int duration;
            String path = body.getString("path");
            if (body.hasKey("duration")) {
                duration = body.getInt("duration");
            } else {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(path);
                duration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            }
            message = EMMessage.createVoiceSendMessage(path, duration, to);
        } else {
            message = EMMessage.createTxtSendMessage(body.getString("text"), to);
        }
        message.setChatType(type);
        if (ext != null) {
            try {
                setExt(message, ext);
            } catch (JSONException e) {
                promise.reject(e);
                return;
            }
        }
        EMClient.getInstance().chatManager().sendMessage(message);
        promise.resolve(EasemobConverter.convert(message));
    }

    private void setExt(EMMessage message, ReadableMap map) throws JSONException {
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = map.getType(key);
            switch (type) {
                case Null:
                    break;
                case Boolean:
                    message.setAttribute(key, map.getBoolean(key));
                    break;
                case Number:
                    message.setAttribute(key, map.getInt(key));
                    break;
                case String:
                    message.setAttribute(key, map.getString(key));
                    break;
                case Map:
                    message.setAttribute(key, EasemobConverter.toJsonObject(map.getMap(key)));
                    break;
                case Array:
                    message.setAttribute(key, EasemobConverter.toJsonArray(map.getArray(key)));
                    break;
            }
        }
    }

}
