//
//  GroupManager.m
//  EaseMob-Example
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年. All rights reserved.
//

#import "GroupManager.h"
#import <HyphenateChat/HyphenateChat.h>
#import "NSString+Util.h"
#import "NSObject+Util.h"
#import "Constant.h"

@implementation GroupManager

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(createGroup:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    EMGroupOptions *setting = [[EMGroupOptions alloc] init];
    setting.maxUsersCount = 500;
    setting.IsInviteNeedConfirm = NO;
    setting.style = EMGroupStylePublicOpenJoin;
    NSDictionary *options = [allParams objectForKey:@"setting"];
    NSString *subject = [allParams objectForKey:@"subject"];
    NSString *description = [allParams objectForKey:@"description"];
    NSArray *invitees = [allParams objectForKey:@"invitees"];
    NSString *message = [allParams objectForKey:@"message"];
    if ([[options allKeys] count] > 0) {
        [setting updateWithDictionary:options];
    }
    EMGroup *group = [[EMClient sharedClient].groupManager createGroupWithSubject:subject description:description invitees:invitees message:message setting:setting error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(getJoinedGroups:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSArray *groupList = [[EMClient sharedClient].groupManager getJoinedGroups];
    NSMutableArray *dicArray = [NSMutableArray array];
    [groupList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [dicArray addObject:[obj objectToDictionary]];
    }];
    resolve(JSONSTRING(dicArray));
}

RCT_EXPORT_METHOD(getGroupMemberList:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSString *cursor = [allParams objectForKey:@"cursor"];
    int pageSize = [[allParams objectForKey:@"pageSize"] intValue];
    [[EMClient sharedClient].groupManager getGroupMemberListFromServerWithId:groupId cursor:cursor pageSize:pageSize completion:^(EMCursorResult *aResult, EMError *aError) {
        if (!aError) {
            resolve([aResult objectToJSONString]);
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
        }
    }];
}

RCT_EXPORT_METHOD(getGroupSpecification:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    EMGroup *group = [[EMClient sharedClient].groupManager getGroupSpecificationFromServerWithId:groupId error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(addOccupants:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSArray *members = [allParams objectForKey:@"members"];
    EMGroup *group = [[EMClient sharedClient].groupManager addOccupants:members toGroup:groupId welcomeMessage:@"" error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(removeOccupants:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSArray *members = [allParams objectForKey:@"members"];
    EMGroup *group = [[EMClient sharedClient].groupManager removeOccupants:members fromGroup:groupId error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(changeGroupSubject:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSString *subject = [allParams objectForKey:@"subject"];
    EMGroup *group = [[EMClient sharedClient].groupManager changeGroupSubject:subject forGroup:groupId error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(leaveGroup:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *groupId = [allParams objectForKey:@"groupId"];
     [[EMClient sharedClient].groupManager leaveGroup:groupId completion:^(EMError *aError) {
        if (!aError) {
            resolve(@"{}");
        } else {
            reject([NSString stringWithFormat:@"%ld", (NSInteger)aError.code], aError.errorDescription, nil);
        }
     }];
    
}

RCT_EXPORT_METHOD(destroyGroup:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *groupId = [allParams objectForKey:@"groupId"];
    EMError *error  = [[EMClient sharedClient].groupManager destroyGroup:groupId];
    if (!error) {
        resolve(@"{}");
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(updateGroupOwner:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSString *newOwner = [allParams objectForKey:@"newOwner"];
    EMError *error = nil;
    EMGroup *group = [[EMClient sharedClient].groupManager updateGroupOwner:groupId newOwner:newOwner error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(updateGroupExt:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSDictionary *ext = [allParams objectForKey:@"ext"];
    NSString *extString = JSONSTRING(ext);
    EMGroup *group = [[EMClient sharedClient].groupManager updateGroupExtWithId:groupId ext:extString error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

RCT_EXPORT_METHOD(changeGroupDescription:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *allParams = [params jsonStringToDictionary];
    EMError *error = nil;
    NSString *groupId = [allParams objectForKey:@"groupId"];
    NSString *description = [allParams objectForKey:@"description"];
    EMGroup *group = [[EMClient sharedClient].groupManager changeDescription:description forGroup:groupId error:&error];
    if (!error) {
        resolve([group objectToJSONString]);
    } else {
        reject([NSString stringWithFormat:@"%ld", (NSInteger)error.code], error.errorDescription, nil);
    }
}

@end
