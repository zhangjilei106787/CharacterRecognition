package com.image.characterrecognition.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.image.characterrecognition.R;


/**
 * Created by xuzelei on 2016/4/27.
 */
public class AlterDialog extends DialogFragment {
    private View view;
    private IOkClickedListener okClickListener;
    private DialogInterface.OnDismissListener onDismissListener;

    public interface IOkClickedListener {
        public void callback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_alert, container, false);
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
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
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
    public void setOnOkClickedListener(IOkClickedListener clickCBListener) {
        this.okClickListener = clickCBListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}