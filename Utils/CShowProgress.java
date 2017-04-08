package com.iappstreat.ixigohackathon.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iappstreat.ixigohackathon.R;

/**
 * Created by verma on 08-04-2017.
 */

public class CShowProgress {
    public static CShowProgress s_m_oCShowProgress;

    public Dialog m_Dialog;
    private ProgressBar m_ProgressBar;
    private static Context context;

    private CShowProgress(Context context) {
        this.context = context;

    }

    public static CShowProgress getInstance() {
        if (s_m_oCShowProgress == null) {
            s_m_oCShowProgress = new CShowProgress(context);
        }
        return s_m_oCShowProgress;
    }

    public void showProgress(Context m_Context, String message) {
        m_Dialog = new Dialog(m_Context);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        m_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_Dialog.setContentView(R.layout.progress_bar);
        m_ProgressBar = (ProgressBar) m_Dialog.findViewById(R.id.progress_bar);
        TextView progressText = (TextView) m_Dialog.findViewById(R.id.progress_text);
        progressText.setText("" + message);
        progressText.setVisibility(View.VISIBLE);
        m_ProgressBar.setVisibility(View.VISIBLE);
        m_ProgressBar.setIndeterminate(true);
        m_Dialog.setCancelable(false);
        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.show();
    }
    public  void dismissDialog(){
        m_Dialog.dismiss();
    }
}
