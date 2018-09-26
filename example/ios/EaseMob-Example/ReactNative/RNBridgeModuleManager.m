//
//  RNBridgeModuleManager.m
//  EaseMob-Example
//
//  Created by Xiaosong Gao on 2018/6/14.
//  Copyright © 2018年. All rights reserved.
//

#import "RNBridgeModuleManager.h"
#import <React/RCTBundleURLProvider.h>
#import "RNViewManager.h"

@interface RNBridgeModuleManager () <RCTBridgeDelegate>

@property (nonatomic, strong) RCTBridge *bridge;

@end

@implementation RNBridgeModuleManager

+ (RNBridgeModuleManager *)instance {
    static RNBridgeModuleManager *g_manager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        g_manager = [[RNBridgeModuleManager alloc] init];
    });
    return g_manager;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:nil];
    }
    return self;
}

#pragma mark - Interface

+ (RCTRootView *)generateView:(NSString *)moduleName props:(NSDictionary *)props {
    RNBridgeModuleManager *instance = [RNBridgeModuleManager instance];
    NSDictionary *theNewProps = @{};
    return [[RCTRootView alloc] initWithBridge:instance.bridge moduleName:moduleName initialProperties:theNewProps];
}

#pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    NSURL *location = nil;
#if TARGET_IPHONE_SIMULATOR
    location = [NSURL URLWithString:@"http://localhost:8081/index.bundle?platform=ios&dev=true&minify=false"];
//#elif (TARGET_OS_IPHONE && DEBUG)
//    location = [NSURL URLWithString:@"http://192.168.xx.xx:8081/index.ios.bundle?platform=ios&dev=true&minify=false"];
#else
    location = [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
    return location;
}

@end
