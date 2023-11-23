//
//  APNs.m
//  Easemob-Example
//
//  Created by Xiaosong Gao on 2019/2/13.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "APNs.h"
#import "Constant.h"
#import "NSString+Util.h"
#import "NSObject+Util.h"
#import <HyphenateChat/HyphenateChat.h>
#import <HyphenateChat/EMPushOptions.h>
#import <HyphenateChat/IEMPushManager.h>
#import <HyphenateChat/EMConversation.h>

@implementation APNs

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getPushOptionsFromServer:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[EMClient sharedClient].pushManager getSilentModeForAllWithCompletion:^(EMSilentModeResult *aResult, EMError *aError) {
        if (!aError) {
            NSMutableDictionary *optionsDic = [aResult objectToDictionary].mutableCopy;
            NSString *optionsStr = [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:optionsDic options:0 error:nil] encoding:NSUTF8StringEncoding];
            resolve(optionsStr);
        } else{
            reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(setApnsNickname:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *name = [allParams objectForKey:@"name"];
    EMError *error = [[EMClient sharedClient].pushManager updatePushDisplayName:name];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(setApnsDisplayStyle:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    BOOL showDetail = [[allParams objectForKey:@"showDetail"] boolValue];
    EMPushOptions *options = [[EMClient sharedClient].pushManager pushOptions];
    EMPushDisplayStyle displayStyle = showDetail ? EMPushDisplayStyleMessageSummary : EMPushDisplayStyleSimpleBanner;
    EMError *error = [[EMClient sharedClient].pushManager updatePushDisplayStyle: displayStyle];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(setIgnoreGroupPush:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSInteger groupType = [allParams objectForKey:@"groupType"];
    BOOL ignore = [[allParams objectForKey:@"ignore"] boolValue];
    if (ignore) {
        EMSilentModeParam *param = [[EMSilentModeParam alloc]initWithParamType:EMSilentModeParamTypeRemindType];
        param.remindType = EMPushRemindTypeNone;
        [[EMClient sharedClient].pushManager setSilentModeForConversation:groupId conversationType:groupType params:param completion:^(EMSilentModeResult * _Nullable aResult, EMError * _Nullable aError) {
            if (!aError) {
                resolve(@"{}");
            } else {
                reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
            }
        }];
    } else {
        [[EMClient sharedClient].pushManager clearRemindTypeForConversation:groupId conversationType:groupType completion:^(EMSilentModeResult * _Nullable aResult, EMError * _Nullable aError) {
            if (!aError) {
                resolve(@"{}");
            } else {
                reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
            }
        }];
    }
}

RCT_EXPORT_METHOD(getIgnoreGroupPush:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMConversation *conversation = [[EMClient sharedClient].chatManager getConversation:[allParams objectForKey:@"groupId"] type:[allParams objectForKey:@"groupType"] createIfNotExist:NO];
    [[EMClient sharedClient].pushManager getSilentModeForConversations:@[conversation] completion:^(NSDictionary<NSString *,EMSilentModeResult *> * _Nullable aResult, EMError * _Nullable aError) {
        if (!aError) {
            // todo
            resolve(@"{}");
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(setNoDisturbStatus:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMSilentModeParam *param;
    // 当开始时间和结束时间的hours和minutes都为0时候表示关闭免打扰时间段
    NSInteger startH = [[allParams objectForKey:@"startH"] integerValue];
    NSInteger startM = [[allParams objectForKey:@"startM"] integerValue];
    NSInteger endH = [[allParams objectForKey:@"endH"] integerValue];
    NSInteger endM = [[allParams objectForKey:@"endM"] integerValue];
    param = [[EMSilentModeParam alloc]initWithParamType:EMSilentModeParamTypeInterval];
    param.silentModeStartTime = [[EMSilentModeTime alloc]initWithHours:startH minutes:startM];
    param.silentModeEndTime = [[EMSilentModeTime alloc]initWithHours:endH minutes:endM];
    [[EMClient sharedClient].pushManager setSilentModeForAll:param completion:^(EMSilentModeResult *aResult, EMError *aError) {
        if (!aError) {
            resolve(@"{}");
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
        }
    }];
}

@end
