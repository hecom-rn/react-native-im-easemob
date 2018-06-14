//
//  RNViewManager.h
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "RNViewController.h"
#import <React/RCTBridgeModule.h>

@interface RNViewManager : NSObject <RCTBridgeModule>

+ (void)pushToJS:(NSString *)fileName
           props:(NSDictionary *)props
            navi:(UINavigationController *)navi;

+ (RNViewController *)generateViewController:(NSString *)fileName
                                       props:(NSDictionary *)props;

+ (RNViewController *)currentViewController;

+ (void)clear;

@end
