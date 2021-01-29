package com.universitedebordeaux.jumathsji.ocr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.universitedebordeaux.jumathsji.R;

import java.io.IOException;

// Recognition and preview activity.
// Build from an example of CameraX.
public class TextRecognitionActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    private CameraSource camerasource;
    private final int RequestCameraPermissionID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        cameraView = findViewById(R.id.camera_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TextRecognitionActivity.class.getSimpleName(), "Detector dependencies are not yet available.");
        } else {
            camerasource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(TextRecognitionActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        camerasource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    camerasource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(@NonNull @org.jetbrains.annotations.NotNull Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();

                    if (items.size() != 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        Log.i("Result", stringBuilder.toString());
                    }
                }
            });
        }
    }
}

// Recognition and preview activity.
// Build from an example of CameraX.
/*
public class TextRecognitionActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private TextureView mCameraView;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        FirebaseApp.initializeApp(this);
        mCameraView = findViewById(R.id.camera_view);

        Log.i(TAG, "Ocr Capture activity started");

        if (hasCameraPermissions()) {
            mCameraView.post(this::startCamera);
        }
        else {
            requestCameraPermission();
        }
        mCameraView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());
    }

    private boolean hasCameraPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Camera permission granted : initialize the camera");
            mCameraView.post(this::startCamera);
        } else {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startCamera() {
        // Setup Preview.
        Preview preview = new Preview.Builder().setTargetResolution(new Size(640, 480)).build();

        preview.setOnPreviewOutputUpdateListener(output -> {
            ViewGroup parent = (ViewGroup) mCameraView.getParent();

            parent.removeView(mCameraView);
            parent.addView(mCameraView, 0);

            mCameraView.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        // Setup OCR.
        ImageAnalysis analysis = new ImageAnalysis(new ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build());

        analysis.setAnalyzer(mExecutor, new TextAnalyzer());

        // Here we go !
        CameraX.bindToLifecircle(this, preview, analysis);
    }

    private float cameraRotationToDegrees(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                throw new IllegalArgumentException("Rotation must be 0, 90, 180, or 270.");
        }
    }

    private void updateTransform() {
        Matrix mat = new Matrix();

        // Compute the center of the view finder
        float centerX = mCameraView.getWidth() / 2f;
        float centerY = mCameraView.getHeight() / 2f;

        // Correct preview output to account for display rotation
        mat.postRotate(-cameraRotationToDegrees(mCameraView.getDisplay().getRotation()), centerX, centerY);

        // Finally, apply transformations to our TextureView
        mCameraView.setTransform(mat);
    }

    // Send the recognition result to the activity who call this.
    public void sendResult(List<CardWithLines> cardsWithLines) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("CARD_LIST", CardWithLines.toParcelableList(cardsWithLines));

        data.putExtras(bundle);
        setResult(CommonStatusCodes.SUCCESS, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ocr_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("test", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        final int id = item.getItemId();

        if (id == R.id.action_goBack) {
            Log.d("Case goBack", "Go to Main Activity case chosen");
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            Log.d("Case Option", "Option case chosen");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Override the behaviour of the back button in the activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
*/