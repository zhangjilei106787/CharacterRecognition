package com.image.characterrecognition.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.image.characterrecognition.dialog.AlterDialog;
import com.image.characterrecognition.dialog.AlterDialogV4;
import com.image.characterrecognition.dialog.ConfirmDialog;
import com.image.characterrecognition.dialog.ConfirmDialogV4;
import com.image.characterrecognition.dialog.DialogLoadingFM;

/**
 * 对话框工具类、
 * 徐泽雷
 */
public class DialogUtil {
    private static final String TITLE = "title";
    private static final String CANCELABLE = "cancelable";
    private static final String CANCELED_ON_TOUCH = "canceledOnTouchOutside";
    /**
     * 提示对话框
     *
     * @param context                上下文
     * @param title                  标题
     * @param msg                    提示信息
     * @param ok                     确定文字信息
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     * @param okListener             点击确定按钮的回调
     */
    public static void alter(Context context, String title, String msg, String ok, boolean cancelable, boolean canceledOnTouchOutside, AlterDialog.IOkClickedListener okListener) {
        //对ActivityCheckUtil.isActive判断是为了防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return;

        if (context != null && context instanceof FragmentActivity) {
            FragmentActivity ac = (FragmentActivity) context;
            AlterDialogV4 d = new AlterDialogV4();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);

            //d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            FragmentTransaction transaction = ac.getSupportFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        } else if (context != null && context instanceof Activity) {
            Activity ac = (Activity) context;
            AlterDialog d = new AlterDialog();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);

            //d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            android.app.FragmentTransaction transaction = ac.getFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 提示对话框
     *
     * @param context                上下文
     * @param title                  标题
     * @param msg                    提示信息
     * @param ok                     确定文字信息
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     * @param okListener             点击确定按钮的回调
     * @param onDismissListener      对话框消失时的回调
     */
    public static void alter(Context context, String title, String msg, String ok, boolean cancelable, boolean canceledOnTouchOutside, DialogInterface.OnDismissListener onDismissListener, AlterDialog.IOkClickedListener okListener) {
        //对ActivityCheckUtil.isActive判断是为了防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return;

        if (context != null && context instanceof FragmentActivity) {
            FragmentActivity ac = (FragmentActivity) context;
            AlterDialogV4 d = new AlterDialogV4();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);
            d.setOnDismissListener(onDismissListener);

            //d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            FragmentTransaction transaction = ac.getSupportFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        } else if (context != null && context instanceof Activity) {
            Activity ac = (Activity) context;
            AlterDialog d = new AlterDialog();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);
            d.setOnDismissListener(onDismissListener);

            //d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            android.app.FragmentTransaction transaction = ac.getFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        }
    }

    public static void alter(Context context, String title, String msg, String ok, boolean cancelable, boolean canceledOnTouchOutside, DialogInterface.OnDismissListener onDismissListener) {
        alter(context, title, msg, ok, cancelable, canceledOnTouchOutside, onDismissListener, null);
    }

    /**
     * 提示对话框
     *
     * @param context                上下文
     * @param title                  标题
     * @param msg                    提示信息
     * @param ok                     确定按钮
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     */
    public static void alter(Context context, String title, String msg, String ok, boolean cancelable, boolean canceledOnTouchOutside) {
        alter(context, title, msg, ok, cancelable, canceledOnTouchOutside, null, null);
    }

    /**
     * 提示对话框
     *
     * @param context 上下文
     * @param title   标题
     * @param msg     提示信息
     * @param ok      确定按钮
     */
    public static void alter(Context context, String title, String msg, String ok) {
        alter(context, title, msg, ok, true, true, null, null);
    }

    /**
     * 提示对话框
     *
     * @param context 上下文
     * @param msg     提示信息
     */
    public static void alter(Context context, String msg) {
        alter(context, "提示", msg, "确定", true, true, null, null);
    }

    /**
     * 确认对话框
     *
     * @param context                上下文
     * @param title                  对话框标题
     * @param msg                    提示信息
     * @param ok                     确定按钮
     * @param cancel                 取消按钮
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     * @param okListener             确定按钮的回调
     * @param cancelListener         取消按钮的回调
     */
    public static void confirm(Context context, String title, String msg, String ok, String cancel, boolean cancelable, boolean canceledOnTouchOutside, ConfirmDialog.IOkClickedListener okListener, ConfirmDialog.ICancelClickedListener cancelListener) {
        //对ActivityCheckUtil.isActive判断是为了防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return;

        if (context != null && context instanceof FragmentActivity) {
            FragmentActivity ac = (FragmentActivity) context;
            ConfirmDialogV4 d = new ConfirmDialogV4();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putString("cancel", cancel);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);
            d.setOnCancelClickedListener(cancelListener);
//					d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            FragmentTransaction transaction = ac.getSupportFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        } else if (context != null && context instanceof Activity) {
            Activity ac = (Activity) context;
            ConfirmDialog d = new ConfirmDialog();
            Bundle arguments = new Bundle();
            arguments.putString(TITLE, title);
            arguments.putString("msg", msg);
            arguments.putString("ok", ok);
            arguments.putString("cancel", cancel);
            arguments.putBoolean(CANCELABLE, cancelable);
            arguments.putBoolean(CANCELED_ON_TOUCH, canceledOnTouchOutside);
            d.setArguments(arguments);
            d.setOnOkClickedListener(okListener);
            d.setOnCancelClickedListener(cancelListener);
//					d.show(((FragmentActivity) context).getSupportFragmentManager(), "");

            //show()改为transaction是防止onSaveInstanceState（）后出错
            android.app.FragmentTransaction transaction = ac.getFragmentManager().beginTransaction();
            transaction.add(d, d.getClass().getName());
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 确认对话框
     *
     * @param context                上下文
     * @param title                  对话框标题
     * @param msg                    提示信息
     * @param ok                     确定按钮
     * @param cancel                 取消按钮
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     * @param okListener             确定的回调
     */
    public static void confirm(Context context, String title, String msg, String ok, String cancel, boolean cancelable, boolean canceledOnTouchOutside, ConfirmDialog.IOkClickedListener okListener) {
        confirm(context, title, msg, ok, cancel, cancelable, canceledOnTouchOutside, okListener, null);
    }

    /**
     * 确认对话框
     *
     * @param context                上下文
     * @param msg                    提示信息
     * @param ok                     确定按钮
     * @param cancelable             是否允许被返回键取消
     * @param canceledOnTouchOutside 是否允许点击对话框外被取消
     * @param okListener             确定的回调
     */
    public static void confirm(Context context, String msg, String ok, boolean cancelable, boolean canceledOnTouchOutside, ConfirmDialog.IOkClickedListener okListener) {
        confirm(context, "提示", msg, ok, "取消", cancelable, canceledOnTouchOutside, okListener, null);
    }

    /**
     * 返回进度dialogfragment，注意返回null的判断
     *
     * @param context 上下文
     * @param msg     进度提示
     * @return
     */
    public static DialogLoadingFM getProgressDialog(final Context context, String msg) {
        //对ActivityCheckUtil.isActive判断是为了防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return null;

        DialogLoadingFM d = new DialogLoadingFM();
        Bundle arg = new Bundle();
        arg.putString("msg", msg);
        d.setArguments(arg);
        return d;
    }

    /**
     * 返回进度dialogfragment，注意返回null的判断
     *
     * @param context 上下文
     * @param msg     进度提示
     * @return
     */
    public static DialogLoadingFMV4 getProgressDialogV4(final Context context, String msg) {
        //对ActivityCheckUtil.isActive判断是为了防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return null;

        DialogLoadingFMV4 d = new DialogLoadingFMV4();
        Bundle arg = new Bundle();
        arg.putString("msg", msg);
        d.setArguments(arg);
        return d;
    }

    public static void show(Context context, DialogLoadingFM dfm) {
        //防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return;

        if (context != null && context instanceof Activity) {
            Activity ac = (Activity) context;
            if (null != dfm && !dfm.isAdded()) {
                //show()改为transaction防止onSaveInstanceState（）后出错
                android.app.FragmentTransaction transaction = ac.getFragmentManager().beginTransaction();
                transaction.add(dfm, dfm.getClass().getName());
                transaction.commitAllowingStateLoss();
            }
        }
    }

    public static void show(Context context, DialogLoadingFMV4 dfm) {
        //防止当activity释放时出错
        if (!ActivityCheckUtil.isActive(context)) return;

        if (context != null && context instanceof FragmentActivity) {
            FragmentActivity ac = (FragmentActivity) context;
            if (null != dfm && !dfm.isAdded()) {
                //show()改为transaction防止onSaveInstanceState（）后出错
                FragmentTransaction transaction = ac.getSupportFragmentManager().beginTransaction();
                transaction.add(dfm, dfm.getClass().getName());
                transaction.commitAllowingStateLoss();
            }
        }
    }

    public static void dismiss(Context context, DialogLoadingFM dfm) {
        if (!ActivityCheckUtil.isActive(context)) return;

        if (null != dfm && dfm.isResumed()) {
            dfm.dismissAllowingStateLoss();
        }
    }

    public static void dismiss(Context context, DialogLoadingFMV4 dfm) {
        if (!ActivityCheckUtil.isActive(context)) return;

//        if (null != dfm && dfm.isResumed()) {
//            dfm.dismissAllowingStateLoss();
//        }

        if (null != dfm) {
            dfm.dismissAllowingStateLoss();
        }
    }
}
