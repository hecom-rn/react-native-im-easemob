import { Platform } from 'react-native';

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