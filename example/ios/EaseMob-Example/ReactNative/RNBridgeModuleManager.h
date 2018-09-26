//
//  RNBridgeModuleManager.h
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年. All rights reserved.
//

#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

@interface RNBridgeModuleManager : NSObject

+ (RNBridgeModuleManager *)instance;

+ (RCTRootView *)generateView:(NSString *)moduleName props:(NSDictionary *)props;

@end
