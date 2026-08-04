/*
 * SPDX-FileCopyrightText: 2026 The LegacyDroid Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.lineageos.setupwizard.R;

/**
 * Material You hero used on the very first setup screen. It reproduces the LegacyDroid demo
 * welcome step: fluid background blobs, a floating circular brand logo, headline and pill
 * buttons with a staggered reveal.
 */
public class WelcomeView extends FrameLayout {

    private static final long FLOAT_DURATION_MS = 3800L;
    private static final long BLOB_DURATION_MS = 14000L;

    private boolean mAnimationsStarted;

    public WelcomeView(@NonNull Context context) {
        this(context, null);
    }

    public WelcomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.welcome_view_content, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAnimationsStarted) {
            mAnimationsStarted = true;
            startEntranceAnimations();
            startAmbientAnimations();
        }
    }

    private void startEntranceAnimations() {
        View logoCircle = findViewById(R.id.logo_circle);
        TextView brandTitle = findViewById(R.id.welcome_brand_title);
        TextView subtitle = findViewById(R.id.welcome_title);
        Button start = findViewById(R.id.start);
        Button accessibility = findViewById(R.id.launch_accessibility);
        Button emergency = findViewById(R.id.emerg_dialer);

        enter(logoCircle, 0L, 0f, 12f);
        enter(brandTitle, 120L, 0f, 24f);
        enter(subtitle, 200L, 0f, 24f);
        enter(start, 260L, 0f, 24f);
        enter(accessibility, 320L, 0f, 24f);
        enter(emergency, 360L, 0f, 24f);
    }

    private void enter(View view, long delay, float fromTranslationY, float startAlpha) {
        view.setAlpha(0f);
        view.setTranslationY(fromTranslationY);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(delay)
                .setDuration(420L)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void startAmbientAnimations() {
        View logoCircle = findViewById(R.id.logo_circle);
        ObjectAnimator floatAnimator = ObjectAnimator.ofFloat(logoCircle, View.TRANSLATION_Y, 0f, -12f);
        floatAnimator.setDuration(FLOAT_DURATION_MS);
        floatAnimator.setStartDelay(600L);
        floatAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        floatAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        floatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        floatAnimator.start();

        View blob1 = findViewById(R.id.fluid_blob_1);
        ObjectAnimator blob1Animator = ObjectAnimator.ofFloat(blob1, View.TRANSLATION_X, 0f, 48f);
        blob1Animator.setDuration(BLOB_DURATION_MS);
        blob1Animator.setRepeatCount(ObjectAnimator.INFINITE);
        blob1Animator.setRepeatMode(ObjectAnimator.REVERSE);
        blob1Animator.setInterpolator(new DecelerateInterpolator());
        blob1Animator.start();

        View blob2 = findViewById(R.id.fluid_blob_2);
        ObjectAnimator blob2Animator = ObjectAnimator.ofFloat(blob2, View.TRANSLATION_Y, 0f, -40f);
        blob2Animator.setDuration(BLOB_DURATION_MS);
        blob2Animator.setStartDelay(1400L);
        blob2Animator.setRepeatCount(ObjectAnimator.INFINITE);
        blob2Animator.setRepeatMode(ObjectAnimator.REVERSE);
        blob2Animator.setInterpolator(new DecelerateInterpolator());
        blob2Animator.start();
    }
}
