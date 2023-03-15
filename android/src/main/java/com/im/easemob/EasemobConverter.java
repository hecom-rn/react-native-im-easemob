package com.im.easemob;

import android.net.Uri;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 将环信的数据结构转换为ImEasemob的数据结构 Created by kevin.bai on 2018/6/13.
 */

public class EasemobConverter {
    private static final Map<Integer, EMMessage.ChatType> toChatType = new HashMap<>(2);
    private static final Map<EMMessage.ChatType, Integer> convertChatType = new HashMap<>(2);
    private static final Map<Integer, EMConversation.EMConversationType> toConversationType = new HashMap<>(2);
    private static final Map<EMConversation.EMConversationType, Integer> convertConversationType = new HashMap<>(2);
    private static final Map<Integer, EMMessage.Type> toMessageType = new HashMap<>(7);
    private static final Map<EMMessage.Type, Integer> convertMessageType = new HashMap<>(7);
    private static final Map<Integer, EMMessage.Direct> toDirect = new HashMap<>(2);
    private static final Map<EMMessage.Direct, Integer> convertDirect = new HashMap<>(2);
    private static final Map<Integer, EMMessage.Status> toStatus = new HashMap<>(4);
    private static final Map<EMMessage.Status, Integer> convertStatus = new HashMap<>(4);

    static {
        toChatType.put(IMConstant.ChatType.GROUP, EMMessage.ChatType.GroupChat);
        toChatType.put(IMConstant.ChatType.SINGLE, EMMessage.ChatType.Chat);
        convertChatType.put(EMMessage.ChatType.GroupChat, IMConstant.ChatType.GROUP);
        convertChatType.put(EMMessage.ChatType.Chat, IMConstant.ChatType.SINGLE);
        toConversationType.put(IMConstant.ChatType.GROUP, EMConversation.EMConversationType.GroupChat);
        toConversationType.put(IMConstant.ChatType.SINGLE, EMConversation.EMConversationType.Chat);
        convertConversationType.put(EMConversation.EMConversationType.GroupChat, IMConstant.ChatType.GROUP);
        convertConversationType.put(EMConversation.EMConversationType.Chat, IMConstant.ChatType.SINGLE);
        toMessageType.put(IMConstant.MessageType.TEXT, EMMessage.Type.TXT);
        toMessageType.put(IMConstant.MessageType.IMAGE, EMMessage.Type.IMAGE);
        toMessageType.put(IMConstant.MessageType.VIDEO, EMMessage.Type.VIDEO);
        toMessageType.put(IMConstant.MessageType.LOCATION, EMMessage.Type.LOCATION);
        toMessageType.put(IMConstant.MessageType.VOICE, EMMessage.Type.VOICE);
        toMessageType.put(IMConstant.MessageType.FILE, EMMessage.Type.FILE);
        toMessageType.put(IMConstant.MessageType.CMD, EMMessage.Type.CMD);
        convertMessageType.put(EMMessage.Type.TXT, IMConstant.MessageType.TEXT);
        convertMessageType.put(EMMessage.Type.IMAGE, IMConstant.MessageType.IMAGE);
        convertMessageType.put(EMMessage.Type.VIDEO, IMConstant.MessageType.VIDEO);
        convertMessageType.put(EMMessage.Type.LOCATION, IMConstant.MessageType.LOCATION);
        convertMessageType.put(EMMessage.Type.VOICE, IMConstant.MessageType.VOICE);
        convertMessageType.put(EMMessage.Type.FILE, IMConstant.MessageType.FILE);
        convertMessageType.put(EMMessage.Type.CMD, IMConstant.MessageType.CMD);

        toDirect.put(IMConstant.EMMessageDirection.SEND, EMMessage.Direct.SEND);
        toDirect.put(IMConstant.EMMessageDirection.RECEIVE, EMMessage.Direct.RECEIVE);
        convertDirect.put(EMMessage.Direct.SEND, IMConstant.EMMessageDirection.SEND);
        convertDirect.put(EMMessage.Direct.RECEIVE, IMConstant.EMMessageDirection.RECEIVE);

        toStatus.put(IMConstant.MessageStatus.PENDING, EMMessage.Status.CREATE);
        toStatus.put(IMConstant.MessageStatus.DELIVERING, EMMessage.Status.INPROGRESS);
        toStatus.put(IMConstant.MessageStatus.SUCCEED, EMMessage.Status.SUCCESS);
        toStatus.put(IMConstant.MessageStatus.FAILED, EMMessage.Status.FAIL);
        convertStatus.put(EMMessage.Status.CREATE, IMConstant.MessageStatus.PENDING);
        convertStatus.put(EMMessage.Status.INPROGRESS, IMConstant.MessageStatus.DELIVERING);
        convertStatus.put(EMMessage.Status.SUCCESS, IMConstant.MessageStatus.SUCCEED);
        convertStatus.put(EMMessage.Status.FAIL, IMConstant.MessageStatus.FAILED);
    }

    private static WritableMap convertMessage(EMMessage message) {
        if (message == null) return null;
        WritableMap result = Arguments.createMap();
        result.putString("conversationId", message.conversationId());
        result.putString("from", message.getFrom());
        result.putString("to", message.getTo());
        result.putString("messageId", message.getMsgId());
        result.putDouble("timestamp", message.getMsgTime());
        result.putString("userName", message.getUserName());
        result.putInt("chatType", toChatType(message.getChatType()));
        result.putInt("direct", toDirect(message.direct()));
        result.putInt("status", toStatus(message.status()));
        result.putDouble("localTime", message.localTime());
        result.putBoolean("isReadAcked", message.isAcked());
        result.putBoolean("isDeliverAcked", message.isDelivered());
        result.putBoolean("isListened", message.isListened());
        result.putBoolean("isRead", !message.isUnread());
        result.putInt("direction", toDirect(message.direct()));
        result.putMap("body", convertBody(message.getBody()));
        result.putMap("ext", convertExt(message.ext()));
        return result;
    }

    /**
     * 将JS部分传递的参数转换为EMOptions
     */
    public static EMOptions buildOption(ReadableMap params) {
        EMOptions options = new EMOptions();
        if (params.hasKey("appKey")) {
            options.setAppKey(params.getString("appKey"));
        }
//        if (params.hasKey("miAppKey") && params.hasKey("miAppSecret")) {
//            options.setMipushConfig(params.getString("miAppKey"), params.getString("miAppKey"));
//        }
        if (params.hasKey("isAutoLogin")) {
            options.setAutoLogin(params.getBoolean("isAutoLogin"));
        }
        return options;
    }

    /**
     * 将{@link EMMessage.Status}转换为{@link IMConstant.MessageStatus}
     */
    private static int toStatus(EMMessage.Status status) {
        Integer integer = convertStatus.get(status);
        return integer != null ? integer : IMConstant.MessageStatus.FAILED;
    }

    /**
     * 将{@link IMConstant.MessageStatus}转换为{@link EMMessage.Status}
     */
    public static EMMessage.Status toStatus(int status) {
        EMMessage.Status result = toStatus.get(status);
        return result != null ? result : EMMessage.Status.FAIL;
    }

    /**
     * 将{@link EMMessage.ChatType}转换为{@link IMConstant.ChatType}
     */
    private static int toChatType(EMMessage.ChatType type) {
        Integer chatType = convertChatType.get(type);
        return chatType != null ? chatType : IMConstant.ChatType.SINGLE;
    }

    /**
     * 将{@link IMConstant.ChatType}转换为{@link EMMessage.ChatType}
     */
    public static EMMessage.ChatType toChatType(int type) {
        EMMessage.ChatType result = toChatType.get(type);
        return result != null ? result : EMMessage.ChatType.Chat;
    }

    /**
     * 将{@link EMConversation.EMConversationType}转换为{@link IMConstant.ChatType}
     */
    private static int toConversationType(EMConversation.EMConversationType type) {
        Integer result = convertConversationType.get(type);
        return result != null ? result : IMConstant.ChatType.SINGLE;
    }

    /**
     * 将{@link IMConstant.ChatType}转换为{@link EMConversation.EMConversationType}
     */
    public static EMConversation.EMConversationType toConversationType(int type) {
        EMConversation.EMConversationType result = toConversationType.get(type);
        return result != null ? result : EMConversation.EMConversationType.Chat;
    }

    /**
     * 将{@link EMMessage.Type}转换为{@link IMConstant.MessageType}
     */
    private static int toMessageType(EMMessage.Type type) {
        Integer result = convertMessageType.get(type);
        return result != null ? result : IMConstant.MessageType.TEXT;
    }

    /**
     * 将{@link IMConstant.MessageType}转换为{@link EMMessage.Type}
     */
    public static EMMessage.Type toMessageType(int type) {
        EMMessage.Type result = toMessageType.get(type);
        return result != null ? result : EMMessage.Type.TXT;
    }

    /**
     * 将{@link EMMessage.Direct}转换为{@link IMConstant.EMMessageDirection}
     */
    private static int toDirect(EMMessage.Direct direct) {
        Integer result = convertDirect.get(direct);
        return result != null ? result : IMConstant.EMMessageDirection.SEND;
    }

    /**
     * 将{@link IMConstant.EMMessageDirection}转换为{@link EMMessage.Direct}
     */
    public static EMMessage.Direct toDirect(int type) {
        EMMessage.Direct result = toDirect.get(type);
        return result != null ? result : EMMessage.Direct.SEND;
    }

    private static WritableMap convertConversation(EMConversation conversation) {
        WritableMap result = Arguments.createMap();
        result.putString("conversationId", conversation.conversationId());
        result.putInt("allMsgCount", conversation.getAllMsgCount());
        result.putMap("latestMessage", convertMessage(conversation.getLastMessage()));
        result.putInt("unreadMessagesCount", conversation.getUnreadMsgCount());
        result.putInt("type", toConversationType(conversation.getType()));
        return result;
    }

    private static WritableMap convertExt(Map<String, Object> ext) {
        WritableMap result = Arguments.createMap();
        for (String key : ext.keySet()) {
            Object value = ext.get(key);
            if (value instanceof Double) {
                result.putDouble(key, (Double) value);
            } else if (value instanceof Integer) {
                result.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                result.putBoolean(key, (Boolean) value);
            } else if (value instanceof Long) {
                result.putDouble(key, (Long) value);
            } else if (value instanceof Float) {
                result.putDouble(key, (Float) value);
            } else if (value instanceof String) {
                String str = (String) value;
                try {
                    if (str.startsWith("{")) {
                        result.putMap(key, convertJsonObject(new JSONObject(str)));
                    } else if (str.startsWith("[")) {
                        result.putArray(key, convertJsonArray(new JSONArray(str)));
                    } else {
                        result.putString(key, str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result.putString(key, str);
                }
            }
        }
        return result;
    }

    private static WritableArray convertJsonArray(JSONArray array) {
        WritableArray result = Arguments.createArray();
        for (int i = 0, size = array.length(); i < size; i++) {
            Object value = array.opt(i);
            if (value instanceof Double) {
                result.pushDouble((Double) value);
            } else if (value instanceof Integer) {
                result.pushInt((Integer) value);
            } else if (value instanceof Boolean) {
                result.pushBoolean((Boolean) value);
            } else if (value instanceof Long) {
                result.pushDouble((Long) value);
            } else if (value instanceof Float) {
                result.pushDouble((Float) value);
            } else if (value instanceof String) {
                result.pushString((String) value);
            } else if (value instanceof JSONObject) {
                result.pushMap(convertJsonObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.pushArray(convertJsonArray((JSONArray) value));
            }
        }
        return result;
    }

    private static WritableMap convertJsonObject(JSONObject map) {
        WritableMap result = Arguments.createMap();
        Iterator<String> iterator = map.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = map.opt(key);
            if (value instanceof Double) {
                result.putDouble(key, (Double) value);
            } else if (value instanceof Integer) {
                result.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                result.putBoolean(key, (Boolean) value);
            } else if (value instanceof Long) {
                result.putDouble(key, (Long) value);
            } else if (value instanceof Float) {
                result.putDouble(key, (Float) value);
            } else if (value instanceof String) {
                result.putString(key, (String) value);
            } else if (value instanceof JSONObject) {
                result.putMap(key, convertJsonObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.putArray(key, convertJsonArray((JSONArray) value));
            }
        }
        return result;
    }

    private static WritableMap convertBody(EMMessageBody body) {
        WritableMap result = Arguments.createMap();
        int type = IMConstant.MessageType.TEXT;
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody text = (EMTextMessageBody) body;
            result.putString("text", text.getMessage());
            type = IMConstant.MessageType.TEXT;
        } else if (body instanceof EMImageMessageBody) {
            EMImageMessageBody image = (EMImageMessageBody) body;
            result.putString("remotePath", image.getRemoteUrl());
            result.putString("thumbnailRemotePath", image.getThumbnailUrl());
            Uri local = UriPathUtil.getUri(EasemobHelper.getInstance().context(), image.getLocalUrl());
            if (local != null) {
                result.putString("localPath", local.toString());
            }
            Uri thumbLocal = UriPathUtil.getUri(EasemobHelper.getInstance().context(), image.thumbnailLocalPath());
            if (thumbLocal != null) {
                result.putString("thumbnailLocalPath", thumbLocal.toString());
            }
            WritableMap size = Arguments.createMap();
            size.putInt("height", image.getHeight());
            size.putInt("width", image.getWidth());
            result.putMap("size", size);
            type = IMConstant.MessageType.IMAGE;
        } else if (body instanceof EMVideoMessageBody) {
            EMVideoMessageBody video = (EMVideoMessageBody) body;
            result.putInt("duration", video.getDuration());
            result.putString("localPath", video.getLocalUrl());
            result.putString("remotePath", video.getRemoteUrl());
            result.putString("localThumb", video.getLocalThumb());
            result.putDouble("fileLength", video.getVideoFileLength());
            type = IMConstant.MessageType.VIDEO;
        } else if (body instanceof EMVoiceMessageBody) {
            EMVoiceMessageBody voice = (EMVoiceMessageBody) body;
            result.putString("localPath", voice.getLocalUrl());
            result.putString("remotePath", voice.getRemoteUrl());
            result.putString("secretKey", voice.getSecret());
            result.putString("displayName", voice.getFileName());
            result.putInt("duration", voice.getLength());
            type = IMConstant.MessageType.VOICE;
        } else if (body instanceof EMFileMessageBody) {
            EMFileMessageBody file = (EMFileMessageBody) body;
            result.putString("localPath", file.getLocalUrl());
            result.putString("remotePath", file.getRemoteUrl());
            type = IMConstant.MessageType.FILE;
        } else if (body instanceof EMLocationMessageBody) {
            EMLocationMessageBody location = (EMLocationMessageBody) body;
            result.putString("address", location.getAddress());
            result.putDouble("latitude", location.getLatitude());
            result.putDouble("longitude", location.getLongitude());
            type = IMConstant.MessageType.LOCATION;
        } else if (body instanceof EMCmdMessageBody) {
            EMCmdMessageBody cmd = (EMCmdMessageBody) body;
            result.putString("action", cmd.action());
            WritableMap params = Arguments.createMap();
//            for (String key : cmd.getParams().keySet()) {
//                result.putString(key, cmd.getParams().get(key));
//            }
            result.putMap("params", params);
            type = IMConstant.MessageType.CMD;
        }
        result.putInt("type", type);
        return result;
    }

    public static JSONArray toJsonArray(ReadableArray array) throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0, size = array.size(); i < size; i++) {
            ReadableType type = array.getType(i);
            switch (type) {
                case Null:
                    result.put(i, null);
                    break;
                case Boolean:
                    result.put(i, array.getBoolean(i));
                    break;
                case Number:
                    try {
                        result.put(i, array.getInt(i));
                    } catch (Exception e) {
                        result.put(i, array.getDouble(i));
                    }
                    break;
                case String:
                    result.put(i, array.getString(i));
                    break;
                case Map:
                    result.put(i, toJsonObject(array.getMap(i)));
                    break;
                case Array:
                    result.put(i, toJsonArray(array.getArray(i)));
                    break;
            }
        }
        return result;
    }

    public static JSONObject toJsonObject(ReadableMap map) throws JSONException {
        JSONObject result = new JSONObject();
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = map.getType(key);
            switch (type) {
                case Null:
                    result.put(key, null);
                    break;
                case Boolean:
                    result.put(key, map.getBoolean(key));
                    break;
                case Number:
                    result.put(key, map.getDouble(key));
                    break;
                case String:
                    result.put(key, map.getString(key));
                    break;
                case Map:
                    result.put(key, toJsonObject(map.getMap(key)));
                    break;
                case Array:
                    result.put(key, toJsonArray(map.getArray(key)));
                    break;
            }
        }
        return result;
    }

    public static EMGroupOptions buildGroupOptions(ReadableMap setting) {
        EMGroupOptions option = new EMGroupOptions();
        if (setting.hasKey("maxUsers")) {
            option.maxUsers = setting.getInt("maxUsers");
        }
        // todo 增加其他群设置
        return option;
    }

    private static WritableMap convertGroup(EMGroup group) {
        WritableMap result = Arguments.createMap();
        result.putString("groupId", group.getGroupId());
        result.putArray("blackList", convertList(group.getBlackList()));
        result.putInt("occupantsCount", group.getMemberCount() + 1);
        result.putString("owner", group.getOwner());

        WritableMap setting = Arguments.createMap();
        setting.putInt("maxUsersCount", group.getMaxUserCount());
        result.putMap("setting", setting);
        result.putArray("adminList", convertList(group.getAdminList()));
        result.putString("subject", group.getGroupName());
        result.putInt("membersCount", group.getMemberCount());
        List<String> members = group.getMembers();
        members.add(0, group.getOwner());
        result.putArray("occupants", convertList(members));
        result.putArray("muteList", convertList(group.getMuteList()));
        result.putString("announcement", group.getAnnouncement());
        result.putBoolean("isBlocked", group.isMsgBlocked());
        result.putBoolean("isPublic", group.isPublic());
        result.putArray("sharedFileList", convertList(group.getShareFileList()));
        result.putString("description", group.getDescription());
        return result;
    }

    public static WritableArray convertList(List<?> list) {
        WritableArray array = Arguments.createArray();
        if (list != null) {
            for (Object group : list) {
                if (group instanceof String) {
                    array.pushString((String) group);
                } else {
                    array.pushMap(convert(group));
                }
            }
        }
        return array;
    }

    public static WritableMap convert(Object item) {
        if (item instanceof EMGroup) {
            return convertGroup((EMGroup) item);
        } else if (item instanceof EMMessage) {
            return convertMessage((EMMessage) item);
        } else if (item instanceof EMConversation) {
            return convertConversation((EMConversation) item);
        } else if (item instanceof EMMucSharedFile) {
            return convertSharedFile((EMMucSharedFile) item);
        } else {
            throw new RuntimeException("未知的类型：" + item.toString());
        }
    }

    private static WritableMap convertSharedFile(EMMucSharedFile item) {
        WritableMap result = Arguments.createMap();
        result.putString("fileId", item.getFileId());
        result.putString("fileName", item.getFileName());
        result.putString("fileOwner", item.getFileOwner());
        result.putDouble("fileSize", item.getFileSize());
        result.putDouble("fileUpdateTime", item.getFileUpdateTime());
        return result;
    }
}
