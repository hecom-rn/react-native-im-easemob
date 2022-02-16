package com.im.easemob;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.push.EMPushConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 环信接口辅助类
 * Created by kevin.bai on 2018/6/13.
 */

class EasemobHelper {
    private static final String TAG = EasemobHelper.class.getSimpleName();
    private boolean sdkInited = false;
    private Context mContext;
    private ReactContext mReactContext;
    private List<WritableMap> cache;
    private EaseMobHookDelegate mHookDelegate;
    private static Bundle metadata;
    private static final String XIAOMI_PUSH_APP_ID = "IM_EASEMOB_XIAOMI_PUSH_APP_ID";
    private static final String XIAOMI_PUSH_APP_KEY = "IM_EASEMOB_XIAOMI_PUSH_APP_KEY";
    private static final String OPPO_PUSH_APP_KEY = "IM_EASEMOB_OPPO_PUSH_APP_KEY";
    private static final String OPPO_PUSH_APP_SECRET = "IM_EASEMOB_OPPO_PUSH_APP_SECRET";
    private static final String MEIZU_PUSH_APP_ID = "IM_EASEMOB_MEIZU_PUSH_APP_ID";
    private static final String MEIZU_PUSH_APP_KEY = "IM_EASEMOB_MEIZU_PUSH_APP_KEY";
    public Context context() {
        return mContext;
    }

    private static class Inner {
        private static EasemobHelper INSTANCE = new EasemobHelper();
    }

    static EasemobHelper getInstance() {
        return Inner.INSTANCE;
    }

    private EasemobHelper() {
        cache = new ArrayList<>();
    }

    void setHookDelegate(EaseMobHookDelegate delegate) {
        this.mHookDelegate = delegate;
    }

    void init(Context context, EMOptions options) {
        if (sdkInited) return;
        this.mContext = context.getApplicationContext();
        int pid = Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            Log.e(TAG, "enter the service process!");
        }

        EMOptions emOptions = initOptions(options);
        configOfflinePushPar(emOptions);
        EMClient.getInstance().init(context, emOptions);

        registerListener();

        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);

        if (mHookDelegate != null) {
            mHookDelegate.onInit(this.mContext, options);
        }

        sdkInited = true;
    }

    void notifyJSDidLoad(ReactContext reactContext) {
        this.mReactContext = reactContext;
        sendCachedEvent();
    }

    private void sendCachedEvent() {
        for (WritableMap param : cache) {
            sendEvent(param);
        }
        cache.clear();
    }

    private void registerListener() {
        EasemobListener listener = new EasemobListener(mContext);
        EMClient instance = EMClient.getInstance();
        instance.addConnectionListener(listener);
        instance.addMultiDeviceListener(listener);
        instance.groupManager().addGroupChangeListener(listener);
        instance.contactManager().setContactListener(listener);
        instance.chatManager().addConversationListener(listener);
        instance.chatManager().addMessageListener(listener);
    }

    /**
     * 发送事件，不携带数据时使用
     *
     * @param type    分类
     * @param subType 子分类
     */
    public void sendEvent(@IMConstant.Type String type, @IMConstant.SubType String subType) {
        sendEvent(type, subType, null);
    }

    /**
     * 发送事件，携带参数
     *
     * @param type    分类
     * @param subType 子分类
     * @param data    携带参数
     */
    public void sendEvent(@IMConstant.Type String type, @IMConstant.SubType String subType, Object data) {
        WritableMap map = Arguments.createMap();
        map.putString("type", type);
        map.putString("subType", subType);
        if (data instanceof WritableMap) {
            map.putMap("data", (WritableMap) data);
        } else if (data instanceof WritableArray) {
            map.putArray("data", (WritableArray) data);
        } else if (data instanceof String) {
            map.putString("data", (String) data);
        } else if (data instanceof Integer) {
            map.putInt("data", (Integer) data);
        } else if (data instanceof Double) {
            map.putDouble("data", (Double) data);
        } else if (data instanceof Boolean) {
            map.putBoolean("data", (Boolean) data);
        } else {
            map.putNull("data");
        }
        if (mReactContext != null) {
            sendEvent(map);
        } else {
            cache.add(map);
        }
    }

    /**
     * 实际向JS发送Event的方法，除非有特殊的数据结构，否则不应该直接使用此方法
     *
     * @param params 发送到JS的数据
     * @see EasemobHelper#sendEvent(String, String)
     * @see EasemobHelper#sendEvent(String, String, Object)
     */
    private void sendEvent(WritableMap params) {
        if (mReactContext != null) {
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("RNEaseMob", params);
        }
    }

    private EMOptions initOptions(EMOptions options) {
        if (options == null) {
            options = new EMOptions();
            // set if accept the invitation automatically
            options.setAcceptInvitationAlways(false);
            // set if you need read ack
            options.setRequireAck(true);
            // set if you need delivery ack
            options.setRequireDeliveryAck(false);
        }
        return options;
    }

    private String getAppName(int pid) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        if (am != null) {
            List l = am.getRunningAppProcesses();
            for (Object aL : l) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (aL);
                try {
                    if (info.pid == pid) {
                        return info.processName;
                    }
                } catch (Exception ignore) {
                }
            }
        }
        return null;
    }


      private void configOfflinePushPar(EMOptions options){
          EMPushConfig.Builder builder = new EMPushConfig.Builder(mContext);
          builder.enableVivoPush()
                  .enableMeiZuPush(getPushPar(MEIZU_PUSH_APP_ID), getPushPar(MEIZU_PUSH_APP_KEY))
                  .enableMiPush(getPushPar(XIAOMI_PUSH_APP_ID), getPushPar(XIAOMI_PUSH_APP_KEY))
                  .enableOppoPush(getPushPar(OPPO_PUSH_APP_KEY), getPushPar(OPPO_PUSH_APP_SECRET))
                  .enableHWPush();
          options.setPushConfig(builder.build());
         }

          private String getPushPar(String metaKey){
                 if (metadata == null) {
                     try {
                         ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                         metadata = applicationInfo.metaData;
                     } catch (PackageManager.NameNotFoundException e) {
                         e.printStackTrace();
                         metadata = new Bundle();
                     }
                 }
                 String metaData = metadata.getString(metaKey);
                 if(metaData == null){
                     metaData = "";
                 }
                 return  metaData;
          }
}
