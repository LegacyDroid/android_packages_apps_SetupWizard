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
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

        logoAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                logo.setVisibility(View.VISIBLE);
            }
            @Override public void onAnimationEnd(Animator animation) {
                animateLetters();
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });

        logoAnim.start();
    }

    private void animateLetters() {
        String text = "LegacyDroid";
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            int resId = getResources().getIdentifier("letter_" + i, "id", getPackageName());
            final TextView letter = findViewById(resId);
            if (letter == null) continue;

            letter.setTranslationY(120f);
            letter.setAlpha(0f);
            letter.setVisibility(View.VISIBLE);

            Keyframe kf0 = Keyframe.ofFloat(0f, 120f);
            Keyframe kf1 = Keyframe.ofFloat(0.5f, -15f);
            Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
            PropertyValuesHolder jump = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, kf0, kf1, kf2);
            PropertyValuesHolder fade = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);

            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(letter, jump, fade);
            anim.setDuration(400);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());

            mHandler.postDelayed(() -> anim.start(), 800 + (index * 80));
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_activity;
    }
}
