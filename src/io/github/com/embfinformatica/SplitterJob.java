package io.github.com.embfinformatica;

public class SplitterJob {


    public static interface Split {

        public void onBegin(String originName, long FileSize);

        public void onWrittingFile(long currentByte, int multiply, long fileSize);

        public void onEnd();
    }

    public static interface Merge {

        public void onBegin(String originName, long fileSize);

        public void onMergin(String currentFileName,long pieceInBytes, long fileSizeTotal);

        public void onEnd();

    }
}
