package me.czmc.library.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.czmc.library.R;
import me.czmc.library.entity.DownloadModel;

public class DownloadDialog extends Dialog {

    private ImageView close;
    private ProgressBar progressBar;
    private TextView textView;

    public DownloadDialog(Context context) {
        super(context, R.style.Custom_Dialog_Dim);
        initDialogAttrs(context);
        initView();
    }

    public void setModel(DownloadModel model){
        if(model.done) {
            dismiss();
//            getContext().startActivity(AppUtils.openFile(model.path));
        }else {
            textView.setText(model.progress + "%");
            progressBar.setProgress(model.progress);
        }
    }

    private void initView() {
        setContentView(R.layout.dialog_download);
        close = (ImageView) findViewById(R.id.downloaddialog_close);
        progressBar = (ProgressBar) findViewById(R.id.downloaddialog_progress);
        textView = (TextView) findViewById(R.id.downloaddialog_count);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void initDialogAttrs(Context context) {
        setCanceledOnTouchOutside(true);
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        getWindow().getAttributes().width = metric.widthPixels * 3 / 4;
        getWindow().getAttributes().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getWindow().getAttributes().y = 0;
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().setAttributes(getWindow().getAttributes());
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }
}