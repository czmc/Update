package me.czmc.library.entity;

/**
 * Created by czmc on 2016/9/5.
 */

public class CheckResult {
    public int versionCode;
    public String versionName;
    public String describtion;
    public String msg;
    public String downloadUrl;
    public int code;
    public boolean isSuccessed(){
        if(code==200){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", describtion='" + describtion + '\'' +
                ", msg='" + msg + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", code=" + code +
                '}';
    }
}
