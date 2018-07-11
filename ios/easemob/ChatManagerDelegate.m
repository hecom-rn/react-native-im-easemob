//
//  ChatManagerModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "ChatManagerDelegate.h"

static NSString * const type = @"chat_manager";

@implementation ChatManagerDelegate

DEFINE_SINGLETON_FOR_CLASS(ChatManagerDelegate);

RCT_EXPORT_MODULE();

#pragma mark - EMChatManagerDelegate

- (void)cmdMessagesDidReceive:(NSArray *)aCmdMessages {
    
}

@end
