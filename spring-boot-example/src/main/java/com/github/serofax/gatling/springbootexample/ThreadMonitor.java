package com.github.serofax.gatling.springbootexample;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ThreadMonitor implements Runnable {
    private final String maxWorkerThreads;

    public ThreadMonitor(@Value("${server.tomcat.max-threads:200}") String maxWorkerThreads) {
        this.maxWorkerThreads = maxWorkerThreads;
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

            long httpWorkersCount = threadSet.stream().filter(t -> t.getName().contains("http-nio-") && t.getName().contains("exec"))
                .count();
            System.out.println("Http worker threads: " + httpWorkersCount + " of max " + maxWorkerThreads + " (" + threadSet.size()
                + " threads in total)");
            Thread.sleep(1000);
        }

    }

    public void startMonitor() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("Monitor-thread");
        thread.start();
    }
}
