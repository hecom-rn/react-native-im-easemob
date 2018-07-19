//
//  Constant.h
//  CRMPro
//
//  Created by zzg on 2018/7/11.
//  Copyright © 2018年 zhangchunshang. All rights reserved.
//


#define JSONSTRING(obj) \
([[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:obj options:0 error:nil] \
encoding:NSUTF8StringEncoding])
