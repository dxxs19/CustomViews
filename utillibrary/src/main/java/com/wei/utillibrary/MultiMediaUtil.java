package com.wei.utillibrary;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 多媒体相关操作工具类
 * Created by WEI on 2016/6/22.
 */
public class MultiMediaUtil {
    private final static String TAG = "MultiMediaUtil";

    public static boolean isAudioForbidden(Context context) {
        boolean isForbidden = false;
        MediaRecorder mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            String path = context.getFilesDir().getPath() + "/xx.aar";
            Log.e(TAG, "path = " + path);
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    Log.e(TAG, "file.isFile() = " + file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaRecorder.setOutputFile(file.getPath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.e(TAG, "录音正常");
        } catch (Exception e) {
            Log.e(TAG, "录音被禁用");
            isForbidden = true;
        }
        if (null != mediaRecorder) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        return isForbidden;
    }

    // 判断当前相机是否被禁用
    public static boolean isCameraForbidden() {
        boolean isForbidden = false;
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            isForbidden = true;
        }

        if (camera != null) {
            camera.release();
            camera = null;
        }
        return isForbidden;
    }

}
