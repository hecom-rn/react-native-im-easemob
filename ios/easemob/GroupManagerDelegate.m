//
//  GroupManagerModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "GroupManagerDelegate.h"
#import "Client.h"

@implementation GroupManagerDelegate

DEFINE_SINGLETON_FOR_CLASS(GroupManagerDelegate);

RCT_EXPORT_MODULE();

#pragma mark - EMGroupManagerDelegate

- (void)userDidJoinGroup:(EMGroup *)aGroup
                    user:(NSString *)aUsername {
    [Client sendEventByType:@"GroupManagerDelegate" subType:@"userDidJoinGroup" data:@{@"username":aUsername}];
}

- (void)userDidLeaveGroup:(EMGroup *)aGroup
                     user:(NSString *)aUsername {
    [Client sendEventByType:@"GroupManagerDelegate" subType:@"userDidLeaveGroup" data:@{@"username":aUsername}];
}

- (void)groupOwnerDidUpdate:(EMGroup *)aGroup
                   newOwner:(NSString *)aNewOwner
                   oldOwner:(NSString *)aOldOwner {
    NSDictionary *dic = @{@"newOwner":aNewOwner,@"oldOwner":aOldOwner};
    [Client sendEventByType:@"GroupManagerDelegate" subType:@"groupOwnerDidUpdate" data:dic];
}

@end
