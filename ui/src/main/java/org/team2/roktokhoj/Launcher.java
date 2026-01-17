package org.team2.roktokhoj;

import javafx.application.Application;

public class Launcher {
    static void main(String[] args) throws Exception {
        ThreadPool.init(-1);
        Application.launch(MainApplication.class, args);
        ThreadPool.close();
    }
}
