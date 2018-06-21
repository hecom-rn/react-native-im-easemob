//
//  ChatManagerModule.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "RCTBridgeModule.h"
#import "Hyphenate.h"
#import "Singleton.h"

@interface ChatManagerModule : NSObject <RCTBridgeModule, EMChatManagerDelegate>

DEFINE_SINGLETON_FOR_HEADER(ChatManagerModule);

@end
