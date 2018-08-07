import { Platform } from 'react-native';

/**
 * 根据平台类型，区分调用方法和返回值处理。
 * @param func 待调用的方法
 * @param data 调用参数，如果为undefined，则表示没有参数
 * @param hasCallback 是否是Promise
 */
export default function (func, data, hasCallback = true) {
    const isIOS = Platform.OS === 'ios';
    if (data === undefined) {
        if (hasCallback) {
            return isIOS ? func().then(result => JSON.parse(result)) : func();
        } else {
            return func();
        }
    } else {
        const params = isIOS ? JSON.stringify(data) : data;
        if (hasCallback) {
            return isIOS ? func(params).then(result => JSON.parse(result)) : func(params);
        } else {
            return func(params);
        }
    }
}