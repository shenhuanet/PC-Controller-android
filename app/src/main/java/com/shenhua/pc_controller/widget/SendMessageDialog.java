package com.shenhua.pc_controller.widget;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.shenhua.pc_controller.R;
import com.shenhua.pc_controller.base.BaseBottomSheetDialog;
import com.shenhua.pc_controller.core.SocketImpl;
import com.shenhua.pc_controller.utils.SocketCallback;

import static com.shenhua.pc_controller.utils.StringUtils.EDIT_COPY;
import static com.shenhua.pc_controller.utils.StringUtils.EDIT_PASTE;

/**
 * Created by shenhua on 1/5/2017.
 * Email shenhuanet@126.com
 */
public class SendMessageDialog extends BaseBottomSheetDialog implements View.OnClickListener {

    public SendMessageDialog(@NonNull Activity activity, @LayoutRes int contentViewRes) {
        super(activity, contentViewRes);
    }

    @Override
    protected void onBindContentView(Window window) {
        window.findViewById(R.id.btn_copy).setOnClickListener(this);
        window.findViewById(R.id.btn_paste).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                SocketImpl.getInstance().communicate(EDIT_COPY, new SocketCallback() {
                    @Override
                    public void onSuccess(Object msg) {
                        String s = new String((byte[]) msg);
                        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setPrimaryClip(ClipData.newPlainText(null, s));
                    }

                    @Override
                    public void onFailed(int errorCode, String msg) {

                    }
                });
                break;
            case R.id.btn_paste:
                // TODO: 1/15/2017 取出手机剪切板文字，发送至pc端，再粘贴
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager.hasPrimaryClip()) {
                    String str = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                    SocketImpl.getInstance().communicate(EDIT_PASTE + str, null);
                } else {
                    Toast.makeText(getContext(), "剪切板中没有内容", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
