package me.czmc.library.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;

import me.czmc.library.entity.CheckResult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by czmc on 2016/9/5.
 */

public abstract class UIupdateCheckCallBack implements Callback {
    private static final int REQUEST_FAILE=0xaa;
    private static final int REQUEST_SUCCESS=0x0a;
    private class UIHandler extends Handler {
        //弱引用
        private  WeakReference<UIupdateCheckCallBack> mUIRequestCallBackWeakReference;

        public UIHandler(Looper looper, UIupdateCheckCallBack requestCallBack) {
            super(looper);
            mUIRequestCallBackWeakReference = new WeakReference<UIupdateCheckCallBack>(requestCallBack);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    try {
                        CheckResult result = (CheckResult) msg.obj;
                        onUIResponse(result);
                    }catch (IOException e){
                        onUIFailure(e);
                    };
                    break;
                case REQUEST_FAILE:
                    IOException exception = (IOException)msg.obj;
                    onUIFailure(exception);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    //主线程Handler
    private final Handler mHandler = new UIupdateCheckCallBack.UIHandler(Looper.getMainLooper(), this);
    @Override
    public void onFailure(Call call, IOException e) {
        Message message = Message.obtain();
        message.obj = e;
        message.what = REQUEST_FAILE;
        mHandler.sendMessage(message);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()){
            CheckResult result =  new Gson().fromJson(response.body().string(),CheckResult.class);
            Message message = Message.obtain();
            message.obj = result;
            message.what = REQUEST_SUCCESS;
            mHandler.sendMessage(message);
        }
    }

    public abstract void onUIFailure(IOException e);
    public abstract void onUIResponse(CheckResult result) throws IOException ;
}
