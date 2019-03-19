//
//  ChatManager.m
//  EaseMob-Example
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年. All rights reserved.
//

#import "ChatManager.h"
#import <Hyphenate/Hyphenate.h>
#import "NSString+Util.h"
#import "NSObject+Util.h"
#import "Constant.h"

@implementation ChatManager

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getConversation:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"type"] intValue];
    BOOL aIfCreate = [[allParams objectForKey:@"ifCreate"] boolValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:aIfCreate];
    resolve([conversation objectToJSONString]);
}

RCT_EXPORT_METHOD(getAllConversations:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSMutableArray *dicArray = [NSMutableArray array];
    [conversations enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    resolve(JSONSTRING(dicArray));
}

RCT_EXPORT_METHOD(deleteConversation:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    BOOL isDeleteMessages = [[allParams objectForKey:@"ifClearAllMessage"] boolValue];
    [[EMClient sharedClient].chatManager deleteConversation:conversationId isDeleteMessages:isDeleteMessages completion:^(NSString *aConversationId, EMError *error) {
        if (!error) {
            resolve(@"{}");
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(sendMessage:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMChatType chatType = [[allParams objectForKey:@"chatType"] intValue];
    NSString *from = [[EMClient sharedClient] currentUsername];
    NSString *to = [allParams objectForKey:@"to"];
    EMMessageBodyType messageType = [[allParams objectForKey:@"messageType"] intValue];
    NSDictionary *messageExt = [allParams objectForKey:@"messageExt"];
    NSDictionary *bodyDic = [allParams objectForKey:@"body"];
    long long timestamp = [[allParams objectForKey:@"timestamp"] longLongValue];
    long long localTime = [[allParams objectForKey:@"localTime"] longLongValue];
    EMMessageBody *body;
    switch (messageType) {
        case EMMessageBodyTypeText: {
            body = [[EMTextMessageBody alloc] initWithText:bodyDic[@"text"] ];
        }
            break;
        case EMMessageBodyTypeImage: {
            NSString *path = bodyDic[@"path"];
            NSData *imageData = [NSData dataWithContentsOfFile:path];
            body = [[EMImageMessageBody alloc] initWithData:imageData displayName:@"image"];
        }
            break;
        case EMMessageBodyTypeLocation: {
            double latitude = [bodyDic[@"latitude"] doubleValue];
            double longitude = [bodyDic[@"longitude"] doubleValue];
            NSString *address = bodyDic[@"address"];
            body = [[EMLocationMessageBody alloc] initWithLatitude:latitude longitude:longitude address:address];
        }
            break;
        case EMMessageBodyTypeVoice: {
            NSString *path = bodyDic[@"path"];
            int duration = [bodyDic[@"duration"] intValue];
            EMVoiceMessageBody *voiceBody = [[EMVoiceMessageBody alloc] initWithLocalPath:path displayName:@"audio"];
            voiceBody.duration = duration;
            body = voiceBody;
        }
            break;
        case EMMessageBodyTypeVideo: {
            NSString *path = bodyDic[@"path"];
            body = [[EMVideoMessageBody alloc] initWithLocalPath:path displayName:@"video"];
        }
            break;
        case EMMessageBodyTypeFile: {
            NSString *path = bodyDic[@"path"];
            body = [[EMFileMessageBody alloc] initWithLocalPath:path displayName:@"file"];
        }
            break;
        case EMMessageBodyTypeCmd: {
            NSString *action = bodyDic[@"action"];
            body = [[EMCmdMessageBody alloc] initWithAction:action];
        }
            break;
        default:
            break;
    }
    // 生成Message
    EMMessage *message = [[EMMessage alloc] initWithConversationID:conversationId from:from to:to body:body ext:messageExt];
    message.chatType = chatType;
    if (timestamp > 0) {
        // 插入消息
        message.timestamp = timestamp;
        message.localTime = localTime;
        message.status = EMMessageStatusSucceed;
        EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
        EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:NO];
        [conversation insertMessage:message error:nil];
        resolve([message objectToJSONString]);
    } else {
        // 发送消息
        [[EMClient sharedClient].chatManager sendMessage:message progress:nil completion:^(EMMessage *message, EMError *error) {
            // 不管有没有错误都会返回message，根据message中的status判断消息的发送状态
            resolve([message objectToJSONString]);
        }];
    }
}

RCT_EXPORT_METHOD(loadMessages:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:YES];
    NSString *fromId = [allParams objectForKey:@"fromId"];
    int count = [[allParams objectForKey:@"count"] intValue];
    EMMessageSearchDirection searchDirection = [[allParams objectForKey:@"searchDirection"] intValue];
    [conversation loadMessagesStartFromId:fromId count:count searchDirection:searchDirection completion:^(NSArray *aMessages, EMError *error) {
        NSMutableArray *dicArray = [NSMutableArray array];
        [aMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [dicArray addObject:[obj objectToDictionary]];
        }];
        if (!error) {
            resolve([dicArray objectToJSONString]);
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(recallMessage:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:NO];
    NSString *messageId = [allParams objectForKey:@"messageId"];
    EMMessage *message = [conversation loadMessageWithId:messageId error:nil];
    [[EMClient sharedClient].chatManager recallMessage:message completion:^(EMMessage *aMessage, EMError *aError) {
        if (!aError) {
            resolve([aMessage objectToJSONString]);
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(deleteMessage:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:NO];
    NSString *messageId = [allParams objectForKey:@"messageId"];
    EMError *error = nil;
    [conversation deleteMessageWithId:messageId error:&error];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(markAllMessagesAsRead:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:NO];
    EMError *error = nil;
    [conversation markAllMessagesAsRead:&error];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

@end
