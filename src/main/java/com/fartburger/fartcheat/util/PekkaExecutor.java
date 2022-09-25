package com.fartburger.fartcheat.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PekkaExecutor {

    // more stolen code from meteor client :)))

    public static ExecutorService executor;

    @PreInit
    public static void init() {executor = Executors.newSingleThreadExecutor();}

    public static void execute(Runnable task) { executor.execute(task);}
}
