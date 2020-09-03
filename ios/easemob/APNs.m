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
#import <Hyphenate/Hyphenate.h>

@implementation APNs

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getPushOptionsFromServer:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    EMError *error = nil;
    EMPushOptions *options = [[EMClient sharedClient] getPushOptionsFromServerWithError:&error];
    if (!error) {
        NSMutableDictionary *optionsDic = [options objectToDictionary].mutableCopy;
        if (options.noDisturbStatus == EMPushNoDisturbStatusClose) {
            optionsDic[@"noDisturbStatus"] = @(0);
        } else {
            optionsDic[@"noDisturbStatus"] = @(1);
        }
        NSString *optionsStr = [[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:optionsDic options:0 error:nil] encoding:NSUTF8StringEncoding];
        resolve(optionsStr);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(setApnsNickname:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *name = [allParams objectForKey:@"name"];
    EMError *error = [[EMClient sharedClient] setApnsNickname:name];
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
    EMPushOptions *options = [[EMClient sharedClient] pushOptions];
    options.displayStyle = showDetail ? EMPushDisplayStyleMessageSummary : EMPushDisplayStyleSimpleBanner;
    EMError *error = [[EMClient sharedClient] updatePushOptionsToServer];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(ignoreGroupsPush:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSArray *groupIds = [allParams objectForKey:@"groupIds"];
    BOOL ignore = [[allParams objectForKey:@"ignore"] boolValue];
    EMError *error = [[EMClient sharedClient].groupManager ignoreGroupsPush:groupIds ignore:ignore];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(getIgnoredGroupIds:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    EMError *error = nil;
    NSArray *ignoredGroupIds = [[EMClient sharedClient].groupManager getGroupsWithoutPushNotification:&error];
    if (!error) {
        resolve(JSONSTRING(ignoredGroupIds));
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(setNoDisturbStatus:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    BOOL status = [[allParams objectForKey:@"status"] boolValue];
    EMPushOptions *options = [[EMClient sharedClient] pushOptions];
    if (status) {
        NSInteger startH = [[allParams objectForKey:@"startH"] integerValue];
        NSInteger endH = [[allParams objectForKey:@"endH"] integerValue];
        options.noDisturbingStartH = startH;
        options.noDisturbingEndH = endH;
        BOOL isWholeDay = startH == 0 && endH == 24;
        options.noDisturbStatus = isWholeDay ? EMPushNoDisturbStatusDay : EMPushNoDisturbStatusCustom;
    } else {
        options.noDisturbStatus = EMPushNoDisturbStatusClose;
    }
    EMError *error = [[EMClient sharedClient] updatePushOptionsToServer];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

@end
