package com.image.characterrecognition.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

/**
 * Created by xuzl on 2016/10/13.
 */
public class ActivityCheckUtil {
    /**
     * activity是否未释放
     *
     * @param context 上下文
     * @return true：有效  false：已经释放
     */
    public static boolean isActive(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= 17) {
                if (activity.isDestroyed())
                    return false;
            } else {
                if (activity.isFinishing())
                    return false;
            }
        }

        return true;
    }
}
