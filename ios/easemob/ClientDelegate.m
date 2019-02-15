//
//  ClientModule.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年. All rights reserved.
//

#import "ClientDelegate.h"
#import "Client.h"

NSString *eventType = @"ClientDelegate";

@implementation ClientDelegate

DEFINE_SINGLETON_FOR_CLASS(ClientDelegate);

RCT_EXPORT_MODULE();

#pragma mark - EMClientDelegate

- (void)autoLoginDidCompleteWithError:(EMError *)aError {
    NSMutableDictionary *result = [NSMutableDictionary dictionary];
    if (aError) {
        [result setObject:@(aError.code) forKey:@"code"];
        [result setObject:aError.errorDescription forKey:@"message"];
    }
    [Client sendEventByType:eventType subType:@"autoLoginDidComplete" data:result];
}

- (void)connectionStateDidChange:(EMConnectionState)aConnectionState {
    [Client sendEventByType:eventType subType:@"connectionStateDidChange" data:@(aConnectionState)];
}

- (void)userAccountDidLoginFromOtherDevice {
    [Client sendEventByType:eventType subType:@"userAccountDidLoginFromOtherDevice" data:@{}];
}

- (void)userAccountDidRemoveFromServer {
    [Client sendEventByType:eventType subType:@"userAccountDidRemoveFromServer" data:@{}];
}

@end
