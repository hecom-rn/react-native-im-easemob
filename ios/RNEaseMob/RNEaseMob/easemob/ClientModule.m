//
//  ClientModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "ClientModule.h"
#import "RNEaseMobModule.h"

NSString * const type = @"client";
NSString * const subtype_login = @"login";

@implementation ClientModule

DEFINE_SINGLETON_FOR_CLASS(ClientModule);

RCT_EXPORT_MODULE();

#pragma mark - Function

RCT_EXPORT_METHOD(login:(NSString *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [[ClientModule sharedClientModule] login_local:params resolver:resolve rejecter:reject];
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
                [[RNEaseMobModule sharedRNEaseMobModule] sendError:aError.description data:nil];
            } else {
                [[RNEaseMobModule sharedRNEaseMobModule] sendEventByType:type subType:subtype_login data:@{username: aUsername}];
            }
        }];
    }
}

#pragma mark - EMClientDelegate

@end
