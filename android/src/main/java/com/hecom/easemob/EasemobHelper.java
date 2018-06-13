package com.hecom.easemob;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hecom.easemob.EventName.CMD_MESSAGE_RECEIVED;
import static com.hecom.easemob.EventName.CONNECTION_CONNECTED;
import static com.hecom.easemob.EventName.CONNECTION_DISCONNECTED;

/**
 * Created by kevin.bai on 2018/6/13.
 */

public class EasemobHelper {
    private static final String TAG = EasemobHelper.class.getSimpleName();
    private boolean sdkInited = false;
    private Context mContext;
    private ReactContext mReactContext;
    private Map<String, List<Object>> cache;

    private static class Inner {
        private static EasemobHelper INSTANCE = new EasemobHelper();
    }

    public static EasemobHelper getInstance() {
        return Inner.INSTANCE;
    }

    private EasemobHelper() {
        cache = new HashMap<>();
    }

    public void init(Context context, EMOptions options) {
        if (sdkInited) return;
        this.mContext = context;
        int pid = Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            Log.e(TAG, "enter the service process!");
        }
        if (options == null) {
            EMClient.getInstance().init(context, initOptions());
        } else {
            EMClient.getInstance().init(context, options);
        }

        registerListener();

        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);

        sdkInited = true;
    }

    public void notifyJSDidLoad(ReactContext reactContext) {
        this.mReactContext = reactContext;
        sendCachedEvent();
    }

    private void sendCachedEvent() {
        for (String eventName : cache.keySet()) {
            List<Object> array = cache.get(eventName);
            for (Object param : array) {
                sendEvent(eventName, param);
            }
        }
    }

    private void registerListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                sendEvent(CONNECTION_CONNECTED);
            }

            @Override
            public void onDisconnected(int i) {
                sendEvent(CONNECTION_DISCONNECTED, i);
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                sendEvent(CMD_MESSAGE_RECEIVED, MessageConverter.toMessageArray(list));
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

    private void sendEvent(String eventName, Object params) {
        if (mReactContext != null) {
            mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        } else {
            Log.d(TAG, "ReactContext is null, cached message");
            addCacheMessage(eventName, params);
        }
    }

    private void addCacheMessage(String eventName, Object params) {
        List<Object> array = cache.get(eventName);
        if (array == null) {
            array = new ArrayList<>();
            cache.put(eventName, array);
        }
        array.add(params);
    }

    private void sendEvent(String eventName) {
        sendEvent(eventName, null);
    }

    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);
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
