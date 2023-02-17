package ru.otus.threads;

public class CommonData {

    private String usedThread;

    public CommonData(String usedThread) {
        this.usedThread = usedThread;
    }

    public String getUsedThread() {
        return usedThread;
    }

    public void setUsedThread(String usedThread) {
        this.usedThread = usedThread;
    }
}
