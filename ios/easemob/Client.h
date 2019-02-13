//
//  Client.h
//  CRMPro
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 zhangchunshang. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface Client : RCTEventEmitter <RCTBridgeModule>

+ (void)sendEventByType:(NSString *)type
                subType:(NSString *)subType
                   data:(id)data;

+ (void)sendError:(NSString *)message;

@end
