package com.hecom.easemob;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hecom.easemob.EventName.CMD_MESSAGE_RECEIVED;
import static com.hecom.easemob.EventName.DISCONNECT_CHAT_SERVER;
import static com.hecom.easemob.EventName.LOGIN_ON_OTHER_DEVICE;
import static com.hecom.easemob.EventName.NO_NETWORK;
import static com.hecom.easemob.EventName.TYPE_CHAT_MANAGER;
import static com.hecom.easemob.EventName.TYPE_CLIENT;

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

    private static class Inner {
        private static EasemobHelper INSTANCE = new EasemobHelper();
    }

    static EasemobHelper getInstance() {
        return Inner.INSTANCE;
    }

    private EasemobHelper() {
        cache = new ArrayList<>();
    }

    void init(Context context, EMOptions options) {
        if (sdkInited) return;
        this.mContext = context.getApplicationContext();
        int pid = Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            Log.e(TAG, "enter the service process!");
        }
        EMClient.getInstance().init(context, initOptions(options));

        registerListener();

        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);

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
    }

    private void registerListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                if (i == EMError.USER_REMOVED) {
                    // 账号在服务端被删除
                } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    sendEvent(TYPE_CLIENT, LOGIN_ON_OTHER_DEVICE);
                } else {
                    if (NetUtils.hasNetwork(mContext)) {
                        sendEvent(TYPE_CLIENT, DISCONNECT_CHAT_SERVER);
                    } else {
                        sendEvent(TYPE_CLIENT, NO_NETWORK);
                    }
                }
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                sendEvent(TYPE_CHAT_MANAGER, CMD_MESSAGE_RECEIVED, MessageConverter.toMessageArray(list));
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    private void sendEvent(String type, String subType) {
        sendEvent(type, subType, null);
    }

    private void sendEvent(String type, String subType, Object data) {
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
}
