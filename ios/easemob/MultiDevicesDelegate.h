//
//  MultiDevicesModule.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <Hyphenate/Hyphenate.h>
#import "Singleton.h"

@interface MultiDevicesDelegate : NSObject <RCTBridgeModule, EMMultiDevicesDelegate>

DEFINE_SINGLETON_FOR_HEADER(MultiDevicesDelegate);

@end
