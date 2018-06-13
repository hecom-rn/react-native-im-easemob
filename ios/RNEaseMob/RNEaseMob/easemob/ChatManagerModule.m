//
//  ChatManagerModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "ChatManagerModule.h"
#import "Hyphenate.h"

@interface ChatManagerModule () <EMChatManagerDelegate>

@end

@implementation ChatManagerModule

DEFINE_SINGLETON_FOR_CLASS(ChatManagerModule);

RCT_EXPORT_MODULE();

#pragma mark - EMChatManagerDelegate

- (void)cmdMessagesDidReceive:(NSArray *)aCmdMessages {
    NSLog(aCmdMessages);
}

@end
