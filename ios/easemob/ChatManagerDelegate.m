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

@implementation ChatManagerDelegate

DEFINE_SINGLETON_FOR_CLASS(ChatManagerDelegate);

RCT_EXPORT_MODULE();

#pragma mark - EMChatManagerDelegate

- (void)messagesDidReceive:(NSArray *)aMessages {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    [Client sendEventByType:@"ChatManagerDelegate" subType:@"messageDidReceive" data:dicArray];
}

- (void)cmdMessagesDidReceive:(NSArray *)aCmdMessages {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aCmdMessages enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    [Client sendEventByType:@"ChatManagerDelegate" subType:@"cmdMessageDidReceive" data:dicArray];
}

- (void)conversationListDidUpdate:(NSArray *)aConversationList {
    NSMutableArray *dicArray = [NSMutableArray array];
    [aConversationList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    [Client sendEventByType:@"ChatManagerDelegate" subType:@"conversationListDidUpdate" data:dicArray];
}

@end
