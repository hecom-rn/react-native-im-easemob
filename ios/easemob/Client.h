//
//  Client.h
//  CRMPro
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 zhangchunshang. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "Singleton.h"

@interface Client : RCTEventEmitter <RCTBridgeModule>

DEFINE_SINGLETON_FOR_HEADER(Client);

+ (void)sendEventByType:(NSString *)type
                subType:(NSString *)subType
                   data:(NSDictionary *)data;

+ (void)sendError:(NSString *)message
             data:(NSDictionary *)data;

@end
