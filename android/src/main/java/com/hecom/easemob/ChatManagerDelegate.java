package com.hecom.easemob;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by kevin.bai on 2018/7/19.
 */

public class ChatManagerDelegate extends ReactContextBaseJavaModule {
    public ChatManagerDelegate(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ChatManagerDelegate";
    }

    @ReactMethod
    public void setMessageDidReceive(Callback callback) {
        EasemobHelper.getInstance().putDelegate(EasemobHelper.KEY_MESSAGE_DID_RECEIVE, callback);
    }

    @ReactMethod
    public void setCmdMessageDidReceive(Callback callback) {
        EasemobHelper.getInstance().putDelegate(EasemobHelper.KEY_CMD_MESSAGE_DID_RECEIVE, callback);
    }

    @ReactMethod
    public void setConversationListDidUpdate(Callback callback) {
        EasemobHelper.getInstance().putDelegate(EasemobHelper.KEY_CONVERSATION_DID_UPDATE, callback);
    }
}
