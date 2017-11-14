package com.zhaoqi.psp.upload.listener;

/**
 * Created by qi on 17-11-14.
 */
public interface  Listener {

    public <T> void onStart(T t);

    public <T> void onRead(T t);

    public <T> void onUpload(T t);

    public <T> void onPartDone(T t);

    public void onNotify();

    public <T> void onFail(T t);

    public void onSuccess();

    public static class DefaultInstance implements Listener {

        @Override
        public <T> void onStart(T t) {
            onMessage("Start uploading " + t + " part(s). ");
        }

        @Override
        public <T> void onRead(T t) {
            onMessage("Reading part " + t + " .");
        }

        @Override
        public <T> void onUpload(T t) {
            onMessage("Uploading part " + t + " .");
        }

        @Override
        public <T> void onPartDone(T t) {
            onMessage("Part " + t + " is done. ");
        }

        @Override
        public void onNotify() {
            onMessage(" Notifying .");
        }

        @Override
        public <T> void onFail(T t) {
            onMessage(t + " uploading fail.");
        }

        @Override
        public void onSuccess() {
            onMessage("uploading success .");
        }

        protected void onMessage(String msg) {
            System.out.println(msg);
        }
    }
}
