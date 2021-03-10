package com.im.easemob;

import android.text.TextUtils;

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
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

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
    public void createGroup(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"subject", "description", "invitees", "message", "setting"},
                promise)) {
            return;
        }
        String groupName = params.getString("subject");
        String desc = params.getString("description");
        String reason = params.getString("message");
        String[] allMembers = toMembers(params.getArray("invitees"));
        EMGroupOptions option = EasemobConverter.buildGroupOptions(params.getMap("setting"));
        try {
            EMGroup group = EMClient.getInstance().groupManager()
                    .createGroup(groupName, desc, allMembers, reason, option);
            promise.resolve(EasemobConverter.convert(group));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "创建群失败", e);
        }

    }

    @ReactMethod
    public void leaveGroup(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, "groupId", promise)) {
            return;
        }
        try {
            EMClient.getInstance().groupManager().leaveGroup(params.getString("groupId"));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "删除群失败", e);
        }
    }

    @ReactMethod
    public void destroyGroup(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, "groupId", promise)) {
            return;
        }
        try {
            EMClient.getInstance().groupManager().destroyGroup(params.getString("groupId"));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "删除群失败", e);
        }
    }

    @ReactMethod
    public void updateGroupOwner(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "newOwner"}, promise)) {
            return;
        }
        try {
            EMGroup group = EMClient.getInstance().groupManager().changeOwner(
                    params.getString("groupId"), params.getString("newOwner"));
            promise.resolve(EasemobConverter.convert(group));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "更新群主失败", e);
        }
    }

    @ReactMethod
    public void getJoinedGroups(Promise promise) {
        try {
            List<EMGroup> list = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            for (EMGroup emGroup : list) {
                if (emGroup.getMemberCount() != emGroup.getMembers().size()) {
                    getAllMembers(emGroup.getGroupId());
                }
            }
            list = EMClient.getInstance().groupManager().getAllGroups();
            promise.resolve(EasemobConverter.convertList(list));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "获取群失败", e);
        }
    }

    @ReactMethod
    public void getGroupMemberList(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "cursor", "pageSize"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        String cursor = params.getString("cursor");
        int pageSize = params.getInt("pageSize");
        EMCursorResult<String> result = null;
        try {
            result = EMClient.getInstance().groupManager()
                    .fetchGroupMembers(groupId, cursor, pageSize);
            WritableMap obj = Arguments.createMap();
            obj.putString("cursor", result.getCursor());
            obj.putArray("list", convertMembers(result.getData()));
            promise.resolve(obj);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "获取群成员失败", e);
        }
    }

    @ReactMethod
    public void getGroupSpecification(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        try {
            getAllMembers(groupId);
            promise.resolve(EasemobConverter
                    .convert(EMClient.getInstance().groupManager().getGroupFromServer(groupId)));
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "获取群详情失败", e);
        }
    }

    @ReactMethod
    public void addOccupants(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "members"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        String[] members = toMembers(params.getArray("members"));
        try {
            EMClient.getInstance().groupManager().addUsersToGroup(groupId, members);
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "添加群成员失败", e);
        }
    }

    @ReactMethod
    public void removeOccupants(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "members"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        String[] members = toMembers(params.getArray("members"));
        EMGroupManager gm = EMClient.getInstance().groupManager();
        try {
            for (String member : members) {
                gm.removeUserFromGroup(groupId, member);
            }
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "移除群成员失败", e);
        }
    }

    @ReactMethod
    public void changeGroupSubject(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "subject"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        String subject = params.getString("subject");
        EMGroupManager gm = EMClient.getInstance().groupManager();
        try {
            EMClient.getInstance().groupManager().changeGroupName(groupId, subject);
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "修改群名称失败", e);
        }
    }

    @ReactMethod
    public void updateGroupExt(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "ext"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        String subject = params.getString("ext");
        try {
            EMClient.getInstance().groupManager().updateGroupExtension(groupId, subject);
            promise.resolve(null);
        } catch (HyphenateException e) {
            e.printStackTrace();
            promise.reject("-1", "修改群名称失败", e);
        }
    }

    private List<String> getAllMembers(String groupId) throws HyphenateException {
        return getAllMembers(groupId, 1000);
    }

    private List<String> getAllMembers(String groupId, int pageSize) throws HyphenateException {
        List<String> list = new ArrayList<>();
        EMCursorResult<String> result = null;
        do {
            result = EMClient.getInstance().groupManager()
                    .fetchGroupMembers(groupId, result != null ? result.getCursor() : "", pageSize);
            list.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
        return list;
    }

    private WritableArray convertMembers(List<String> list) {
        WritableArray array = Arguments.createArray();
        for (String imId : list) {
            array.pushString(imId);
        }
        return array;
    }

    private String[] toMembers(ReadableArray invitees) {
        String[] result = new String[invitees.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = invitees.getString(i);
        }
        return result;
    }
}
