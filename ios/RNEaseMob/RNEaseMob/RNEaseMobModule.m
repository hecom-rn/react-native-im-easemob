//
//  RNEaseMobModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/12.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "RNEaseMobModule.h"
#import "ChatManagerModule.h"
#import "ClientModule.h"
#import "MultiDevicesModule.h"
#import "GroupManagerModule.h"
#import "ContactManagerModule.h"
#import "ChatroomManagerModule.h"

@interface RNEaseMobModule ()

@end

@implementation RNEaseMobModule

DEFINE_SINGLETON_FOR_CLASS(RNEaseMobModule);

RCT_EXPORT_MODULE();

#pragma mark - App

RCT_EXPORT_METHOD(initWithAppKey:(NSString *)appKey
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[RNEaseMobModule sharedRNEaseMobModule] initWithAppKey_local:appKey params:nil resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(initWithAppKey:(NSString *)appKey
                  params:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[RNEaseMobModule sharedRNEaseMobModule] initWithAppKey_local:appKey params:params resolver:resolve rejecter:reject];
}

- (void)initWithAppKey_local:(NSString *)appKey
                      params:(NSString *)params
                    resolver:(RCTPromiseResolveBlock)resolve
                    rejecter:(RCTPromiseRejectBlock)reject {
    EMOptions *options = [EMOptions optionsWithAppkey:appKey];
    if (params != nil) {
        [options updateWithDictionary:[params jsonStringToDictionary]];
    }
    [[EMClient sharedClient] initializeSDKWithOptions:options];
    [[EMClient sharedClient] addDelegate:[ClientModule sharedClientModule] delegateQueue:nil];
    [[EMClient sharedClient] addMultiDevicesDelegate:[MultiDevicesModule sharedMultiDevicesModule] delegateQueue:nil];
    [[EMClient sharedClient].groupManager addDelegate:[GroupManagerModule sharedGroupManagerModule] delegateQueue:nil];
    [[EMClient sharedClient].contactManager addDelegate:[ContactManagerModule sharedContactManagerModule] delegateQueue:nil];
    [[EMClient sharedClient].roomManager addDelegate:[ChatroomManagerModule sharedChatroomManagerModule] delegateQueue:nil];
    [[EMClient sharedClient].chatManager addDelegate:[ChatManagerModule sharedChatManagerModule] delegateQueue:nil];
}

@end
