package ru.itis.downloader;

import java.util.Objects;

public class DownloadTaskInfo {
    private volatile float progress;
    private volatile String threadName;
    private volatile String filename;
    private volatile long downloaded;
    private volatile long filesize;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float f) {
        this.progress = f;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DownloadTaskInfo() {
        this.progress = 0;
        this.threadName = "unknown thread";
        this.filename = "unknown";
        this.downloaded = 0;
        this.filesize = 0;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public DownloadTaskInfo(float progress, String threadName, String filename, long downloaded, long filesize) {
        this.progress = progress;
        this.threadName = threadName;
        this.filename = filename;
        this.downloaded = downloaded;
        this.filesize = filesize;
    }
}
