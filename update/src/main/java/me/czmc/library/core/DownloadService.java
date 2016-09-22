package me.czmc.library.core;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import me.czmc.library.entity.DownloadModel;
import me.czmc.library.listener.OnDownloadListener;
import me.czmc.library.utils.AppUtils;
import me.czmc.library.utils.Constants;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private String apkUrl = "";
    private int type;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apkUrl = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);
        type = intent.getIntExtra("type", 0x11);
        if (type == UpdateInterface.TYPE_FROM_NOTIFY) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                    .setContentTitle("下载更新")
                    .setContentText("正在下载")
                    .setAutoCancel(true);

            notificationManager.notify(0, notificationBuilder.build());
        }
        download();
    }
    int i=0;
    private void download() {
        UpdateApi.ins().downloadApp(apkUrl, new OnDownloadListener() {
            @Override
            public void onDownload(DownloadModel model) {
//                DownloadModel model = new DownloadModel(bytesRead,contentLength,done,path);
//                int progress = (int) ((bytesRead * 100) / contentLength);
//                model.progress=progress;
                i++;
                if (type == UpdateInterface.TYPE_FROM_NOTIFY) {
                    if(i>50) {
                        sendNotification(model);
                        i=0;
                    }
                    if (model.done)
                      downloadCompleted();
                }
                if (model.done) {
                    DownloadService.this.startActivity(AppUtils.openFile(model.path));
                    stopSelf();
                }
            }
        });
    }

    private void downloadCompleted() {
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("更新包下载完成");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification(DownloadModel download) {
        notificationBuilder.setProgress(100, download.progress, true);
        notificationBuilder.setContentText(download.progress+"%");
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}