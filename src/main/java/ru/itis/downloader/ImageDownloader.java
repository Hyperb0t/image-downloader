package ru.itis.downloader;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader {
    private Connection db;
    private ExecutorService executorService;
    private List<DownloadTaskInfo> infos;
    private static ImageDownloader instance;

    private ImageDownloader(String jdbcUsername, String jdbcPassword, String jdbcUrl, int threads)
            throws SQLException {
        db = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        executorService = Executors.newFixedThreadPool(threads);
        infos = new ArrayList<DownloadTaskInfo>();
    }

    public void load(URL url, String folder) {
        DownloadTaskInfo info = new DownloadTaskInfo();
        Runnable task = new DownloaderTask(url, folder, info, db);
        executorService.submit(task);
        infos.add(info);
    }

    public static ImageDownloader create (String jdbcUsername, String jdbcPassword, String jdbcUrl, int threads)
            throws SQLException{
        if(instance == null) {
            instance = new ImageDownloader(jdbcUsername, jdbcPassword, jdbcUrl, threads);
            return instance;
        }
        else {
            return instance;
        }
    }

    public List<DownloadTaskInfo> getInfos() {
        return infos;
    }

    public void deleteFinishedInfos() {
        for(int i = 0; i < infos.size(); i++) {
            if(infos.get(i).getProgress() == 1) {
                infos.remove(i);
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
