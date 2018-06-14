//
//  Singleton.h
//  RNEaseMob
//
//  Created by Xiaosong Gao on 2018/6/7.
//  Copyright © 2018年 Hecom. All rights reserved.
//

#define DEFINE_SINGLETON_FOR_HEADER(className) \
\
+ (className *)shared##className;

#define DEFINE_SINGLETON_FOR_CLASS(className) \
\
+ (className *)shared##className { \
static className *shared##className = nil; \
static dispatch_once_t onceToken; \
dispatch_once(&onceToken, ^{ \
shared##className = [[self alloc] init]; \
}); \
return shared##className; \
}
