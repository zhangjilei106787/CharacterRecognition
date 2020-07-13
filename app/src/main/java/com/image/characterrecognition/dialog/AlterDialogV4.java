package com.image.characterrecognition.dialog;

import android.content.DialogInterface;
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
 * Created by xuzelei on 2016/4/27.
 */
public class AlterDialogV4 extends DialogFragment {
    private View view;
    private AlterDialog.IOkClickedListener okClickListener;
    private DialogInterface.OnDismissListener onDismissListener;

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
    public void setOnOkClickedListener(AlterDialog.IOkClickedListener clickCBListener) {
        this.okClickListener = clickCBListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}