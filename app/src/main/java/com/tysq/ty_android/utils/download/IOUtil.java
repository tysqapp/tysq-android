package com.tysq.ty_android.utils.download;

import android.os.Build;
import android.util.Log;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public class IOUtil {
    public static void closeIO(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        if (closeable instanceof Flushable) {
            try {
                ((Flushable) closeable).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            Log.e(IOUtil.class.getSimpleName(), e.getMessage());
        }
    }
}
