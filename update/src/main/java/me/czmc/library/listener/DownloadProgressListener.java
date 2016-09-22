package me.czmc.library.listener;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done,String path);
}