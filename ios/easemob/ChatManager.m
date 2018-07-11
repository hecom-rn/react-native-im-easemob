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
#import <MJExtension/MJExtension.h>

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
    resolve([conversation mj_JSONString]);
}

RCT_EXPORT_METHOD(getAllConversations:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ChatManager sharedChatManager] getAllConversations_local:resolve rejecter:reject];
}

- (void)getAllConversations_local:(RCTPromiseResolveBlock)resolve
                     rejecter:(RCTPromiseRejectBlock)reject {
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSArray *dictArray = [EMConversation mj_keyValuesArrayWithObjectArray:conversations];
    resolve([dictArray mj_JSONString]);
}

@end
