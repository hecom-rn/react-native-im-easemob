//
//  ChatManager.h
//  EaseMob-Example
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import "Singleton.h"

@interface ChatManager : NSObject <RCTBridgeModule>

DEFINE_SINGLETON_FOR_HEADER(ChatManager);

@end
