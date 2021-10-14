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
#import <Hyphenate/Hyphenate.h>
#import "ClientDelegate.h"
#import "MultiDevicesDelegate.h"
#import "GroupManagerDelegate.h"
#import "ContactManagerDelegate.h"
#import "ChatManagerDelegate.h"

@implementation Client

#pragma mark - React Native

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(init:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSMutableDictionary *allParams = [[params jsonStringToDictionary] mutableCopy];
    if ([allParams objectForKey:@"isAutoLogin"]) {
        [allParams removeObjectForKey:@"isAutoLogin"];
    }
    NSString *appKey = [allParams objectForKey:@"appKey"];
    EMOptions *options = [EMOptions optionsWithAppkey:appKey];
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

RCT_EXPORT_METHOD(registerUser:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *username = [allParams objectForKey:@"username"];
    NSString *password = [allParams objectForKey:@"password"];
    EMError *error = [[EMClient sharedClient] registerWithUsername:username password:password];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld",(long)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(login:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *username = [allParams objectForKey:@"username"];
    NSString *password = [allParams objectForKey:@"password"];
    BOOL autoLogin = [[allParams objectForKey:@"autoLogin"] boolValue];
    BOOL isAutoLoginEnabled = [EMClient sharedClient].isAutoLogin;
    if (!isAutoLoginEnabled) {
        EMError *error = [[EMClient sharedClient] loginWithUsername:username password:password];
        if (!error) {
            if (autoLogin) {
                [[EMClient sharedClient].options setIsAutoLogin:YES];
            }
            resolve(@"{}");
        } else {
            reject([NSString stringWithFormat:@"%ld",(long)error.code], error.errorDescription, nil);
        }
    }
}

RCT_EXPORT_METHOD(logout:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    EMError *error = [[EMClient sharedClient] logout:YES];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(kickAllDevices:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *username = [allParams objectForKey:@"username"];
    NSString *password = [allParams objectForKey:@"password"];
    EMError *error = [[EMClient sharedClient] kickAllDevicesWithUsername:username password:password];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld",(long)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(isConnected:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    BOOL result = [EMClient sharedClient].isConnected;
    resolve([@{@"result": @(result)} objectToJSONString]);
}

RCT_EXPORT_METHOD(isLoggedIn:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    BOOL result = [EMClient sharedClient].isLoggedIn;
    resolve([@{@"result": @(result)} objectToJSONString]);
}

RCT_EXPORT_METHOD(fetchToken:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *username = [allParams objectForKey:@"username"];
    NSString *password = [allParams objectForKey:@"password"];
    [[EMClient sharedClient] fetchTokenWithUsername:username password:password completion:^(NSString *aToken, EMError *aError) {
        if (aError || aToken.length == 0) {
            reject([NSString stringWithFormat:@"%ld",(long)aError.code], aError.errorDescription, nil);
        } else {
            resolve([@{@"result": aToken} objectToJSONString]);
        }
    }];
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
