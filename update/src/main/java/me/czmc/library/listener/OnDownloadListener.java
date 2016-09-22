package me.czmc.library.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import me.czmc.library.entity.DownloadModel;

public abstract class OnDownloadListener implements DownloadProgressListener {
    private static final int RESPONSE_UPDATE = 0x02;
    //处理UI层的Handler子类
    private static class UIHandler extends Handler {
        //弱引用
        private final WeakReference<OnDownloadListener> mUIProgressResponseListenerWeakReference;
 
        public UIHandler(Looper looper, OnDownloadListener uiProgressResponseListener) {
            super(looper);
            mUIProgressResponseListenerWeakReference = new WeakReference<OnDownloadListener>(uiProgressResponseListener);
        }
 
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE_UPDATE:
                    OnDownloadListener uiProgressResponseListener = mUIProgressResponseListenerWeakReference.get();
                    if (uiProgressResponseListener != null) {
                        //获得进度实体类
                        DownloadModel model = (DownloadModel) msg.obj;
                        //回调抽象方法
                        uiProgressResponseListener.onDownload(model);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    //主线程Handler
    private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);

    @Override
    public void update(long bytesRead, long contentLength, boolean done,String path) {
        Message message = Message.obtain();
        message.obj = new DownloadModel(bytesRead, contentLength, done,path);
        message.what = RESPONSE_UPDATE;
        mHandler.sendMessage(message);
    }

    public abstract void onDownload(DownloadModel model);
}