package io.github.com.embfinformatica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Splitter {
    private static Splitter splitter;

    public static final long _1KB = 1024;
    public static final long _1MB = 1024 * 1024;
    public static final long _5MB = _1MB * 5;
    public static final long _10MB = _1MB * 10;
    public static final long _25MB = _1MB * 25;
    public static final long _50MB = _1MB * 50;
    public static final long _100MB = _1MB * 100;
    public static final long _200MB = _1MB * 200;
    public static final long _300MB = _1MB * 300;
    public static final long _400MB = _1MB * 400;
    public static final long _500MB = _1MB * 500;
    public static final long _1GB = 1073741824;
    public static final long _2GB = _1GB * 2;
    public static final long _3GB = _1GB * 3;

    private Splitter(){};

    public static Splitter getInstance() {
        if(splitter == null) {
            return new Splitter();
        } else {
            return splitter;
        }

    }

    public void makeInPieces(String filePath, long limitInBytes, String splittedPath, int factorOfMultiplication) throws SplitterException {

        if(factorOfMultiplication < 0 || factorOfMultiplication > 7) {
            throw new SplitterException("The factor of Multiplication parameter must be a number between 0 and 7");
        }

        int multiply = (factorOfMultiplication == 0 || factorOfMultiplication == 1) ? 1 : 10 * factorOfMultiplication;

        File file = new File(filePath);
        byte[] bytes = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);

                byte[] bs = new byte[multiply];

                int fileCounter = 0;
                long currentByte = 0;
                FileOutputStream outputStream = new FileOutputStream(splittedPath + "/" + file.getName() + ".edusplitter.999" + fileCounter);
                DataOutputStream dOut = new DataOutputStream(outputStream);

                while (dataInputStream.read(bs) != -1) {

                    if(currentByte * multiply < limitInBytes) {
                        dOut.write(bs);
                        dOut.flush();
                        currentByte++;

                    } else {
                        dOut.close();
                        dOut = null;
                        outputStream.close();
                        outputStream = null;
                        fileCounter++;
                        outputStream = new FileOutputStream(splittedPath + "/" + file.getName() + ".edusplitter.999" + fileCounter);
                        dOut = new DataOutputStream(outputStream);
                        dOut.write(bs);
                        dOut.flush();
                        currentByte = 0;
                    }

                }

                dataInputStream.close();
                fileInputStream.close();

            } catch (Exception exception) {
                throw new SplitterException("Failed to split file");
            }
    }

    public void makeInPieces(String filePath, long limitInBytes, String splittedPath, int factorOfMultiplication, SplitterJob.Split split) throws SplitterException {

        if(factorOfMultiplication < 0 || factorOfMultiplication > 7) {
            throw new SplitterException("The factor of Multiplication parameter must be a number between 0 and 7");
        }

        int multiply = (factorOfMultiplication == 0 || factorOfMultiplication == 1) ? 1 : 10 * factorOfMultiplication;

        File file = new File(filePath);
        byte[] bytes = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);

            byte[] bs = new byte[multiply];

            int fileCounter = 0;
            long currentByte = 0;
            FileOutputStream outputStream = new FileOutputStream(splittedPath + "/" + file.getName() + ".edusplitter.999" + fileCounter);
            DataOutputStream dOut = new DataOutputStream(outputStream);

            split.onBegin(file.getPath(),file.length());

            while (dataInputStream.read(bs) != -1) {

                if(currentByte * multiply < limitInBytes) {
                    dOut.write(bs);
                    dOut.flush();
                    currentByte++;

                } else {
                    dOut.close();
                    dOut = null;
                    outputStream.close();
                    outputStream = null;
                    fileCounter++;
                    outputStream = new FileOutputStream(splittedPath + "/" + file.getName() + ".edusplitter.999" + fileCounter);
                    dOut = new DataOutputStream(outputStream);
                    dOut.write(bs);
                    dOut.flush();
                    currentByte = 0;
                }

                split.onWrittingFile(currentByte,multiply,file.length());

            }

            dataInputStream.close();
            fileInputStream.close();

            split.onEnd();

        } catch (Exception exception) {
            throw new SplitterException("Failed to split file");
        }
    }

    public void restoreFromPieces(List<File> files, int factorOfMultiplication) throws SplitterException {

        if(factorOfMultiplication < 0 || factorOfMultiplication > 7) {
            throw new SplitterException("The factor of Multiplication parameter must be a number between 0 and 7");
        }

        int multiply = (factorOfMultiplication == 0 || factorOfMultiplication == 1) ? 1 : 10 * factorOfMultiplication;

        List<String> paths = new ArrayList<>();
        long total = 0;
        long totalFromPieces = 0;

        for (int i = 0; i < files.size(); i++) {
            paths.add(files.get(i).getPath());
            total += files.get(i).length();
        }

        List<String> sortedPaths = paths.stream().sorted().collect(Collectors.toList());

        String originName = sortedPaths.get(0).split("\\.edusplitter")[0];

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(originName);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            for(String cur : sortedPaths) {

                totalFromPieces += new File(cur).length();

                FileInputStream inCur = new FileInputStream(cur);
                DataInputStream dInCur = new DataInputStream(inCur);
                byte[] bytes = new byte[multiply];

                while (dInCur.read(bytes) != -1) {
                    dataOutputStream.write(bytes);
                    dataOutputStream.flush();
                }

                dInCur.close();
                inCur.close();
            }

            dataOutputStream.close();
            fileOutputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void restoreFromPieces(List<File> files, int factorOfMultiplication, SplitterJob.Merge merge) throws SplitterException {

        if(factorOfMultiplication < 0 || factorOfMultiplication > 7) {
            throw new SplitterException("The factor of Multiplication parameter must be a number between 0 and 7");
        }

        int multiply = (factorOfMultiplication == 0 || factorOfMultiplication == 1) ? 1 : 10 * factorOfMultiplication;

        List<String> paths = new ArrayList<>();
        long total = 0;
        long totalFromPieces = 0;

        for (int i = 0; i < files.size(); i++) {
            paths.add(files.get(i).getPath());
            total += files.get(i).length();
        }

        List<String> sortedPaths = paths.stream().sorted().collect(Collectors.toList());

        String originName = sortedPaths.get(0).split("\\.edusplitter")[0];

        merge.onBegin(originName,total);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(originName);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            for(String cur : sortedPaths) {

                totalFromPieces += new File(cur).length();

                FileInputStream inCur = new FileInputStream(cur);
                DataInputStream dInCur = new DataInputStream(inCur);
                byte[] bytes = new byte[multiply];

                while (dInCur.read(bytes) != -1) {
                    dataOutputStream.write(bytes);
                    dataOutputStream.flush();
                }

                dInCur.close();
                inCur.close();

                merge.onMergin(cur,totalFromPieces,total);
            }

            dataOutputStream.close();
            fileOutputStream.close();

            merge.onEnd();

        } catch (Exception ex) {
            throw new SplitterException("Merge file error");
        }
    }
}
