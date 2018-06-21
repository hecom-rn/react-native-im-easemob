//
//  NSObject+Util.m
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/13.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#import "NSObject+Util.h"
#import <objc/runtime.h>

@implementation NSObject (Util)

- (void)updateWithDictionary:(NSDictionary *)dic {
    unsigned int outCount;
    //获取类中的所有成员属性
    objc_property_t *arrPropertys = class_copyPropertyList([self class], &outCount);
    for (NSInteger i = 0; i < outCount; i ++) {
        objc_property_t property = arrPropertys[i];
        //获取属性名字符串
        //model中的属性名
        NSString *propertyName = [NSString stringWithUTF8String:property_getName(property)];
        id propertyValue = dic[propertyName];
        if (propertyValue != nil) {
            [self setValue:propertyValue forKey:propertyName];
        }
    }
    free(arrPropertys);
}

@end
