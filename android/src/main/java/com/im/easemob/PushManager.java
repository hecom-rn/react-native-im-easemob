package com.im.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.bai on 2020/9/3.
 */
public class PushManager extends ReactContextBaseJavaModule {
    public PushManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "APNs";
    }

    @ReactMethod
    public void getPushOptionsFromServer(Promise promise) {
        try {
            EMPushConfigs config = EMClient.getInstance().pushManager().getPushConfigsFromServer();
            WritableMap result = Arguments.createMap();
            result.putString("displayName", config.getDisplayNickname());
            result.putInt("noDisturbingStartH", config.getNoDisturbStartHour());
            result.putInt("noDisturbingEndH", config.getNoDisturbEndHour());
            result.putInt("noDisturbStatus", config.isNoDisturbOn() ? 1 : 0);
            promise.resolve(result);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void setApnsNickname(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, "name", promise)) {
            return;
        }
        try {
            boolean success = EMClient.getInstance().pushManager()
                    .updatePushNickname(params.getString("name"));
            promise.resolve(success);
        } catch (Exception e) {
            promise.reject(e);
        }

    }

    @ReactMethod
    public void ignoreGroupsPush(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupIds", "ignore"}, promise)) {
            return;
        }
        try {
            ReadableArray arr = params.getArray("groupIds");
            List<String> groupIds = new ArrayList<>(arr.size());
            for (int i = 0; i < arr.size(); i++) {
                groupIds.add(arr.getString(i));
            }
            EMClient.getInstance().pushManager()
                    .updatePushServiceForGroup(groupIds, params.getBoolean("ignore"));
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getIgnoredGroupIds(Promise promise) {
        WritableArray result = Arguments.createArray();
        List<String> list = EMClient.getInstance().pushManager().getNoPushGroups();
        for (String id : list) {
            result.pushString(id);
        }
        promise.resolve(result);
    }

    @ReactMethod
    public void setNoDisturbStatus(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"status", "startH", "endH"}, promise)) {
            return;
        }
        try {
            boolean status = params.getBoolean("status");
            if (status) {
                int startH = params.getInt("startH");
                int endH = params.getInt("endH");
                EMClient.getInstance().pushManager()
                        .disableOfflinePush(startH, endH);
            } else {
                EMClient.getInstance().pushManager().enableOfflinePush();
            }

        } catch (Exception e) {
            promise.reject(e);
        }
    }

}
