//
//  ChatManagerModule.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <HyphenateChat/HyphenateChat.h>
#import "Singleton.h"

@interface ChatManagerDelegate : NSObject <RCTBridgeModule, EMChatManagerDelegate>

DEFINE_SINGLETON_FOR_HEADER(ChatManagerDelegate);

@end
