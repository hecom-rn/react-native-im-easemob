//
//  GroupManagerModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年. All rights reserved.
//

#import "GroupManagerDelegate.h"
#import "Client.h"
#import "NSObject+Util.h"

static NSString *eventType = @"GroupManagerDelegate";

@implementation GroupManagerDelegate

DEFINE_SINGLETON_FOR_CLASS(GroupManagerDelegate);

RCT_EXPORT_MODULE();

#pragma mark - EMGroupManagerDelegate

- (void)userDidJoinGroup:(EMGroup *)aGroup
                    user:(NSString *)aUsername {
    [Client sendEventByType:eventType subType:@"userDidJoinGroup" data:@{@"username":aUsername}];
}

- (void)userDidLeaveGroup:(EMGroup *)aGroup
                     user:(NSString *)aUsername {
    [Client sendEventByType:eventType subType:@"userDidLeaveGroup" data:@{@"username":aUsername}];
}

- (void)groupOwnerDidUpdate:(EMGroup *)aGroup
                   newOwner:(NSString *)aNewOwner
                   oldOwner:(NSString *)aOldOwner {
    NSDictionary *dic = @{@"newOwner":aNewOwner,@"oldOwner":aOldOwner};
    [Client sendEventByType:eventType subType:@"groupOwnerDidUpdate" data:dic];
}

- (void)didLeaveGroup:(EMGroup *)aGroup
               reason:(EMGroupLeaveReason)aReason {
    NSDictionary *groupDic = [aGroup objectToDictionary];
    NSDictionary *dic = @{@"group": groupDic, @"reason": @(aReason)};
    [Client sendEventByType:eventType subType:@"didLeaveGroup" data:dic];
}

@end
