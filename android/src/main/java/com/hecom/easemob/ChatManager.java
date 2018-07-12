package com.hecom.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.Map;

/**
 * Created by kevin.bai on 2018/7/12.
 */

public class ChatManager extends ReactContextBaseJavaModule {
    private static final String CONVERSATION_TYPE_CHAT = "0";
    private static final String CONVERSATION_TYPE_GROUP = "1";

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
            type = convertType(param.getString("type"));
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
        WritableMap result = Arguments.createMap();
        for (String key : map.keySet()) {
            result.putMap(key, convertConversation(map.get(key)));
        }
        promise.resolve(result);
    }

    private WritableMap convertConversation(EMConversation conversation) {
        WritableMap result = Arguments.createMap();
        result.putString("id", conversation.conversationId());
        return result;
    }

    private EMConversation.EMConversationType convertType(String type) {
        if (CONVERSATION_TYPE_CHAT.equals(type)) {
            return EMConversation.EMConversationType.Chat;
        } else if (CONVERSATION_TYPE_GROUP.equals(type)) {
            return EMConversation.EMConversationType.GroupChat;
        } else {
            return EMConversation.EMConversationType.Chat;
        }
    }
}
