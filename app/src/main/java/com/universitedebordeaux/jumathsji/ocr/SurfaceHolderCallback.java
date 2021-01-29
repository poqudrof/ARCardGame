package com.universitedebordeaux.jumathsji.ocr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class SurfaceHolderCallback implements SurfaceHolder.Callback {

    private final WeakReference<AppCompatActivity> activityReference;
    private final WeakReference<SurfaceView> cameraViewReference;
    private final WeakReference<CameraSource> cameraSourceReference;

    public SurfaceHolderCallback(AppCompatActivity activity, SurfaceView cameraView, CameraSource cameraSource) {
        activityReference = new WeakReference<>(activity);
        cameraViewReference = new WeakReference<>(cameraView);
        cameraSourceReference = new WeakReference<>(cameraSource);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        final int requestCameraPermissionID = 1001;

        try {
            if (checkCameraPermission()) {
                ActivityCompat.requestPermissions(activityReference.get(),
                        new String[] { Manifest.permission.CAMERA },
                        requestCameraPermissionID);
                return;
            }
            cameraSourceReference.get().start(cameraViewReference.get().getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        cameraSourceReference.get().stop();
    }

    private boolean checkCameraPermission() {
        return ActivityCompat.checkSelfPermission(activityReference.get(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
    }
}