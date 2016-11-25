package com.aotasoft.rnfileuploader;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by leonacky on 11/24/16.
 */

public class FileUploaderModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mReactContext;
    private final String CALLBACK_TYPE_SUCCESS = "success";
    private final String CALLBACK_TYPE_ERROR = "error";
    private final String CALLBACK_TYPE_CANCEL = "cancel";
    Callback mTokenCallback;

    String TAG = "FileUploaderModule";

    AsyncHttpClient client = new AsyncHttpClient();

    public FileUploaderModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "FileUploader";
    }

    private void consumeCallback(String type, WritableMap map) {
        if (mTokenCallback != null) {
            map.putString("type", type);
            map.putString("provider", "vatgia");
            if (type.equals(CALLBACK_TYPE_SUCCESS)) {
                mTokenCallback.invoke(null, map);
            } else {
                mTokenCallback.invoke(map, null);
            }
            mTokenCallback = null;
        }
    }

    RequestParams getParams(ReadableMap map, String key_upload) {
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        RequestParams params = new RequestParams();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            if(key.equals(key_upload)) continue;
            switch (map.getType(key)) {
                case Null:
                    break;
                case Boolean:
                    params.put(key, map.getBoolean(key));
                    break;
                case Number:
                    params.put(key, map.getDouble(key));
                    break;
                case String:
                    params.put(key, map.getString(key));
                    break;
                default:
                    Log.e(TAG, key + " type "+map.getType(key)+" is not supported");
            }
        }
        return params;
    }

    @ReactMethod
    public void setHeaders(ReadableMap map) {
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            try {
                String key = iterator.nextKey();
                client.addHeader(key, map.getString(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ReactMethod
    public void upload(String url, ReadableMap params, String key_upload, final Callback callback) {
        this.mTokenCallback = callback;
        final WritableMap map = Arguments.createMap();
        final WritableMap data = Arguments.createMap();
        if(params.hasKey(key_upload)) {
            String tmp_file = params.getString(key_upload);
            try {
                if(tmp_file.startsWith("content://")) {
                    Uri uri = Uri.parse(tmp_file);
                    tmp_file = FileUtils.getPath(mReactContext, uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String file_upload = tmp_file;
            File file = new File(file_upload);
            if(file.exists()) {
                try {
                    RequestParams _params = getParams(params, key_upload);
                    _params.put(key_upload, file);

                    client.post(url, _params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String result = new String(responseBody);
                                if(statusCode>=200 && statusCode<=299) {
                                    map.putInt("code", statusCode);
                                    data.putString("result", result);
                                    map.putMap("data", data);
                                    consumeCallback(CALLBACK_TYPE_ERROR, map);
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            map.putInt("code", statusCode);
                            data.putString("error", "onSuccess: File "+file_upload+" cannot upload. Please try again");
                            map.putMap("data", data);
                            consumeCallback(CALLBACK_TYPE_ERROR, map);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            try {
                                String result = new String(responseBody);
                                data.putString("result", result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            map.putInt("code", statusCode);
                            data.putString("error", "onFailure: File "+file_upload+" cannot upload. Please try again.");
                            map.putMap("data", data);
                            consumeCallback(CALLBACK_TYPE_ERROR, map);
                        }
                    });
                } catch (FileNotFoundException e) {
                    map.putInt("code", 404);
                    data.putString("error", "File "+file_upload+" is not exists");
                    map.putMap("data", data);
                    consumeCallback(CALLBACK_TYPE_ERROR, map);
                } catch (Exception e) {
                    map.putInt("code", 400);
                    data.putString("error", "File "+file_upload+" cannot upload. Please try again"+"\n"+e.toString());
                    map.putMap("data", data);
                    consumeCallback(CALLBACK_TYPE_ERROR, map);
                }

            } else {
                map.putInt("code", 404);
                data.putString("error", "File "+file_upload+" is not exists");
                map.putMap("data", data);
                consumeCallback(CALLBACK_TYPE_ERROR, map);
            }

        } else {
            map.putInt("code", 400);
            data.putString("error", "No param file_upload");
            map.putMap("data", data);
            consumeCallback(CALLBACK_TYPE_ERROR, map);
        }
    }
    
    public void onNewIntent(Intent intent) {

    }
}
