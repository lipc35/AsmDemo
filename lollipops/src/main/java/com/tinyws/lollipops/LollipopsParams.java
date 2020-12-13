package com.tinyws.lollipops;

/**
 * @author : tiny
 * @version :
 * @description : LollipopsParams
 * @date : 2020/12/13 12:31 AM
 */
public class LollipopsParams {
    private boolean onlyPrintMain;
    private int edge = -1;
    private String logTag = "";

    public LollipopsParams onlyPrintMain() {
        onlyPrintMain = true;
        return this;
    }

    public LollipopsParams setEdge(int edge) {
        this.edge = edge;
        return this;
    }

    public LollipopsParams setLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }

    public boolean isOnlyPrintMain() {
        return onlyPrintMain;
    }

    public int getEdge() {
        return edge;
    }

    public String getLogTag() {
        return logTag;
    }
}
