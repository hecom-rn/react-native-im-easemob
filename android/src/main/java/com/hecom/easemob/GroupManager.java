package com.hecom.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by kevin.bai on 2018/7/12.
 */

public class GroupManager extends ReactContextBaseJavaModule {
    GroupManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "GroupManager";
    }

    @ReactMethod
    public void createGroup(ReadableMap param, Promise promise) {
        String groupName = "";
        String desc = "";
        String reason = "";
        String[] allMembers = null;
        EMGroupOptions option = new EMGroupOptions();
        if (param.hasKey("subject")) {
            groupName = param.getString("subject");
        }
        if (param.hasKey("description")) {
            desc = param.getString("description");
        }
        if (param.hasKey("message")) {
            reason = param.getString("message");
        }
        if (param.hasKey("invitees")) {
            allMembers = convertAllMembers(param.getArray("invitees"));
        }
        if (param.hasKey("setting")) {
            convertGroupOptions(param.getMap("setting"), option);
        }
        try {
            EMGroup group = EMClient.getInstance().groupManager()
                    .createGroup(groupName, desc, allMembers, reason, option);
            promise.resolve(convertGroup(group));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "创建群失败");
        }

    }

    private void convertGroupOptions(ReadableMap setting, EMGroupOptions option) {
        if (setting.hasKey("maxUsers")) {
            option.maxUsers = setting.getInt("maxUsers");
        }
        // todo 增加其他群设置
    }

    private String[] convertAllMembers(ReadableArray invitees) {
        String[] result = new String[invitees.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = invitees.getString(i);
        }
        return result;
    }

    private WritableMap convertGroup(EMGroup group) {
        WritableMap result = Arguments.createMap();
        result.putString("id", group.getGroupId());
        return result;
    }
}
