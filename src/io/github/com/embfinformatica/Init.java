package io.github.com.embfinformatica;

public class Init {
    public static void main(String[] args) {
        Splitter splitter = Splitter.getInstance();
        try {
            splitter.makeInPieces(null,5,null);
            //do restoreFromPieces at next Sprint...
        } catch (SplitterException ex) {

        }
    }
}
