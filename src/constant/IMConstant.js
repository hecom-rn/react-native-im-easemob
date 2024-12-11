// 聊天类型
export const ChatType = {
    single: 0, // 单聊
    group: 1, // 群聊
};

// 消息类型
export const MessageType = {
    text: 1, // 文本消息
    image: 2, // 图片消息
    video: 3, // 视频消息
    location: 4, // 位置消息
    voice: 5, // 语音消息
    file: 6, // 文件消息
    cmd: 7, // CMD控制消息
};

// 消息状态
export const MessageStatus = {
    pending: 0, // 发送未开始
    delivering: 1, // 正在发送
    succeed: 2, // 发送成功
    failed: 3, // 发送失败
};

// 消息列表加载方向
export const MessageSearchDirection = {
    up: 0, // 向上加载
    down: 1, // 向下加载
};

// 消息来源方向
export const MessageDirection = {
    send: 0, // 我发出去的消息
    receive: 1, // 我收到的消息
};

// 网络连接状态
export const ConnectionState = {
    Connected: 0, // 已连接
    Disconnected: 1, // 未连接
};