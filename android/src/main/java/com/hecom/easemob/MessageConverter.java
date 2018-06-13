package com.hecom.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;

import java.util.List;

/**
 * 将环信的Message结构转换为RN的数据结构
 * Created by kevin.bai on 2018/6/13.
 */

class MessageConverter {
    static WritableArray toMessageArray(List<EMMessage> list) {
        WritableArray array = Arguments.createArray();
        for (EMMessage message : list) {
            array.pushMap(toMessage(message));
        }
        return array;
    }

    static WritableMap toMessage(EMMessage message) {
        WritableMap map = Arguments.createMap();
        map.putString("id", message.getMsgId());
        map.putString("to", message.getTo());
        map.putString("from", message.getFrom());
        map.putDouble("msgTime", message.getMsgTime());
        map.putString("userName", message.getUserName());
        map.putInt("chatType", message.getChatType().ordinal());
        map.putString("direct", message.direct().toString());
        map.putString("status", message.status().toString());
        map.putDouble("localTime", message.localTime());
        map.putBoolean("isAcked", message.isAcked());
        map.putBoolean("isDelivered", message.isDelivered());
        map.putBoolean("isListened", message.isListened());
        map.putBoolean("isUnread", message.isUnread());
        map.putInt("type", message.getType().ordinal());

        EMMessageBody emaBody = message.getBody();

        if (emaBody instanceof EMCmdMessageBody) {
            EMCmdMessageBody body = (EMCmdMessageBody) emaBody;
            WritableMap mapBody = Arguments.createMap();
            mapBody.putString("action", body.action());
            WritableMap params = Arguments.createMap();
            for (String key : body.getParams().keySet()) {
                params.putString(key, body.getParams().get(key));
            }
            mapBody.putMap("params", params);
            map.putMap("body", mapBody);
        }

        WritableMap ext = Arguments.createMap();
        for (String key : message.ext().keySet()) {
            Object value = message.ext().get(key);
            if (value instanceof Double) {
                ext.putDouble("key", (Double) value);
            } else if (value instanceof Integer) {
                ext.putInt("key", (Integer) value);
            } else if (value instanceof Boolean) {
                ext.putBoolean("key", (Boolean) value);
            } else if (value instanceof Long) {
                ext.putDouble("key", (Long) value);
            } else if (value instanceof Float) {
                ext.putDouble("key", (Float) value);
            } else if (value instanceof String) {
                ext.putString("key", (String) value);
            }
        }

        map.putMap("ext", ext);

        return map;
    }

    /**
     * 将JS部分传递的参数转换为EMOptions
     */
    static EMOptions toOption(ReadableMap params) {
        EMOptions options = new EMOptions();
        if (params.hasKey("appKey")) {
            options.setAppKey(params.getString("appKey"));
        }
        return options;
    }
}
