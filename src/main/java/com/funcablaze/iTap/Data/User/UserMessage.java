package com.funcablaze.iTap.Data.User;

import com.funcablaze.iTap.Net.TCP.Client;

import java.io.File;

public class UserMessage {

    private String ip;
    private File FilePath;

    public UserMessage(String ip) {
        this.ip = ip;
        this.FilePath = new File("./E.Tick/UserMessage/" + ip + ".MESSAGE");
    }

    public boolean DELETE_USER_MESSAGE() {
        if (FilePath.exists()) {
            return FilePath.delete();
        } else {
            return true;
        }
    }

    public void del(String message) {

    }

    public void add(Client.MessageType type, String message) {
        if (FilePath.exists()) {
            // TODO: add message to file
        } else {
            create();
        }
    }

    private boolean create() {
        try {
            if (FilePath.mkdirs()) {
                return FilePath.createNewFile();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
