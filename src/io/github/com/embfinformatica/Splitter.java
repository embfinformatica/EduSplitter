package io.github.com.embfinformatica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Splitter {
    private static Splitter splitter;

    private Splitter(){};

    public static Splitter getInstance() {
        if(splitter == null) {
            return new Splitter();
        } else {
            return splitter;
        }
    }

    public void makeInPieces(String filePath, int quantityOfPieces, String splittedPath) throws SplitterException {
        File file = new File(filePath);
        long length = file.length();
        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            String newFileName = null;
            long limit = length / quantityOfPieces;
            long count = 0;
            int countInPath = 0;
            byte[] bytes = new byte[1];

            System.out.println("[*] File: " + file.getName());
            System.out.println("[*] Size: " + length);
            System.out.println("[*] Splitting...\n");

            while ((count = fileInputStream.read(bytes)) > 0 && countInPath < quantityOfPieces) {
                newFileName = splittedPath + "/" + file.getName() + ".edusplitter.999" + String.valueOf(countInPath);
                FileOutputStream fileOutputStream = new FileOutputStream(newFileName);
                System.out.println("[*] Writting: " + newFileName);
                while (count <= limit) {
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                    count++;
                }
                countInPath++;
            }
            fileInputStream.close();
            System.out.println("[*] Done");

        } catch (Exception ex) {
            throw new SplitterException("An internal error has ocurred");
        }

    }

    public void makeInPieces(String filePath, int quantityOfPieces, String splittedPath, SplitterJob splitterJob) throws SplitterException {
        File file = new File(filePath);
        long length = file.length();
        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            String newFileName = null;
            long limit = length / quantityOfPieces;
            long count = 0;
            int countInPath = 0;
            byte[] bytes = new byte[1];

            splitterJob.onBegin();

            while ((count = fileInputStream.read(bytes)) > 0 && countInPath < quantityOfPieces) {
                newFileName = splittedPath + "/" + file.getName() + ".edusplitter.999" + String.valueOf(countInPath);
                FileOutputStream fileOutputStream = new FileOutputStream(newFileName);
                splitterJob.onWrittingFile();
                while (count <= limit) {
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                    count++;
                }
                countInPath++;
            }
            fileInputStream.close();

            splitterJob.onFinished();

        } catch (Exception ex) {
            throw new SplitterException("An internal error has ocurred");
        }

    }
}
