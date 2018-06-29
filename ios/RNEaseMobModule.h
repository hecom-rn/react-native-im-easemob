//
//  RNEaseMobModule.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/12.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "Singleton.h"

@interface RNEaseMobModule : RCTEventEmitter <RCTBridgeModule>

DEFINE_SINGLETON_FOR_HEADER(RNEaseMobModule);

+ (void)sendEventByType:(NSString *)type
                subType:(NSString *)subType
                   data:(NSDictionary *)data;

+ (void)sendError:(NSString *)message
             data:(NSDictionary *)data;

@end
