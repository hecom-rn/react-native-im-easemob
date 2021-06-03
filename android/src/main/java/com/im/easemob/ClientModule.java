package com.im.easemob;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMClient;

/**
 * Created by kevin.bai on 2018/7/12.
 */

public class ClientModule extends ReactContextBaseJavaModule {
    ClientModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Client";
    }

    @ReactMethod
    public void init(ReadableMap params, Promise promise) {
        EasemobHelper.getInstance().init(getReactApplicationContext(), EasemobConverter.buildOption(params));
        promise.resolve(null);
    }

    @ReactMethod
    public void notifyJSDidLoad(Promise promise) {
        if (getReactApplicationContext().hasActiveCatalystInstance()) {
            EasemobHelper.getInstance().notifyJSDidLoad(getReactApplicationContext());
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void login(ReadableMap params, final Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"username", "password"}, promise)) return;
        EMClient.getInstance().login(params.getString("username"), params.getString("password"), new EasemobCallback() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                promise.resolve(null);
            }

            @Override
            public void onError(int i, String s) {
                promise.reject(i + "", s);
            }
        });
    }

    @ReactMethod
    public void kickAllDevices(ReadableMap params, final Promise promise) {
        if (CheckUtil.checkParamKey(params, new String[]{"username", "password"}, promise)) return;
        EMClient.getInstance().kickAllDevices(params.getString("username"), params.getString("password"), new EasemobCallback() {
            @Override
            public void onSuccess() {
                promise.resolve(null);
            }

            @Override
            public void onError(int i, String s) {
                promise.reject(i + "", s);
            }
        });
    }

    @ReactMethod
    public void logout(final Promise promise) {
        EMClient.getInstance().logout(true, new EasemobCallback() {
            @Override
            public void onSuccess() {
                promise.resolve(null);
            }

            @Override
            public void onError(int i, String s) {
                promise.reject(i + "", s);
            }
        });
    }

    @ReactMethod
    public void isConnected(Promise promise) {
        WritableMap result = Arguments.createMap();
        result.putBoolean("result", EMClient.getInstance().isConnected());
        promise.resolve(result);
    }

    @ReactMethod
    public void isLoggedIn(Promise promise) {
        WritableMap result = Arguments.createMap();
        result.putBoolean("result", EMClient.getInstance().isLoggedInBefore());
        promise.resolve(result);
    }
}
