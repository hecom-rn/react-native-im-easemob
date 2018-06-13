//
//  RNEaseMobModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/12.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "RNEaseMobModule.h"
#import "ChatManagerModule.h"
#import "Hyphenate.h"

//<EMClientDelegate, EMMultiDevicesDelegate, EMGroupManagerDelegate, EMContactManagerDelegate , EMChatroomManagerDelegate>

@interface RNEaseMobModule ()

@end

@implementation RNEaseMobModule

DEFINE_SINGLETON_FOR_CLASS(RNEaseMobModule);

RCT_EXPORT_MODULE();

#pragma mark - App

RCT_EXPORT_METHOD(init:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[RNEaseMobModule sharedRNEaseMobModule] init_local:params resolver:resolve rejecter:reject];
}

- (void)init_local:(NSString *)params
          resolver:(RCTPromiseResolveBlock)resolve
          rejecter:(RCTPromiseRejectBlock)reject {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *appKey = [allParams objectForKey:@"appKey"];
    NSString *apnsCertName = [allParams objectForKey:@"apnsCertName"];
    EMOptions *options = [EMOptions optionsWithAppkey:appKey];
    options.apnsCertName = apnsCertName;
    options.isAutoLogin = YES;
    [[EMClient sharedClient] initializeSDKWithOptions:options];
//    [[EMClient sharedClient] addDelegate:self delegateQueue:nil];
//    [[EMClient sharedClient] addMultiDevicesDelegate:self delegateQueue:nil];
//    [[EMClient sharedClient].groupManager addDelegate:self delegateQueue:nil];
//    [[EMClient sharedClient].contactManager addDelegate:self delegateQueue:nil];
//    [[EMClient sharedClient].roomManager addDelegate:self delegateQueue:nil];
    [[EMClient sharedClient].chatManager addDelegate:[ChatManagerModule sharedChatManagerModule] delegateQueue:nil];
}

@end
