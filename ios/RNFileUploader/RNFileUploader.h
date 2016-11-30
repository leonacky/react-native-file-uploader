//
//  RNFileUploader.h
//  RNFileUploader
//
//  Created by Tuan Dinh on 11/29/16.
//  Copyright © 2016 Aotasoft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import <Foundation/Foundation.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import <UIKit/UIKit.h>

#import "RCTEventDispatcher.h"
#import "RCTLog.h"
#include <dispatch/dispatch.h>


@interface RNFileUploader : NSObject <RCTBridgeModule, NSURLConnectionDelegate, NSURLConnectionDataDelegate>

@property NSMutableData *responseData;
@property NSInteger responseStatusCode;

@property NSURLConnection *connection;
@property NSMutableURLRequest *request;
@property NSMutableData *requestBody;

@property NSString *formBoundaryString;
@property NSData *formBoundaryData;

@property dispatch_group_t fgroup;
@property NSMutableArray *files;

@end
