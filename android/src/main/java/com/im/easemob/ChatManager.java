package com.im.easemob;

import android.content.Context;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.adapter.message.EMAImageMessageBody;
import com.hyphenate.chat.adapter.message.EMAVideoMessageBody;
import com.hyphenate.chat.adapter.message.EMAVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import static com.im.easemob.IMConstant.MessageKey.CHAT_TYPE;
import static com.im.easemob.IMConstant.MessageKey.CONVERSATION_ID;
import static com.im.easemob.IMConstant.MessageKey.FROM_ID;
import static com.im.easemob.IMConstant.MessageKey.MESSAGE_ID;

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
    public void registerUser(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"username", "password"}, promise)) {
            return;
        }
        try {
            EMClient.getInstance().createAccount(params.getString("username"), params.getString("password"));
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getConversation(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, CONVERSATION_ID, promise)) {
            return;
        }
        String conversationId = params.getString(CONVERSATION_ID);
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
        if (CheckUtil.checkParamKey(params, CONVERSATION_ID, promise)) {
            return;
        }
        String conversationId = params.getString(CONVERSATION_ID);
        boolean ifClearAllMessage = false;
        if (params.hasKey("ifClearAllMessage")) {
            ifClearAllMessage = params.getBoolean("ifClearAllMessage");
        }
        promise.resolve(EMClient.getInstance().chatManager().deleteConversation(conversationId, ifClearAllMessage));
    }

    @ReactMethod
    public void loadMessages(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{CONVERSATION_ID}, promise)) {
            return;
        }
        String conversationId = params.getString(CONVERSATION_ID);
        EMConversation.EMConversationType type = EMConversation.EMConversationType.Chat;
        if (params.hasKey(CHAT_TYPE)) {
            type = EasemobConverter.toConversationType(params.getInt(CHAT_TYPE));
        }
        String fromId = "";
        if (params.hasKey(FROM_ID)) {
            fromId = params.getString(FROM_ID);
        }
        int count = 20;
        if (params.hasKey("count")) {
            count = params.getInt("count");
        }
        List<EMMessage> list = EMClient.getInstance().chatManager().getConversation(conversationId, type, true)
                .loadMoreMsgFromDB(fromId, count);

        promise.resolve(EasemobConverter.convertList(list));
    }

    @ReactMethod
    public void deleteMessage(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{CONVERSATION_ID, MESSAGE_ID}, promise)) {
            return;
        }
        String conversationId = params.getString(CONVERSATION_ID);
        String messageId = params.getString(MESSAGE_ID);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(conversationId);
        if (conversation != null) {
            conversation.removeMessage(messageId);
            promise.resolve(null);
        } else {
            promise.reject("-1", "会话不存在，conversationId=" + conversationId);
        }
    }

    @ReactMethod
    public void recallMessage(ReadableMap params, Promise promise) {
        if (CheckUtil
                .checkParamKey(params, new String[]{MESSAGE_ID}, promise)) {
            return;
        }
        String messageId = params.getString(MESSAGE_ID);
        EMMessage message = EMClient.getInstance().chatManager().getMessage(messageId);
        try {
            EMClient.getInstance().chatManager().recallMessage(message);
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void insertMessage(ReadableMap params, final Promise promise) {
        if (CheckUtil
                .checkParamKey(params, new String[]{CHAT_TYPE, CONVERSATION_ID, "direction", "messageType", "body"},
                        promise)) {
            return;
        }
        try {
            EMMessage message = buildMessage(params, true);
            if (message != null) {
                message.setStatus(EMMessage.Status.SUCCESS);
                EMChatManager manager = EMClient.getInstance().chatManager();
                EMConversation conversation = manager
                        .getConversation(message.conversationId(), getCType(message.getChatType()), true);
                if (conversation != null) {
                    manager.downloadAttachment(message);
                    manager.downloadThumbnail(message);
                    conversation.insertMessage(message);
                    promise.resolve(EasemobConverter.convert(message));
                } else {
                    promise.reject("-1", "会话不存在");
                }
            } else {
                promise.reject("-1", "无法识别的messageType");
            }
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void sendMessage(final ReadableMap params, final Promise promise) {
        if (CheckUtil
                .checkParamKey(params, new String[]{CHAT_TYPE, "messageType", "to", "body"}, promise)) {
            return;
        }
        try {
            final EMMessage message = buildMessage(params, false);
            if (message != null) {
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        promise.resolve(EasemobConverter.convert(message));
                    }

                    @Override
                    public void onError(int i, String s) {
                        promise.resolve(EasemobConverter.convert(message));
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                EMClient.getInstance().chatManager().sendMessage(message);
            } else {
                promise.reject("-1", "无法识别的messageType");
            }
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void markAllMessagesAsRead(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{CONVERSATION_ID, CHAT_TYPE}, promise)) {
            return;
        }
        String conversationId = params.getString(CONVERSATION_ID);
        EMConversation.EMConversationType chatType = EasemobConverter
                .toConversationType(params.getInt(CHAT_TYPE));
        EMClient.getInstance().chatManager().getConversation(conversationId, chatType, true).markAllMessagesAsRead();
        promise.resolve(null);
    }

    private EMMessage buildSendMessage(Context context, ReadableMap params) {
        EMMessage.Type messageType = EasemobConverter.toMessageType(params.getInt("messageType"));
        String path = "path";
        String to = params.getString("to");
        ReadableMap body = params.getMap("body");
        final EMMessage message;
        if (messageType == EMMessage.Type.IMAGE) {
            message = EMMessage
                    .createImageSendMessage(UriPathUtil
                            .getPath(context, body.getString(path)), false, to);
        } else if (messageType == EMMessage.Type.LOCATION) {
            message = EMMessage.createLocationSendMessage(
                    body.getDouble("latitude"), body.getDouble("longitude"),
                    body.getString("address"), to);
        } else if (messageType == EMMessage.Type.VIDEO) {
            message = EMMessage
                    .createVideoSendMessage(UriPathUtil.getPath(context, body.getString(path)),
                            body.getString("thumbPath"), body.getInt("duration"), to);
        } else if (messageType == EMMessage.Type.FILE) {
            message = EMMessage
                    .createFileSendMessage(UriPathUtil.getPath(context, body.getString(path)), to);
            if (body.hasKey("")) {
                ((EMFileMessageBody) message.getBody()).setSecret(body.getString(""));
            }
        } else if (messageType == EMMessage.Type.VOICE) {
            int duration;
            String uri = body.getString(path);
            if (body.hasKey("duration")) {
                duration = body.getInt("duration");
            } else {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(uri);
                duration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            }
            message = EMMessage.createVoiceSendMessage(uri, duration, to);
        } else if (messageType == EMMessage.Type.CMD) {
            message = EMMessage.createSendMessage(EMMessage.Type.CMD);
            EMCmdMessageBody cmd = new EMCmdMessageBody(body.getString("action"));
            message.setTo(to);
            message.addBody(cmd);
        } else if (messageType == EMMessage.Type.TXT) {
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.setTo(to);
            EMTextMessageBody text = new EMTextMessageBody(body.getString("text"));
            message.addBody(text);
        } else {
            message = null;
        }
        return message;
    }

    private EMMessage buildInsertMessage(ReadableMap params) {
        EMMessage.Type messageType = EasemobConverter.toMessageType(params.getInt("messageType"));
        String path = "remotePath";
        String secret = "secretKey";
        ReadableMap bodyMap = params.getMap("body");
        EMMessage.Direct direct = EasemobConverter.toDirect(params.getInt("direction"));
        final EMMessage message = direct == EMMessage.Direct.SEND ? EMMessage
                .createSendMessage(messageType) : EMMessage.createReceiveMessage(messageType);
        EMMessageBody body = null;
        if (messageType == EMMessage.Type.IMAGE) {
            EMAImageMessageBody image = new EMAImageMessageBody("", "");
            if (hasKeys(bodyMap, secret, path)) {
                image.setSecretKey(bodyMap.getString(secret));
                image.setRemotePath(bodyMap.getString(path));
                body = new EMImageMessageBody(image);
            }
        } else if (messageType == EMMessage.Type.LOCATION) {
            if (hasKeys(bodyMap, "address", "latitude", "longitude")) {
                body = new EMLocationMessageBody(bodyMap.getString("address"),
                        bodyMap.getDouble("latitude"), bodyMap.getDouble("longitude"));
            }
        } else if (messageType == EMMessage.Type.VIDEO) {
            EMAVideoMessageBody video = new EMAVideoMessageBody("", "");
            if (hasKeys(bodyMap, path, secret)) {
                video.setRemotePath(bodyMap.getString(path));
                video.setSecretKey(bodyMap.getString(secret));
                body = new EMVideoMessageBody(video);
            }
        } else if (messageType == EMMessage.Type.FILE) {
            EMNormalFileMessageBody file = new EMNormalFileMessageBody();
            if (hasKeys(bodyMap, path, secret)) {
                file.setRemoteUrl(bodyMap.getString(path));
                file.setSecret(bodyMap.getString(secret));
                body = file;
            }
        } else if (messageType == EMMessage.Type.VOICE) {
            EMAVoiceMessageBody voice = new EMAVoiceMessageBody("", 0);
            if (hasKeys(bodyMap, path, secret, "duration")) {
                voice.setDuration(bodyMap.getInt("duration"));
                voice.setRemotePath(bodyMap.getString(path));
                voice.setSecretKey(bodyMap.getString(secret));
                body = new EMVoiceMessageBody(voice);
            }
        } else if (messageType == EMMessage.Type.CMD) {
            if (hasKeys(bodyMap, "action")) {
                body = new EMCmdMessageBody(bodyMap.getString("action"));
            }
        } else if (messageType == EMMessage.Type.TXT) {
            if (hasKeys(bodyMap, "text")) {
                body = new EMTextMessageBody(bodyMap.getString("text"));
            }
        }
        if (body == null) {
            throw new RuntimeException("body 缺少字段");
        }
        message.addBody(body);
        if (params.hasKey(MESSAGE_ID)) {
            message.setMsgId(params.getString(MESSAGE_ID));
        }
        if (params.hasKey("isRead")) {
            message.setUnread(!params.getBoolean("isRead"));
        }
        if (params.hasKey("localTime")) {
            message.setLocalTime((long) params.getDouble("localTime"));
        }
        if (params.hasKey("timestamp")) {
            message.setMsgTime((long) params.getDouble("timestamp"));
        }
        if (params.hasKey("to")) {
            message.setTo(params.getString("to"));
        }
        if (params.hasKey("from")) {
            message.setFrom(params.getString("from"));
        }
        if (params.hasKey("direction")) {
            message.setDirection(direct);
        }
        return message;
    }

    private boolean hasKeys(ReadableMap map, String... keys) {
        boolean result = true;
        for (String key : keys) {
            result = result && map.hasKey(key);
        }
        return result;
    }

    private EMMessage buildMessage(ReadableMap params, boolean isInsert) throws JSONException {
        Context context = getCurrentActivity();
        if (context == null) return null;
        EMMessage message = isInsert ? buildInsertMessage(params) : buildSendMessage(context, params);
        if (message != null) {
            message.setChatType(EasemobConverter.toChatType(params.getInt(CHAT_TYPE)));
            if (params.hasKey("messageExt")) {
                if (params.getType("messageExt") == ReadableType.Map) {
                    setExt(message, params.getMap("messageExt"));
                } else {
                    throw new RuntimeException("messageExt 必须是一个对象");
                }
            }
        }
        return message;
    }

    private EMConversation.EMConversationType getCType(EMMessage.ChatType type) {
        switch (type) {
            case Chat:
                return EMConversation.EMConversationType.Chat;
            case GroupChat:
                return EMConversation.EMConversationType.GroupChat;
            case ChatRoom:
                return EMConversation.EMConversationType.ChatRoom;
            default:
                return EMConversation.EMConversationType.Chat;
        }
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
