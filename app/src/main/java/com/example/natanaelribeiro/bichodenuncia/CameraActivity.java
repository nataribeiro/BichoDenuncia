package com.example.natanaelribeiro.bichodenuncia;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mRecorder;
    private LinearLayout cam_botoes_acao;
    private LinearLayout cam_botoes_confirmacao;

    private Button btn_capture_image;
    private Button btn_start_video;
    private Button btn_stop_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(params);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        cam_botoes_acao = (LinearLayout)findViewById(R.id.cam_botoes_acao);
        cam_botoes_confirmacao = (LinearLayout)findViewById(R.id.cam_botoes_confirmacao);
        btn_capture_image = (Button)findViewById(R.id.btn_capture_image);
        btn_start_video = (Button)findViewById(R.id.btn_start_video);
        btn_stop_video = (Button)findViewById(R.id.btn_stop_video);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if(pictureFile == null){
                Log.d("TakePicture", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TakePicture", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TakePicture", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onClickTirarFoto(View view){
        mCamera.takePicture(null, null, mPicture);
        habilitaBotoesConfirmacao();
    }

    private void habilitaBotoesConfirmacao(){
        cam_botoes_acao.setVisibility(View.GONE);
        cam_botoes_confirmacao.setVisibility(View.VISIBLE);
    }

    private void habilitaBotoesAcao(){
        cam_botoes_confirmacao.setVisibility(View.GONE);
        cam_botoes_acao.setVisibility(View.VISIBLE);
    }

    private boolean prepareMediaRecorder(){
        mRecorder = new MediaRecorder();

        mCamera.unlock();
        mRecorder.setCamera(mCamera);

        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        mRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        try {
            mRecorder.prepare();
        } catch(IllegalStateException e) {
            Log.d("VIDEO", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            mRecorder.release();
            return false;
        }catch (IOException e) {
            Log.d("VIDEO", "IOException preparing MediaRecorder: " + e.getMessage());
            mRecorder.release();
            return false;
        }

        return true;
    }

    public void onClickFilmar(View view) throws IOException {
        mCamera.setPreviewDisplay(mPreview.getHolder());
        if(prepareMediaRecorder()){
            try {
                mRecorder.start();
                btn_start_video.setVisibility(View.GONE);
                btn_capture_image.setVisibility(View.GONE);
                btn_stop_video.setVisibility(View.VISIBLE);
            }catch (Exception e){
                mRecorder.release();
            }
        }
    }

    public void onClickStopVideo(View view){
        mRecorder.stop();
        btn_start_video.setVisibility(View.VISIBLE);
        btn_capture_image.setVisibility(View.VISIBLE);
        btn_stop_video.setVisibility(View.GONE);
        habilitaBotoesConfirmacao();
    }

    public void onClickConfirmaCaptura(View view){

    }

    public void onClickCancelaCaptura(View view) {
        if(mRecorder!=null) {
            mRecorder.reset();
            mRecorder.release();
        }
        mCamera.stopPreview();
        mCamera.startPreview();

        habilitaBotoesAcao();
    }

}
