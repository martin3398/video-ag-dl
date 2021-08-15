package de.martin3398.videoagdl;

import de.martin3398.videoagdl.download.VideoAgDownloader;
import de.martin3398.videoagdl.exception.VideoAgDlException;

import java.util.Scanner;

public class VideoAgDl {

    public static void main(String[] args) {
        String[] inputParams;
        if (args.length == 2) {
            inputParams = args;
            System.out.println("Using course: " + args[0] + ", quality: " + args[1]);
        } else {
            System.out.println("No input parameters specified:");
            inputParams = readInputParams();
        }

        try {
            VideoAgDownloader downloader = new VideoAgDownloader(inputParams[0], inputParams[1]);
            downloader.run();
        } catch (VideoAgDlException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] readInputParams() {
        String[] res = new String[2];

        Scanner sc = new Scanner(System.in);
        System.out.print("Course: ");
        res[0] = sc.nextLine();
        System.out.print("Quality: ");
        res[1] = sc.nextLine();

        sc.close();
        return res;
    }
}
