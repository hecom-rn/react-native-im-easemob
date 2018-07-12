//
//  Client.m
//  CRMPro
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 zhangchunshang. All rights reserved.
//

#import "Client.h"
#import "Constant.h"
#import "NSString+Util.h"
#import "NSObject+Util.h"
#import <MJExtension/MJExtension.h>
#import <Hyphenate/Hyphenate.h>
#import "ClientDelegate.h"
#import "MultiDevicesDelegate.h"
#import "GroupManagerDelegate.h"
#import "ContactManagerDelegate.h"
#import "ChatManagerDelegate.h"

@implementation Client

DEFINE_SINGLETON_FOR_CLASS(Client);

RCT_EXPORT_MODULE();

#pragma mark - App

RCT_EXPORT_METHOD(init:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[Client sharedClient] init_local:params resolver:resolve rejecter:reject];
}

- (void)init_local:(NSString *)params
          resolver:(RCTPromiseResolveBlock)resolve
          rejecter:(RCTPromiseRejectBlock)reject {
    NSMutableDictionary *allParams = [[NSMutableDictionary alloc] initWithDictionary:[params jsonStringToDictionary]];
    EMOptions *options = [EMOptions optionsWithAppkey:[allParams objectForKey:@"appKey"]];
    if ([[allParams allKeys] count] > 1) {
        [allParams removeObjectForKey:@"appKey"];
        [options updateWithDictionary:allParams];
    }
    [[EMClient sharedClient] initializeSDKWithOptions:options];
    [[EMClient sharedClient] addDelegate:[ClientDelegate sharedClientDelegate] delegateQueue:nil];
    [[EMClient sharedClient] addMultiDevicesDelegate:[MultiDevicesDelegate sharedMultiDevicesDelegate] delegateQueue:nil];
    [[EMClient sharedClient].groupManager addDelegate:[GroupManagerDelegate sharedGroupManagerDelegate] delegateQueue:nil];
    [[EMClient sharedClient].contactManager addDelegate:[ContactManagerDelegate sharedContactManagerDelegate] delegateQueue:nil];
    [[EMClient sharedClient].chatManager addDelegate:[ChatManagerDelegate sharedChatManagerDelegate] delegateQueue:nil];
    resolve(@"{}");
}

RCT_EXPORT_METHOD(login:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[Client sharedClient] login_local:params resolver:resolve rejecter:reject];
}

- (void)login_local:(NSString *)params
           resolver:(RCTPromiseResolveBlock)resolve
           rejecter:(RCTPromiseRejectBlock)reject {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *username = [allParams objectForKey:@"username"];
    NSString *password = [allParams objectForKey:@"password"];
    BOOL isAutoLoginEnabled = [EMClient sharedClient].isAutoLogin;
    if (!isAutoLoginEnabled) {
        [[EMClient sharedClient] loginWithUsername:username password:password completion:^(NSString *aUsername, EMError *aError) {
            if (!aError) {
                resolve(@"{}");
            } else {
                reject([NSString stringWithFormat:@"%ld",(long)aError.code],aError.errorDescription,nil);
            }
        }];
    }
}

RCT_EXPORT_METHOD(logout:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[Client sharedClient] logout_local:resolve rejecter:reject];
}

- (void)logout_local:(RCTPromiseResolveBlock)resolve
            rejecter:(RCTPromiseRejectBlock)reject {
    EMError *error = [[EMClient sharedClient] logout:YES];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

#pragma mark - RCTEventEmitter

- (NSArray<NSString *> *)supportedEvents {
    return @[EASEMOB_EVENT_NAME];
}

- (void)startObserving {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendEaseMobEvent:)
                                                 name:EASEMOB_EVENT_NAME
                                               object:nil];
}

- (void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)sendEaseMobEvent:(NSNotification *)notification {
    [self sendEventWithName:EASEMOB_EVENT_NAME
                       body:notification.object];
}

+ (void)sendEventByType:(NSString *)type
                subType:(NSString *)subType
                   data:(id)data {
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    [body setObject:type forKey:@"type"];
    [body setObject:subType forKey:@"subType"];
    [body setObject:data forKey:@"data"];
    [[NSNotificationCenter defaultCenter] postNotificationName:EASEMOB_EVENT_NAME object:body];
}

+ (void)sendError:(NSString *)message {
    NSMutableDictionary *body = [NSMutableDictionary dictionary];
    [body setObject:message forKey:@"errorMessage"];
    [[NSNotificationCenter defaultCenter] postNotificationName:EASEMOB_EVENT_NAME object:body];
}

@end
