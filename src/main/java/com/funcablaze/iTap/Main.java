package com.funcablaze.iTap;

import com.funcablaze.iTap.Net.TCP.Client;
import com.funcablaze.iTap.Elment.Window.Chat;
import com.funcablaze.iTap.Elment.Window.UserList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static String ServerIP = "localhost";
    public static int ServerPort = 9971;
    public static Image AppIcon;
    public static final String AppName = "E.Desk";
    public static UserList UserList;
    public static Map<String, Chat> UserWindow = new HashMap<>();
    public static boolean Notice = false;
    public static Client client;

    public static void main(String[] args) throws IOException {
        InputStream is = Main.class.getResourceAsStream("/icon.png");
        BufferedImage bufferedImage = null;
        if (is != null) {
            bufferedImage = ImageIO.read(is);
        }
        AppIcon = bufferedImage;
        login();
    }

    /**
     * 登录
     */
    private static void login() {
        setChatListener();
//        client.sendMessage("SERVER", Client.MessageType.SERVER, "close");
        start();
    }

    /**
     * 启动
     */
    private static void start() {
//        setChatListener();
        openMainWindow();
    }

    private static void ServerToHere(String m) {
        System.out.println(m);
    }

    /**
     * 设置服务端信息监听
     */
    private static void setChatListener() {
        client = new Client();
        client.linkServer(ServerIP, ServerPort, (from, type, message) -> {
            switch (type) {
                case TEXT -> com.funcablaze.iTap.Elment.Window.UserList.sendNotice(from, message.length() > 20 ? message.substring(19) + "…" : message, TrayIcon.MessageType.NONE);
                case CARD -> com.funcablaze.iTap.Elment.Window.UserList.sendNotice(from, "[卡片]", TrayIcon.MessageType.NONE);
                case FILE -> com.funcablaze.iTap.Elment.Window.UserList.sendNotice(from, "[文件]", TrayIcon.MessageType.NONE);
                case IMAGE -> com.funcablaze.iTap.Elment.Window.UserList.sendNotice(from, "[图片]", TrayIcon.MessageType.NONE);
                case SERVER -> ServerToHere(message);
                case INFO -> System.out.println(from + ":" + type + ":" + message);
            }
        });
    }

    /**
     * 退出程序
     */
    public static void exitApp() {
        client.sendMessage("SERVER", Client.MessageType.SERVER, "close");
        System.exit(0);
    }

    /**
     * 打开用户列表窗口
     */
    public static void openMainWindow() {
        UserList = new UserList(AppName, 0, 0, 230, 400);
    }

    /**
     * 打开聊天窗口
     *
     * @param Name 用户名
     * @param ip   用户IP
     */
    public static void openChatWindow(String Name, String ip) {
        if (UserWindow.containsKey(ip)) {
            UserWindow.get(ip).setVisible(true);
        } else {
            Chat user = new Chat(Name, ip, 500, 100, 500, 650);
            UserWindow.put(ip, user);
        }
    }
}