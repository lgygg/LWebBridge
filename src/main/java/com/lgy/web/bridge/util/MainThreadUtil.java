package com.lgy.web.bridge.util;

import android.os.Handler;
import android.os.Looper;

/**
 * @author: Administrator
 * @date: 2023/8/29
 */
public class MainThreadUtil {

    public static void runMain(Runnable runnable){
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
            return;
        }
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
