package com.im.easemob;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by kevin.bai on 2018/7/30.
 */
public class CheckUtil {
    public static boolean checkParamKey(ReadableMap params, String key, Promise promise) {
        if (!params.hasKey(key)) {
            promise.reject("-1", "必须包含" + key);
            return true;
        }
        return false;
    }

    public static boolean checkParamKey(ReadableMap params, String[] keys, Promise promise) {
        for (String key : keys) {
            if (checkParamKey(params, key, promise)) {
                return true;
            }
        }
        return false;
    }
}
