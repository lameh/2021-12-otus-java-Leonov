package ru.otus.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class NumThread extends Thread {

    public static final Logger logger = LoggerFactory.getLogger(NumThread.class);
    private final CommonData commonData;

    public NumThread(String name, CommonData commonData) {
        super(name);
        this.commonData = commonData;
    }

    public NumThread(CommonData commonData) {
        this.commonData = commonData;
    }

    @Override
    public void run() {
        var first = 1;
        var last = 10;
        var inc = -1;
        var curNum = first;

        while (!isInterrupted()) {
            try {
                synchronized (commonData) {
                    while (Objects.equals(getName(), commonData.getUsedThread())) {
                        commonData.wait();
                    }
                    logger.info("{} -> {}", getName(), curNum);
                    commonData.setUsedThread(getName());
                    commonData.notifyAll();
                }
                if (curNum == first || curNum == last) {
                    inc = -inc;
                }
                curNum += inc;
                sleep(2000);
            } catch (InterruptedException ie) {
                logger.error(ie.getMessage(), ie);
                interrupt();
            }
        }
    }
}
