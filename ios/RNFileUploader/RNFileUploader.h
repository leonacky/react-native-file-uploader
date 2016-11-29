//
//  RNFileUploader.h
//  RNFileUploader
//
//  Created by Tuan Dinh on 11/29/16.
//  Copyright © 2016 Aotasoft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface RNFileUploader : NSObject<RCTBridgeModule>
    NSMutableData *responseData;
    NSInteger responseStatusCode;

    NSURLConnection *connection;
    NSMutableURLRequest *request;
    NSMutableData *requestBody;

    NSString *formBoundaryString;
    NSData *formBoundaryData;

    dispatch_group_t fgroup;
@end
