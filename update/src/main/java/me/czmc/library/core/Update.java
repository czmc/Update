package me.czmc.library.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import me.czmc.library.entity.CheckResult;
import me.czmc.library.entity.DownloadModel;
import me.czmc.library.listener.OnDownloadListener;
import me.czmc.library.listener.UIupdateCheckCallBack;
import me.czmc.library.utils.AppUtils;
import me.czmc.library.view.DownloadDialog;
import me.czmc.library.view.UpdateDialog;

/**
 * Created by czmc on 2016/9/5.
 */

public class Update {
    public OnCheckListener listener;
    private Context context;
    public DownloadDialog downloadDialog;
    private boolean enableDownloadDialog=true;

    public Update(Context context) {
        this.context = context;
    }

    public void requestUpdate(String url) {
        UpdateApi.ins().checkUpdate(url, new UIupdateCheckCallBack() {
            @Override
            public void onUIFailure(IOException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUIResponse(CheckResult result) throws IOException {
                if (result.isSuccessed()) {
                    if (listener != null) {
                        listener.checkUpdate(result);
                    } else {
                        if (AppUtils.getVersionCode(context) < result.versionCode) {
                            Log.i("TAG", result.toString());
                            Toast.makeText(context, "需要更新", Toast.LENGTH_SHORT).show();
                            UpdateDialog.show(context, result.describtion, result.downloadUrl);
                            if (enableDownloadDialog) {
                                addDownloadListener(new OnDownloadListener() {
                                    @Override
                                    public void onDownload(DownloadModel model) {
                                        if(enableDownloadDialog) {
                                            if (downloadDialog == null) {
                                                downloadDialog = new DownloadDialog(context);
                                                downloadDialog.show();
                                            }
                                            downloadDialog.setModel(model);
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(context, "已经是最新的了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void enableDownloadDialog(boolean enable){
        this.enableDownloadDialog =enable;
    }
    public void addDownloadListener(OnDownloadListener listener){
        UpdateApi.ins().addDownloadListener(listener);
    }

    public void setCheckListener(OnCheckListener checkListener) {
        this.listener = checkListener;
    }

    public interface OnCheckListener {
        void checkUpdate(CheckResult result);
    }
    public void release(){
        UpdateApi.ins().release();
        context=null;
    }
}
