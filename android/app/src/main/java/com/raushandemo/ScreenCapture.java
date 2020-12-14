package com.raushandemo; // replace com.your-app-name with your app’s name
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.ReactActivity;
//import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import java.util.Map;
import java.util.HashMap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
//import androidx.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

/**
 * @author Raushan Ranjan
 */

public class ScreenCapture extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 10;
    private static final String GRANTED = "OK";

    private final ReactApplicationContext reactContext;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    ScreenCapture(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        reactContext.addActivityEventListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
//        reactContext.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mScreenDensity = metrics.densityDpi;
        mScreenDensity = 1;

        mMediaRecorder = new MediaRecorder();

        mProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @Override
    public void onNewIntent(Intent intent) { }

    @Override
    public String getName() {
        return "ScreenCapture";
    }

    // add to ScreenCapture.java
    @ReactMethod
    public void testNativeMethod(String name, String location) {
        Log.d("CalendarModule", "Create event called with name: " + name
                + " and location: " + location);
    }

    @ReactMethod
    public void startScreenRecording(String status) {
        onToggleScreenShare(status);
    }

    @ReactMethod
    public void stopScreenRecording(Callback callBack) {
        Log.v(TAG, "Inside stop Screen Recording.........");
//        mMediaProjectionCallback.onStop();
//        mMediaProjection.stop();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Log.v(TAG, "Stopping Screen Recording.........");
        stopScreenSharing();
        Log.i(TAG, "path: "+Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video.mp4");
        callBack.invoke(Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video.mp4");
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mScreenDensity = metrics.densityDpi;
//
//        mMediaRecorder = new MediaRecorder();
//
//        mProjectionManager = (MediaProjectionManager) getSystemService
//                (Context.MEDIA_PROJECTION_SERVICE);
//
//        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
//        mToggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
//                        .checkSelfPermission(MainActivity.this,
//                                Manifest.permission.RECORD_AUDIO)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale
//                            (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
//                            ActivityCompat.shouldShowRequestPermissionRationale
//                                    (MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
//                        mToggleButton.setChecked(false);
//                        Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
//                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        ActivityCompat.requestPermissions(MainActivity.this,
//                                                new String[]{Manifest.permission
//                                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
//                                                REQUEST_PERMISSIONS);
//                                    }
//                                }).show();
//                    } else {
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission
//                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
//                                REQUEST_PERMISSIONS);
//                    }
//                } else {
//                    onToggleScreenShare(v);
//                }
//            }
//        });
//    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Inside onActivity result  method.........");
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(activity,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
//            mToggleButton.setChecked(false);
            return;
        }
        Log.v(TAG, "Inside onActivity result centre  method.........");
        mMediaProjectionCallback = new MediaProjectionCallback();
        Log.v(TAG, "Inside onActivity result centre 1 method.........");
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        Log.v(TAG, "Inside onActivity result centre 2 method.........");
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        Log.v(TAG, "Inside onActivity result centre 3 method.........");
        mVirtualDisplay = createVirtualDisplay();
        Log.v(TAG, "Inside onActivity result centre 4 method.........");
        mMediaRecorder.start();
        Log.v(TAG, "Inside onActivity result end  method.........");
    }

    public void onToggleScreenShare(String status) {
        initRecorder();
        shareScreen();
//        if (status == "OK") {
//
//        } else {
//            Log.v(TAG, "Stopping Recording//////////////");
////            mMediaRecorder.stop();
////            mMediaRecorder.reset();
//            Log.v(TAG, "Stopping Recording");
//            stopScreenSharing();
//        }
    }

    private void shareScreen() {
//        Intent screenCaptureIntent;
//         screenCaptureIntent = new Intent(MediaProjectionManager);
        Log.v(TAG, "Inside share screen start method.........");
        if (mMediaProjection == null) {
            Log.v(TAG, "Inside share screen if method.........");
            getCurrentActivity().startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        Log.v(TAG, "Inside share screen centre method.........");
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        Log.v(TAG, "Inside share screen end method.........");
    }

    private VirtualDisplay createVirtualDisplay() {
        Log.v(TAG, "Inside createVirtualDisplay method.........");
        Log.v(TAG, "Inside createVirtualDisplay method DISPLAY_WIDTH........."+ DISPLAY_WIDTH);
        Log.v(TAG, "Inside createVirtualDisplay method DISPLAY_HEIGHT........."+ DISPLAY_HEIGHT);
        Log.v(TAG, "Inside createVirtualDisplay method SCREEN_DENSITY........."+ mScreenDensity);
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder() {
        Log.v(TAG, "Inside init recorded method.........");
        try {
            Log.v(TAG, "Inside init recorder try method.........");
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video.mp4");
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mMediaRecorder.setVideoFrameRate(30);
            int rotation = getCurrentActivity().getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
            Log.v(TAG, "Inside init recorder try end method.........");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Recording Stopped");
            mMediaProjection = null;
            stopScreenSharing();
        }
    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        destroyMediaProjection();
//    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "MediaProjection Stopped");
        Log.i(TAG, "recorded path: "+Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video.mp4");

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_PERMISSIONS: {
//                if ((grantResults.length > 0) && (grantResults[0] +
//                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
//                    onToggleScreenShare(mToggleButton);
//                } else {
//                    mToggleButton.setChecked(false);
//                    Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
//                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                                    intent.setData(Uri.parse("package:" + getPackageName()));
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                    startActivity(intent);
//                                }
//                            }).show();
//                }
//                return;
//            }
//        }
//    }
}