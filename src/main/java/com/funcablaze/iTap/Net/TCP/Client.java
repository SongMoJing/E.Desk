package com.funcablaze.iTap.Net.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private MessageListener messageListener;

    public enum MessageType {
        TEXT,
        IMAGE,
        FILE,
        CARD,
        INFO,
        SERVER,
        DEBUG
    }

    public static String MessageType(MessageType type) {
        return switch (type) {
            case TEXT -> "text";
            case IMAGE -> "image";
            case FILE -> "file";
            case CARD -> "card";
            case INFO -> "info";
            case SERVER -> "server";
            case DEBUG -> "DEBUG";
        };
    }

    public static MessageType MessageType(String type) {
        return switch (type) {
            case "text" -> MessageType.TEXT;
            case "image" -> MessageType.IMAGE;
            case "file" -> MessageType.FILE;
            case "card" -> MessageType.CARD;
            case "info" -> MessageType.INFO;
            case "server" -> MessageType.SERVER;
            case "DEBUG" -> MessageType.DEBUG;
            default -> null;
        };
    }

    public boolean linkServer(String serverAddress, int serverPort, MessageListener messageListener) {
        boolean Res;
        try {
            this.messageListener = messageListener;
            clientSocket = new Socket(serverAddress, serverPort);
            Res = true;
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            // 读取服务器响应的数据
            new Thread(() -> {
                while (true) {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    try {
                        bytesRead = inputStream.read(buffer);
                    } catch (IOException ignored) {
                    }
                    String info = new String(buffer, 0, bytesRead);
                    switch (info) {
                        case "BREAK_LINKS" -> close();
                        default -> {
                            String[] res = info.split(":");
                            if (res.length == 4) {
                                this.messageListener.onMessageReceived(res[1], MessageType(res[2]), res[3]);
                            }
                        }
                    }
                }
            }).start();
        } catch (Exception ignored) {
            Res = false;
        }
        return Res;
    }

    public boolean sendMessage(String ip, MessageType type, String message) {
        try {
            message = message.replace(":", "：");
            outputStream.write(("Send:" + ip + ":" + MessageType(type) + ":" + message).getBytes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public interface MessageListener {
        void onMessageReceived(String from, MessageType type, String message);
    }

    public void close() {
        try {
            outputStream.write("close".getBytes());
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }
}
