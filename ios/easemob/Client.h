//
//  Client.h
//  CRMPro
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 zhangchunshang. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import "Singleton.h"

@interface Client : NSObject <RCTBridgeModule>

DEFINE_SINGLETON_FOR_HEADER(Client);

@end
