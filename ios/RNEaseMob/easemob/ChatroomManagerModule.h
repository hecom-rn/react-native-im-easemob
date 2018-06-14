//
//  ChatroomManagerModule.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <Hyphenate/Hyphenate.h>

@interface ChatroomManagerModule : NSObject <RCTBridgeModule, EMChatroomManagerDelegate>

DEFINE_SINGLETON_FOR_HEADER(ChatroomManagerModule);

@end
