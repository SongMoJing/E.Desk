package com.funcablaze.iTap.Net.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final Map<String, Socket> clients = new HashMap<>();

    public static void main(String[] args) {
        new Server(8080, 10);
    }

    public Server(int port, int maxClient) {
        try {
            ServerSocket serverSocket = new ServerSocket(port, maxClient);
            new Thread(() -> {
                while (true) {
                    try {
                        // 等待客户端连接
                        Socket clientSocket = serverSocket.accept();
                        this.clients.put(clientSocket.getInetAddress().getHostAddress(), clientSocket);
                        System.out.println("客户端已连接：" + clientSocket.getInetAddress().getHostAddress());
                        InputStream inputStream = clientSocket.getInputStream();
                        new Thread(() -> {
                            while (true) {
                                byte[] buffer = new byte[1024];
                                int bytesRead = 0;
                                try {
                                    bytesRead = inputStream.read(buffer);
                                    receiveFrom(clientSocket, new String(buffer, 0, bytesRead));
                                } catch (IOException e) {
                                    break;
                                }
                            }
                        }).start();
                    } catch (IOException ignored) {
                    }
                }
            }).start();
        } catch (IOException ignored) {
        }
    }

    private void receiveFrom(Socket client, String message) {
        // Send:[ip]:[type]:[message]
        if (message.startsWith("Send:")) {
            String[] mi = message.split(":");
            if (clients.containsKey(mi[1])) {
                if (mi.length == 4) {
                    sendTo(mi[1], mi[2], mi[3]);
                }
            } else {
                if (Objects.equals(mi[1], "SERVER") & Objects.equals(mi[2], "DEBUG")) {
                    System.out.println("DEBUG [" + client.getInetAddress().getHostAddress() + "]: " + mi[3]);
                } else if (Objects.equals(mi[1], "SERVER") & Objects.equals(mi[2], "server")) {
                    switch (mi[3]) {
                        case "close":
                            closeAt(client.getInetAddress().getHostAddress());
                            break;
                    }
                } else {
                    sendTo(client.getInetAddress().getHostAddress(), "info", "发送失败，对方为离线状态");
                }
            }
        }
    }

    public void sendTo(String ip, String Type, String message) {
        try {
            Socket clientSocket = this.clients.get(ip);
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(("Send:" + ip + ":" + Type + ":" + message).getBytes());
        } catch (IOException ignored) {
        }
    }

    public boolean closeAt(String ip) {
        try {
            Socket clientSocket = this.clients.get(ip);
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(("BREAK_LINKS").getBytes());
            System.out.println("客户端已断开：" + ip);
            clientSocket.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}