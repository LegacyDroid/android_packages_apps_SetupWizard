/*
 * SPDX-FileCopyrightText: 2016 The CyanogenMod Project
 * SPDX-FileCopyrightText: 2017-2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard;

import static org.lineageos.setupwizard.SetupWizardApp.ACTION_EMERGENCY_DIAL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.lineageos.setupwizard.util.SetupWizardUtils;

public class WelcomeActivity extends SubBaseActivity {

    private static final String ACTION_ACCESSIBILITY_SETTINGS =
            "android.settings.ACCESSIBILITY_SETTINGS_FOR_SUW";

    @Override
    protected void onStartSubactivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSetupStart();
        applyFullscreen();
        Button startButton = findViewById(R.id.start);
        Button emergButton = findViewById(R.id.emerg_dialer);
        startButton.setOnClickListener(view -> onNextPressed());
        findViewById(R.id.launch_accessibility)
                .setOnClickListener(
                        view -> startSubactivity(new Intent(ACTION_ACCESSIBILITY_SETTINGS)));

        if (SetupWizardUtils.hasTelephony(this)) {
            emergButton.setOnClickListener(
                    view -> startSubactivity(new Intent(ACTION_EMERGENCY_DIAL)));
        } else {
            emergButton.setVisibility(View.GONE);
        }

        TextView welcomeTitle = findViewById(R.id.welcome_title);
        if (SetupWizardUtils.isManagedProfile(this)) {
            welcomeTitle.setText(getString(R.string.setup_managed_profile_welcome_message));
        }
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Hide both the status bar and the navigation bar so the welcome hero
     * draws edge-to-edge over the whole screen (no black system bars).
     */
    private void applyFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        controller.hide(WindowInsetsCompat.Type.statusBars()
                | WindowInsetsCompat.Type.navigationBars());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_activity;
    }

    @Override
    protected int getTitleResId() {
        return -1;
    }
}
