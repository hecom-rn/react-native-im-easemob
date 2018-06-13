export default function (func, data, hasCallback = true) {
    const params = JSON.stringify(data);
    if (hasCallback) {
        return func(params).then(result => JSON.parse(result));
    } else {
        func(params);
    }
}