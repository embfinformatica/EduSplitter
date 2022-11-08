package io.github.com.embfinformatica;

public interface SplitterJob {
    public void onBegin();

    public void onWrittingFile();

    public void onFinished();
}
