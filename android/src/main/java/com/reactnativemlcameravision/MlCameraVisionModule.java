package com.reactnativemlcameravision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ReactModule(name = MlCameraVisionModule.NAME)
public class MlCameraVisionModule extends ReactContextBaseJavaModule {
    public static final String NAME = "MlCameraVision";
    public static ReactApplicationContext reactContext;
    public MlCameraVisionModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }
    private Context getAppContext() {
      return this.reactContext.getApplicationContext();
    }
    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void detectObject(String path, Promise promise) throws IOException {
      Log.d(NAME, "000000000");
      ObjectDetectorOptions options =
        new ObjectDetectorOptions.Builder()
          .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
          .enableMultipleObjects()
          .enableClassification()  // Optional
          .build();
      Log.d(NAME, "11111111");
      ObjectDetector objectDetector = ObjectDetection.getClient(options);
      Log.d(NAME, "2222222");
      InputImage image;
      Log.d(NAME, "3333333");
        Bitmap imageBitMap=this.getImageBitmap(path);
        image = InputImage.fromBitmap(imageBitMap,  0);
        objectDetector.process(image)
          .addOnSuccessListener(
            new OnSuccessListener<List<DetectedObject>>() {
              @Override
              public void onSuccess(List<DetectedObject> detectedObjects) {
                // Task completed successfully
                // ...
                Log.d(NAME, "onSuccess");
                WritableMap params = Arguments.createMap();
                for (DetectedObject detectedObject : detectedObjects) {
                  WritableMap dectedted = Arguments.createMap();
                  Rect boundingBox = detectedObject.getBoundingBox();
                  Integer trackingId = detectedObject.getTrackingId();
                  List<DetectedObject.Label> ob= detectedObject.getLabels();
                  if(trackingId !=null){
                    dectedted.putInt("trackingId",trackingId);
                  }
                  dectedted.putString("boundingBox", boundingBox.flattenToString());
                  for (DetectedObject.Label label : detectedObject.getLabels()) {
                    String text = label.getText();
                    Log.d(NAME, "text label"+text);
                    int index = label.getIndex();
                    float confidence = label.getConfidence();
                    WritableMap map = Arguments.createMap();
                    map.putString("text", text);
                    map.putDouble("confidence", confidence);
                    map.putInt("index", index);
                    dectedted.putMap("listText",map);
                  }
                  params.putMap("dectedted",dectedted);
                }
                promise.resolve(params);
              }
            })
          .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                // ...
                Log.d(NAME, "onFailure");
              }
            });

    }

  private Bitmap getImageBitmap(String url) {
    Bitmap bm = null;
    try {
      URL aURL = new URL(url);
      URLConnection conn = aURL.openConnection();
      conn.connect();
      InputStream is = conn.getInputStream();
      BufferedInputStream bis = new BufferedInputStream(is);
      bm = BitmapFactory.decodeStream(bis);
      bis.close();
      is.close();
    } catch (IOException e) {
      Log.e(NAME, "Error getting bitmap", e);
    }
    return bm;
  }
  private WritableArray getObjectDetectedArray(List<DetectedObject>
                                                     objectDetecteds) throws JSONException {
    WritableArray array = new WritableNativeArray();
    for (DetectedObject objectDetected : objectDetecteds) {
      WritableMap productMap = ReactNativeJson.convertJsonToMap(new
        JSONObject((Map) objectDetected));
      array.pushMap(productMap);
    }
    return array;
  }
}
