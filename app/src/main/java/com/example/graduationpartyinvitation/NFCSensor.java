package com.example.graduationpartyinvitation;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;

public class NFCSensor extends AppCompatActivity implements View.OnClickListener {

    public static final String Error_Detected = "No NFC Detected";
    public static final String Write_Success = "Text Written";

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    Tag myTag;
    Context context;

    TextView nfc_contents;

    private ImageView nfcImageView;
    private AnimatorSet animatorSet;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcsensor);

        context = this;

        nfcImageView = findViewById(R.id.nfc_image);
        nfc_contents = findViewById(R.id.nfc_contents);

        // Load the fade animation from XML
        animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.fade_animation);

        // Check if NFC is available on the device
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            // NFC is not available or disabled
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
        } else {
            // NFC is available and enabled
            Toast.makeText(this, "NFC enabled", Toast.LENGTH_SHORT).show();
            // Start the animation
            startNfcAnimation();
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "Your Device Does Not Support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }

        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Toast.makeText(this, "Tag Detected", Toast.LENGTH_SHORT).show();
            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] message = null;
            if (rawMessage != null) {
                message = new NdefMessage[rawMessage.length];
                for (int i = 0; i < rawMessage.length; i++) {
                    message[i] = (NdefMessage) rawMessage[i];
                }
            }
            buildTagViews(message);
        }
    }

    private void buildTagViews(NdefMessage[] message) {
        if (message == null || message.length == 0) {
            Toast.makeText(context, "Message Null", Toast.LENGTH_SHORT).show();
        }

        String text = "";
        byte[] payload = message[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;

        try {
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("Unsupported Encoding", e.toString());
        }

        nfc_contents.setText("NFC Content: " + text);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if NFC is available on the device
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            // NFC is not available or disabled
            Toast.makeText(this, "NFC not available / disable on your device", Toast.LENGTH_SHORT).show();

        } else {
            // Start the animation
            startNfcAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop the animation
        stopNfcAnimation();
    }

    private void startNfcAnimation() {
        // Apply the animation to the NFC image
        animatorSet.setTarget(nfcImageView);
        animatorSet.start();
    }

    private void stopNfcAnimation() {
        // Cancel the animation
        animatorSet.cancel();
    }

    @Override
    public void onClick(View v) {
        // Handle button click events here
        switch (v.getId()) {
        }
    }
}