package com.universitedebordeaux.joue_maths_gie.ui.camera;

/*
public class CameraSourcePreview extends ViewGroup implements SurfaceHolder.Callback
{
    private static final String TAG = "CameraSourcePreview";

    private final Context mContext;

    private final SurfaceView mSurfaceView;
    private CameraSource mCameraSource;

    private boolean mStartRequested;
    private boolean mSurfaceAvailable;

    public CameraSourcePreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mContext = context;
        mStartRequested = false;
        mSurfaceAvailable = false;

        mSurfaceView = new SurfaceView(context);
        mSurfaceView.getHolder().addCallback(this);
        addView(mSurfaceView);
    }

    // TRICKY : build the layout architecture according to the orientation.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int previewW = 320;
        int previewH = 240;
        if (mCameraSource != null)
        {
            Size size = mCameraSource.getPreviewSize();
            previewW = size.getWidth();
            previewH = size.getHeight();
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode())
        {
            previewW+= previewH;
            previewH = previewW - previewH;
            previewW-= previewH;
        }

        final int viewWidth = right - left;
        final int viewHeight = bottom - top;

        int childWidth;
        int childHeight;
        int childXOffset = 0;
        int childYOffset = 0;
        float widthRatio = (float) viewWidth / (float) previewW;
        float heightRatio = (float) viewHeight / (float) previewH;

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.
        if (widthRatio > heightRatio)
        {
            childWidth = viewWidth;
            childHeight = (int) ((float) previewH * widthRatio);
            childYOffset = (childHeight - viewHeight) / 2;
        }
        else
        {
            childWidth = (int) ((float) previewW * heightRatio);
            childHeight = viewHeight;
            childXOffset = (childWidth - viewWidth) / 2;
        }

        for (int i = 0; i < getChildCount(); ++i)
        {
            // One dimension will be cropped.  We shift child over or up by this offset and adjust
            // the size to maintain the proper aspect ratio.
            getChildAt(i).layout(-1 * childXOffset, -1 * childYOffset, childWidth - childXOffset, childHeight - childYOffset);
        }

        try
        {
            startIfReady();
        }
        catch (SecurityException se)
        {
            Log.e(TAG,"Do not have permission to start the camera", se);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mSurfaceAvailable = true;
        try
        {
            startIfReady();
        }
        catch (SecurityException se)
        {
            Log.e(TAG,"Do not have permission to start the camera", se);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mSurfaceAvailable = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void start(CameraSource cameraSource) throws IOException, SecurityException
    {
        if (cameraSource == null) {
            stop();
        }
        mCameraSource = cameraSource;
        if (mCameraSource != null)
        {
            mStartRequested = true;
            startIfReady();
        }
    }

    public void stop()
    {
        if (mCameraSource != null) {
            mCameraSource.stop();
        }
    }

    public void release()
    {
        if (mCameraSource != null)
        {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private void startIfReady() throws IOException, SecurityException
    {
        if (mStartRequested && mSurfaceAvailable)
        {
            mCameraSource.start(mSurfaceView.getHolder());
            mStartRequested = false;
        }
    }

    private boolean isPortraitMode()
    {
        return mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
*/