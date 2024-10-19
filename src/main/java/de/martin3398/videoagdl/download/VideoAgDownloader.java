package de.martin3398.videoagdl.download;

import de.martin3398.videoagdl.exception.VideoAgDlException;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class VideoAgDownloader {

    public static final String VIDEO_AG_URL = "https://video.fsmpi.rwth-aachen.de/";

    private final String course;
    private final String quality;
    private final List<String> videosLinks;

    public VideoAgDownloader(String course, String quality) {
        this.course = course;
        this.quality = quality;
        this.videosLinks = new LinkedList<>();
    }

    public void run() {
        getVideoLinks();
        downloadVideos();
    }

    private void getVideoLinks() {
        String read;

        try {
            URL url = new URL(VIDEO_AG_URL + course);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            read = sb.toString();
        } catch (IOException e) {
            throw new VideoAgDlException("Could not read course page");
        }

        String[] split = read.split("<li");
        for (String e : split) {
            if ((e.contains("SDV") || e.contains("mp4")) && e.contains(quality)) {
                String url = VIDEO_AG_URL + e.substring(e.indexOf("<a href=\"") + 9, e.indexOf(">", 1));
                url = url.substring(0, url.indexOf("\""))
                        .replaceAll("(?<!:)/+", "/");
                videosLinks.add(url);
            }
        }
    }

    private void downloadVideos() {
        for (String video : videosLinks) {
            String[] s = video.split("/");
            try (BufferedInputStream in = new BufferedInputStream(new URL(video).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(s[s.length - 1])) {
                final int bufSize = 65536;
                byte[] dataBuffer = new byte[bufSize];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, bufSize)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new VideoAgDlException("Could not download video: " + video);
            }
            System.out.println("Downloaded: " + video);
        }
    }
}
