package com.im.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPushManager;
import com.hyphenate.chat.EMSilentModeParam;
import com.hyphenate.chat.EMSilentModeResult;
import com.hyphenate.chat.EMSilentModeTime;

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
        EMClient.getInstance().pushManager()
                .getSilentModeForAll(new EMValueCallBack<EMSilentModeResult>() {
                    @Override
                    public void onSuccess(EMSilentModeResult emSilentModeResult) {
                        WritableMap result = Arguments.createMap();
                        EMSilentModeTime startTime = emSilentModeResult.getSilentModeStartTime();
                        WritableMap start = Arguments.createMap();
                        start.putInt("hours", startTime.getHour());
                        start.putInt("minutes", startTime.getMinute());

                        EMSilentModeTime endTime = emSilentModeResult.getSilentModeEndTime();
                        WritableMap end = Arguments.createMap();
                        end.putInt("hours", endTime.getHour());
                        end.putInt("minutes", endTime.getMinute());

                        result.putMap("silentModeStartTime", start);
                        result.putMap("silentModeEndTime", end);
                        promise.resolve(result);
                    }

                    @Override
                    public void onError(int error, String errMsg) {
                        promise.reject(String.valueOf(error), errMsg);
                    }
                });
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
    public void setIgnoreGroupPush(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"groupId", "ignore", "groupType"}, promise)) {
            return;
        }
        String groupId = params.getString("groupId");
        int groupType = params.getInt("groupType");
        boolean ignore = params.getBoolean("ignore");
        if (ignore) {
            EMSilentModeParam param = new EMSilentModeParam(EMSilentModeParam.EMSilentModeParamType.REMIND_TYPE);
            param.setRemindType(EMPushManager.EMPushRemindType.NONE);
            EMClient.getInstance().pushManager().setSilentModeForConversation(groupId, EasemobConverter.toConversationType(groupType), param, new EMValueCallBack<EMSilentModeResult>() {
                @Override
                public void onSuccess(EMSilentModeResult emSilentModeResult) {
                    promise.resolve(Arguments.createMap());
                }

                @Override
                public void onError(int error, String errMsg) {
                    promise.reject(String.valueOf(error), errMsg);
                }
            });
        } else {
            EMClient.getInstance().pushManager().clearRemindTypeForConversation(groupId, EasemobConverter.toConversationType(groupType), new EMCallBack() {
                @Override
                public void onSuccess() {
                    promise.resolve(Arguments.createMap());
                }

                @Override
                public void onError(int error, String errMsg) {
                    promise.reject(String.valueOf(error), errMsg);
                }
            });
        }
    }

    @ReactMethod
    public void getIgnoreGroupPush(Promise promise) {
        WritableArray result = Arguments.createArray();
        List<String> list = EMClient.getInstance().pushManager().getNoPushGroups();
        for (String id : list) {
            result.pushString(id);
        }
        // 该 API 未使用，暂不更新实现
        promise.resolve(Arguments.createMap());

    }

    @ReactMethod
    public void setNoDisturbStatus(ReadableMap params, Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"startH", "startM", "endH", "endM"},
                promise)) {
            return;
        }
        try {
            int startH = params.getInt("startH");
            int startM = params.getInt("startM");
            int endH = params.getInt("endH");
            int endM = params.getInt("endM");
            EMSilentModeParam smParam =
                    new EMSilentModeParam(EMSilentModeParam.EMSilentModeParamType.SILENT_MODE_INTERVAL)
                            .setSilentModeInterval(new EMSilentModeTime(startH, startM),
                                    new EMSilentModeTime(endH, endM));
            EMClient.getInstance().pushManager()
                    .setSilentModeForAll(smParam, new EMValueCallBack<EMSilentModeResult>() {
                        @Override
                        public void onSuccess(EMSilentModeResult value) {
                            promise.resolve(null);
                        }

                        @Override
                        public void onError(int error, String errorMsg) {
                            promise.reject(String.valueOf(error), errorMsg);
                        }
                    });
        } catch (Exception e) {
            promise.reject(e);
        }
    }

}
