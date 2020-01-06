package com.abc.lib_utils.toast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.abc.lib_utils.R;

public class JToast {

    private android.widget.Toast mToast;

    private JToast(Context context,
                   CharSequence text,
                   int duration) {

        View v = LayoutInflater
                .from(context)
                .inflate(R.layout.toast_framelayout, null);

        TextView textView = v.findViewById(R.id.message);
        textView.setText(text);

        mToast = android.widget.Toast.makeText(context, text, duration);
        mToast.setDuration(duration);
        mToast.setView(v);

    }

    public static JToast makeText(Context context, CharSequence text, int duration) {
        return new JToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public JToast setText(String text) {

        TextView textView = mToast.getView().findViewById(R.id.message);
        textView.setText(text);

        return this;
    }

    public JToast setGravity(int gravity, int xOffset, int yOffset) {

        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        return this;
    }

    public JToast setDuration(int duration) {

        if (mToast != null) {
            mToast.setDuration(duration);
        }

        return this;

    }

}