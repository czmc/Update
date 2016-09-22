package me.czmc.updatedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.jpush.android.api.JPushInterface;
import me.czmc.library.core.UpdateInterface;
import me.czmc.library.view.DownloadDialog;

public class MainActivity extends AppCompatActivity {
    DownloadDialog downloadDialog =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.init(getApplicationContext());
        UpdateInterface.init(this);
        UpdateInterface.requestUpdate("http://192.168.0.112:8080/DataTest/test/checkupdate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateInterface.release();
    }
}
