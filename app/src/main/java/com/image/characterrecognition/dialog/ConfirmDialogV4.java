package com.image.characterrecognition.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.image.characterrecognition.R;


/**
 * Created by xuzelei on 2016/5/3.
 */
public class ConfirmDialogV4 extends DialogFragment {
    private View view;
    private ConfirmDialog.IOkClickedListener okClickListener;
    private ConfirmDialog.ICancelClickedListener cancelClickedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_submit, container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    private void initView() {
        Bundle arguments = getArguments();
        if (null != arguments) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(arguments.getString("title", "提示"));
            ((TextView) view.findViewById(R.id.tv_msg)).setText(arguments.getString("msg", ""));
            ((TextView) view.findViewById(R.id.tv_ok)).setText(arguments.getString("ok", "确定"));
            ((TextView) view.findViewById(R.id.tv_cancel)).setText(arguments.getString("cancel", "取消"));
            getDialog().setCanceledOnTouchOutside(arguments.getBoolean("canceledOnTouchOutside", true));
            setCancelable(arguments.getBoolean("cancelable", true));
        }

        view.findViewById(R.id.tv_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (okClickListener != null) {
                            okClickListener.callback();
                        }
                        dismissAllowingStateLoss();
                    }
                });
        view.findViewById(R.id.tv_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelClickedListener != null) {
                            cancelClickedListener.callback();
                        }
                        dismissAllowingStateLoss();
                    }
                });
    }

    //    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // 校验主Activity实现回调接口
//        try {
//            // 获得NoticeDialogListener实例，这样我们就能将事件发送到主Activity
//            okClickListener = (IOkClickedListener) activity;
//        } catch (ClassCastException e) {
//            // activity没有实现这个接口则抛出异常
//            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
//        }
//    }
    public void setOnOkClickedListener(ConfirmDialog.IOkClickedListener clickCBListener) {
        this.okClickListener = clickCBListener;
    }

    public void setOnCancelClickedListener(ConfirmDialog.ICancelClickedListener cancelClickedListener) {
        this.cancelClickedListener = cancelClickedListener;
    }
}
