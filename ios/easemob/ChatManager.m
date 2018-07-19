//
//  ChatManager.m
//  EaseMob-Example
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "ChatManager.h"
#import <Hyphenate/Hyphenate.h>
#import "NSString+Util.h"
#import "NSObject+Util.h"
#import "Constant.h"

@implementation ChatManager

DEFINE_SINGLETON_FOR_CLASS(ChatManager);

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getConversation:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ChatManager sharedChatManager] getConversation_local:params resolver:resolve rejecter:reject];
}

- (void)getConversation_local:(NSString *)params
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject {
    NSMutableDictionary *allParams = [[NSMutableDictionary alloc] initWithDictionary:[params jsonStringToDictionary]];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"type"] intValue];
    BOOL aIfCreate = [[allParams objectForKey:@"ifCreate"] boolValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:aIfCreate];
    resolve([conversation objectToJSONString]);
}

RCT_EXPORT_METHOD(getAllConversations:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ChatManager sharedChatManager] getAllConversations_local:resolve rejecter:reject];
}

- (void)getAllConversations_local:(RCTPromiseResolveBlock)resolve
                     rejecter:(RCTPromiseRejectBlock)reject {
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSMutableArray *dicArray = [NSMutableArray array];
    [conversations enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    resolve(JSONSTRING(dicArray));
}

RCT_EXPORT_METHOD(sendMessage:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ChatManager sharedChatManager] sendMessage_local:params resolver:resolve rejecter:reject];
}

- (void)sendMessage_local:(NSString *)params
                     resolver:(RCTPromiseResolveBlock)resolve
                     rejecter:(RCTPromiseRejectBlock)reject {
    NSMutableDictionary *allParams = [[NSMutableDictionary alloc] initWithDictionary:[params jsonStringToDictionary]];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMChatType chatType = [[allParams objectForKey:@"chatType"] intValue];
    NSString *from = [[EMClient sharedClient] currentUsername];
    NSString *to = [allParams objectForKey:@"to"];
    EMMessageBodyType messageType = [[allParams objectForKey:@"messageType"] intValue];
    NSDictionary *messageExt = [allParams objectForKey:@"messageExt"];
    NSDictionary *bodyDic = [allParams objectForKey:@"body"];
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
        default:
            break;
    }
    //生成Message
    EMMessage *message = [[EMMessage alloc] initWithConversationID:conversationId from:from to:to body:body ext:messageExt];
    message.chatType = chatType;
    //发送消息
    [[EMClient sharedClient].chatManager sendMessage:message progress:nil completion:^(EMMessage *message, EMError *error) {
        if(!error){
            resolve([message objectToJSONString]);
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(loadMessages:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ChatManager sharedChatManager] loadMessages_local:params resolver:resolve rejecter:reject];
}

- (void)loadMessages_local:(NSString *)params
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject {
    NSMutableDictionary *allParams = [[NSMutableDictionary alloc] initWithDictionary:[params jsonStringToDictionary]];
    NSString *conversationId = [allParams objectForKey:@"conversationId"];
    EMConversationType type = [[allParams objectForKey:@"chatType"] intValue];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:conversationId type:type createIfNotExist:NO];
    NSString *fromId = [allParams objectForKey:@"fromId"];
    int count = [[allParams objectForKey:@"count"] intValue];
    EMMessageSearchDirection searchDirection = [[allParams objectForKey:@"searchDirection"] intValue];
    [conversation loadMessagesStartFromId:fromId count:count searchDirection:searchDirection completion:^(NSArray *aMessages, EMError *error) {
        NSMutableArray *dicArray = [NSMutableArray array];
        [aMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [dicArray addObject:[obj objectToDictionary]];
        }];
        if(!error){
            resolve([dicArray objectToJSONString]);
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
        }
    }];
}




@end
