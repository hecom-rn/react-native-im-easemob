package com.hecom.easemob;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.hyphenate.chat.EMClient;

/**
 * 环信JS接口类
 * Created by kevin.bai on 2018/6/12.
 */

class EasemobModule extends ReactContextBaseJavaModule {
    EasemobModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNEaseMobModule";
    }

    @ReactMethod
    public void init(ReadableMap params, Promise promise) {
        EasemobHelper.getInstance().init(getReactApplicationContext(), MessageConverter.toOption(params));
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
    public void login(String userName, String password, final Promise promise) {
        EMClient.getInstance().login(userName, password, new EasemobCallback() {
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
}
