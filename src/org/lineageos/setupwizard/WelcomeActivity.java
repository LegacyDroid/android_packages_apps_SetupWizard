/*
 * Copyright (C) 2016 The CyanogenMod Project
 * Copyright (C) 2017-2021 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.setupwizard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.setupcompat.util.SystemBarHelper;

import org.lineageos.setupwizard.util.EnableAccessibilityController;

public class WelcomeActivity extends BaseSetupWizardActivity {

    public static final String TAG = WelcomeActivity.class.getSimpleName();

    private View mRootView;
    private EnableAccessibilityController mEnableAccessibilityController;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarHelper.setBackButtonVisible(getWindow(), false);
        mRootView = findViewById(R.id.setup_wizard_layout);
        setNextText(R.string.start);
        setSkipText(R.string.emergency_call);
        findViewById(R.id.start).setOnClickListener(view -> onNextPressed());
        findViewById(R.id.emerg_dialer)
                .setOnClickListener(view -> startEmergencyDialer());
        findViewById(R.id.launch_accessibility)
                .setOnClickListener(view -> startAccessibilitySettings());
        mEnableAccessibilityController =
                EnableAccessibilityController.getInstance(getApplicationContext());
        mRootView.setOnTouchListener((v, event) ->
                mEnableAccessibilityController.onTouchEvent(event));

        startAnimations();
    }

    private void startAnimations() {
        ImageView logo = findViewById(R.id.brand_logo);

        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(logo, View.SCALE_X, 0f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(logo, View.SCALE_Y, 0f, 1f);
        ObjectAnimator logoAlpha = ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f);

        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.setDuration(800);
        logoAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        logoAnim.playTogether(logoScaleX, logoScaleY, logoAlpha);

        final TextView welcomeText = findViewById(R.id.welcome_text);

        logoAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                logo.setVisibility(View.VISIBLE);
            }
            @Override public void onAnimationEnd(Animator animation) {
                animateWelcomeText(welcomeText);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });

        logoAnim.start();
    }

    private void animateWelcomeText(TextView welcomeText) {
        if (welcomeText == null) return;
        welcomeText.setTranslationY(40f);
        welcomeText.setAlpha(0f);
        welcomeText.setVisibility(View.VISIBLE);

        ObjectAnimator slide = ObjectAnimator.ofFloat(welcomeText, View.TRANSLATION_Y, 40f, 0f);
        ObjectAnimator fade = ObjectAnimator.ofFloat(welcomeText, View.ALPHA, 0f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(slide, fade);
        set.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_activity;
    }
}
