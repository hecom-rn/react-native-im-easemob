//
//  ChatManagerModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "ChatManagerDelegate.h"
#import "Client.h"
#import "NSObject+Util.h"

@interface ChatManagerDelegate ()

@property (nonatomic, strong) NSMutableDictionary<NSString *, RCTResponseSenderBlock> *delegate;

@end

@implementation ChatManagerDelegate

DEFINE_SINGLETON_FOR_CLASS(ChatManagerDelegate);

- (instancetype)init {
    self = [super init];
    if (self) {
        self.delegate = [NSMutableDictionary dictionary];
    }
    return self;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setMessageDidReceive:(RCTResponseSenderBlock)block) {
    [ChatManagerDelegate sharedChatManagerDelegate].delegate[@"messageDidReceive"] = block;
}

RCT_EXPORT_METHOD(setCmdMessageDidReceive:(RCTResponseSenderBlock)block) {
    [ChatManagerDelegate sharedChatManagerDelegate].delegate[@"cmdMessageDidReceive"] = block;
}

RCT_EXPORT_METHOD(setConversationListDidUpdate:(RCTResponseSenderBlock)block) {
    [ChatManagerDelegate sharedChatManagerDelegate].delegate[@"conversationListDidUpdate"] = block;
}

#pragma mark - EMChatManagerDelegate

- (void)messagesDidReceive:(NSArray *)aMessages {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    if ([self.delegate objectForKey:@"messageDidReceive"]) {
        [self.delegate objectForKey:@"messageDidReceive"](@[[NSNull null], dicArray]);
    }
}

- (void)cmdMessagesDidReceive:(NSArray *)aCmdMessages {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aCmdMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    if ([self.delegate objectForKey:@"cmdMessageDidReceive"]) {
        [self.delegate objectForKey:@"cmdMessageDidReceive"](@[[NSNull null], dicArray]);
    }
}

- (void)conversationListDidUpdate:(NSArray *)aConversationList {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aConversationList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    if ([self.delegate objectForKey:@"conversationListDidUpdate"]) {
        [self.delegate objectForKey:@"conversationListDidUpdate"](@[[NSNull null], dicArray]);
    }
}

@end
