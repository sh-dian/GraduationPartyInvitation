package com.example.graduationpartyinvitation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Size;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class QRScanner extends AppCompatActivity {
    PreviewView previewView;
    ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        previewView = findViewById(R.id.cameraPreview);

        //checking for camera permission
        if (ContextCompat.checkSelfPermission(QRScanner.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            ActivityCompat.requestPermissions(QRScanner.this, new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(QRScanner.this);
        cameraProviderListenableFuture.addListener(() -> {

            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindImageAnalysis(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        },ContextCompat.getMainExecutor(QRScanner.this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else {
            Toast.makeText(QRScanner.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(QRScanner.this), image -> {

            Image mediaImage = image.getImage();

            if (mediaImage!=null){
                InputImage image2 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                BarcodeScanner scanner = BarcodeScanning.getClient();

                Task<List<Barcode>> results = scanner.process(image2);

                results.addOnSuccessListener(barcodes -> {

                    for (Barcode barcode : barcodes){
                        final String getValue = barcode.getRawValue();
                        displayClickableLink(getValue);
                    }

                    image.close();
                    mediaImage.close();
                });
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector,imageAnalysis,preview);
    }

    private void displayClickableLink(String link) {
        TextView linkTextView = findViewById(R.id.linkTextView);
        SpannableString spannableString = new SpannableString(link);
        Linkify.addLinks(spannableString, Linkify.WEB_URLS);
        linkTextView.setText(spannableString);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView.setVisibility(View.VISIBLE);
    }
}