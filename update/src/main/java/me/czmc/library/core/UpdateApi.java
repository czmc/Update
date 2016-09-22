package me.czmc.library.core;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.czmc.library.listener.DownloadProgressListener;
import me.czmc.library.listener.DownloadProgressResponseBody;
import me.czmc.library.listener.OnDownloadListener;
import me.czmc.library.listener.UIupdateCheckCallBack;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by czmc on 2016/9/5.
 */

public class UpdateApi {
    private static UpdateApi ins =null;
    ArrayList<OnDownloadListener> listeners=null;
    private OkHttpClient client;

    private UpdateApi(){}
    public static UpdateApi ins(){
        synchronized (UpdateApi.class){
            if(ins==null)
                ins=new UpdateApi();
            return ins;
        }
    }
    public void addDownloadListener(OnDownloadListener listener){
        if(listeners==null){
            listeners=new ArrayList();
        }
        listeners.add(listener);

    }
    /**
     * 下载App
     * @param url
     * @param listener
     */
    public void downloadApp(String url, final OnDownloadListener listener) {
        addDownloadListener(listener);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String apkName=url.substring(url.lastIndexOf('/'),url.length());
        final File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), apkName);

        client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new DownloadProgressResponseBody(originalResponse.body(), new DownloadProgressListener() {
                            @Override
                            public void update(long bytesRead, long contentLength, boolean done, String path) {
                                if(listeners!=null) {
                                    //NO UI THREAD
                                    for (OnDownloadListener listener1 : listeners) {
                                        listener1.update(bytesRead, contentLength, done, path);
                                    }
                                }
                            }
                        })).build();
            }
        }).build();
        //增加拦截器
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(outputFile);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if(listeners!=null) {
                        for (OnDownloadListener listener1 : listeners) {
                            listener1.update(current, total, true, outputFile.getPath());
                        }
                    }
                } catch (IOException e) {
                    Log.i("TAG",e.toString());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 检查更新情况
     * @param url
     * @param callback
     */
    public void checkUpdate(String url,UIupdateCheckCallBack callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
    public void release(){
        listeners.clear();
        listeners=null;
        client=null;
        ins=null;
    }
}
