package ru.itis.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

public class DownloaderTask implements Runnable {
    private URL url;
    private String folder;
    private DownloadTaskInfo progress;
    private String filename;
    private Connection db;

    public DownloaderTask(URL url, String folder, DownloadTaskInfo progress, Connection db) {
        this.url = url;
        this.folder = folder;
        this.progress = progress;
        this.db = db;
        filename = url.getFile().split("/")[url.getFile().split("/").length-1];
        progress.setFilename(filename);
    }

    @Override
    public void run() {
        progress.setThreadName(Thread.currentThread().getName());
        try {
            InputStream in = url.openStream();
            long length = url.openConnection().getContentLengthLong();
            progress.setFilesize(length);
            long downloaded = 0;
            progress.setDownloaded(downloaded);
            int t;
            File f = new File(folder);
            f.mkdirs();
            Path finalFilename = f.toPath().resolve(filename);
            FileOutputStream out = new FileOutputStream(finalFilename.toString());
            while ((t = in.read()) != -1) {
                out.write((byte)t);
                downloaded++;
                progress.setDownloaded(downloaded);
                progress.setProgress((float)downloaded/(float)length);
            }
            PreparedStatement statement =db.prepareStatement("INSERT INTO public.\"Images\" VALUES (?,?,?)");
            statement.setString(1, finalFilename.toAbsolutePath().toString());
            statement.setLong(2, length);
            statement.setTimestamp(3, new Timestamp(new Date().getTime()));
            statement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("error((");
        }
    }
}
