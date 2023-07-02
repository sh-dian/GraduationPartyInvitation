package com.example.graduationpartyinvitation;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

public class FingerPrintHelper extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private FingerPrintSensor activity;

    public FingerPrintHelper(Context context, FingerPrintSensor activity) {
        this.context = context;
        this.activity = activity;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerPrintSensor fingerPrintSensor) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(null, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(context, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(context, "Authentication help: " + helpString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
        activity.navigateToNextPage();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }
}



