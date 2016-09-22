package me.czmc.library.core;

import android.content.Context;
import android.content.Intent;

import me.czmc.library.listener.OnDownloadListener;
import me.czmc.library.utils.Constants;

/**
 * Created by czmc on 2016/9/6.
 */

public class UpdateInterface {
    public static final int TYPE_FROM_MAIN=0x11;
    public static final int TYPE_FROM_NOTIFY=0x12;
    private static Update ins;
    public static void init(Context context){
        if(ins==null) {
            ins=new Update(context);
        }
    }
    public static void requestUpdate(String url){
        if(ins!=null){
            ins.requestUpdate(url);
        }else {
            throw new RuntimeException("no-init");
        }
    }
    public static void setOnCheckListenr(Update.OnCheckListener checkListenr){
        if(ins!=null){
            ins.setCheckListener(checkListenr);
        }else {
            throw new RuntimeException("no-init");
        }
    }
    public static  void enableDownloadDialog(boolean enable){
        if(ins!=null){
            ins.enableDownloadDialog(enable);
        }else {
            throw new RuntimeException("no-init");
        }
    }
    public static  void addOnDownloadListener(OnDownloadListener onDownloadListener){
        if(ins!=null){
            ins.addDownloadListener(onDownloadListener);
        }else {
            throw new RuntimeException("no-init");
        }
    }
    public static void release(){
        ins.release();
        ins=null;
    }
    public static void goToDownload(Context context, String downloadUrl,int type) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra("type",type);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
