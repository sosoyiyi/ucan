/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.ucan.app.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ucan.app.R;


/**
 * 网络访问对话框
 * 
 */
public class UCProgressDialog extends Dialog {

    private TextView mTextView;
    private View mImageView;
    AsyncTask mAsyncTask;

    private final OnCancelListener mCancelListener
            = new OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            if(mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
        }
    };

    /**
     * @param context
     */
    public UCProgressDialog(Context context) {
        super(context , R.style.Theme_Light_CustomDialog_Blue);
        mAsyncTask = null;
        setCancelable(true);
        setContentView(R.layout.common_loading_diloag);
        mTextView = (TextView)findViewById(R.id.textview);
        mTextView.setText(R.string.loading_press);
        mImageView = findViewById(R.id.imageview);
        setOnCancelListener(mCancelListener);
    }

    /**
     * @param context
     * @param resid
     */
    public UCProgressDialog(Context context , int resid) {
        this(context);
        mTextView.setText(resid);
    }

    public UCProgressDialog(Context context , CharSequence text) {
        this(context);
        mTextView.setText(text);
    }

    public UCProgressDialog(Context context , AsyncTask asyncTask) {
        this(context);
        mAsyncTask = asyncTask;
    }

    public UCProgressDialog(Context context , CharSequence text , AsyncTask asyncTask) {
        this(context , text);
        mAsyncTask = asyncTask;
    }

    /**
     * 设置对话框显示文本
     * @param text
     */
    public final void setPressText(CharSequence text) {
        mTextView.setText(text);
    }

    public final void dismiss() {
        super.dismiss();
        mImageView.clearAnimation();
    }

    public final void show() {
        super.show();
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext() ,R.anim.loading);
        mImageView.startAnimation(loadAnimation);
    }
}
