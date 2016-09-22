package me.czmc.library.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadModel implements Parcelable {
    //当前读取字节长度
    public long currentBytes;
    //总字节长度
    public long contentLength;
    //是否读取完成
    public boolean done;

    public String path;

    public int progress;

    public DownloadModel(long currentBytes, long contentLength, boolean done,String path) {
        this.path=path;
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
        this.progress=(int)(100*currentBytes/contentLength);
    }
    public DownloadModel(){}

    protected DownloadModel(Parcel in) {
        currentBytes = in.readLong();
        contentLength = in.readLong();
        done = in.readByte() != 0;
        path = in.readString();
        progress = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(currentBytes);
        dest.writeLong(contentLength);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeString(path);
        dest.writeInt(progress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DownloadModel> CREATOR = new Creator<DownloadModel>() {
        @Override
        public DownloadModel createFromParcel(Parcel in) {
            return new DownloadModel(in);
        }

        @Override
        public DownloadModel[] newArray(int size) {
            return new DownloadModel[size];
        }
    };

    @Override
    public String toString() {
        return "DownloadModel{" +
                "currentBytes=" + currentBytes +
                ", contentLength=" + contentLength +
                ", done=" + done +
                '}';
    }
}