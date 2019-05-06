package com.im.easemob;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

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
        instance.chatManager().addConversationListener(listener);
        instance.groupManager().addGroupChangeListener(listener);
        instance.chatManager().addMessageListener(listener);
        instance.addClientListener(listener);
        instance.addMultiDeviceListener(listener);
        instance.contactManager().setContactListener(listener);
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
            String brand= Build.BRAND.toLowerCase();
            EMPushConfig.Builder builder = new EMPushConfig.Builder(mContext);
            if(mContext == null) return;
            String appId,appKey,appSecret;
            switch (brand){
                case "meizu":
                       appId = mContext.getString(R.string.MEIZU_PUSH_APP_ID);
                       appKey = mContext.getString(R.string.MEIZU_PUSH_APP_KEY);
                       if("".equals(appId) || "".equals(appKey)) return;
                       builder.enableMeiZuPush(appId,appKey);
                      break;
                case "xiaomi":
                     appId = mContext.getString(R.string.XIAOMI_PUSH_APP_ID);
                     appKey = mContext.getString(R.string.XIAOMI_PUSH_APP_KEY);
                    if("".equals(appId) || "".equals(appKey)) return;
                    builder.enableMiPush(appId,appKey);
                    break;
                case "oppo":
                       appKey = mContext.getString(R.string.OPPO_PUSH_APP_KEY);
                       appSecret = mContext.getString(R.string.OPPO_PUSH_APP_SECRET);
                      if("".equals(appSecret) || "".equals(appKey)) return;
                      builder.enableOppoPush(appKey,appSecret);
                     break;
                case "huawei":
                    builder.enableHWPush();
                    break;
                default:
                    return;
            }
            options.setPushConfig(builder.build());
        }
}
