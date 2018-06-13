package com.hecom.easemob;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hecom.easemob.EventName.CMD_MESSAGE_RECEIVED;
import static com.hecom.easemob.EventName.TYPE_CHAT_MANAGER;

/**
 * Created by kevin.bai on 2018/6/13.
 */

public class EasemobHelper {
    private static final String TAG = EasemobHelper.class.getSimpleName();
    private boolean sdkInited = false;
    private Context mContext;
    private ReactContext mReactContext;
    private List<WritableMap> cache;

    private static class Inner {
        private static EasemobHelper INSTANCE = new EasemobHelper();
    }

    public static EasemobHelper getInstance() {
        return Inner.INSTANCE;
    }

    private EasemobHelper() {
        cache = new ArrayList<>();
    }

    public void init(Context context, EMOptions options) {
        if (sdkInited) return;
        this.mContext = context;
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

    public void notifyJSDidLoad(ReactContext reactContext) {
        this.mReactContext = reactContext;
        sendCachedEvent();
    }

    private void sendCachedEvent() {
        for (WritableMap param : cache) {
            sendEvent(param);
        }
    }

    private void registerListener() {
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
        String processName = null;
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = mContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception ignore) {
            }
        }
        return processName;
    }
}
