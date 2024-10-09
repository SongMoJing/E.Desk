package com.funcablaze.iTap.Elment.Window;

import com.funcablaze.iTap.Net.TCP.Client;
import com.funcablaze.iTap.Main;

import javax.swing.*;
import java.awt.*;

public class Chat extends JFrame {

    private final String Name;
    private final String ip;
    private JList<UserMessage> messageList;

    public Chat(String Name, String ip, int x, int y, int width, int height) {
        setTitle(Name);
        this.Name = Name;
        this.ip = ip;
        setBounds(x, y, width, height);
        setIconImage(Main.AppIcon);
        initMessage();
        updateMessage();
        setVisible(true);
    }

    public void updateMessage() {
        // TODO: 2022/12/9 更新消息
    }

    private void initMessage() {
        messageList = new JList<>();
        messageList.setListData(new UserMessage[]{new UserMessage(Client.MessageType.TEXT, "你好")});
        messageList.setCellRenderer(new JListRenderer());
        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Main.client.sendMessage(ip, Client.MessageType.TEXT, "你好");
    }

    static class JListRenderer extends JPanel implements ListCellRenderer {
        private String name;
        private int i;
        private boolean isSel = false;
        private boolean isFoc = false;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            name = value.toString();
            i = index;
            isSel = isSelected;
            isFoc = cellHasFocus;
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 30);
        }

        //绘制自定义控件
        @Override
        public void paint(Graphics g) {
            if (name != null) {
                if (isSel) {
                    g.setColor(new Color(147, 227, 232, 155));
                    g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font("宋体", Font.PLAIN, 15));
                g.drawString(name, 10, 20);
            }
        }
    }

    static class UserMessage {
        private Client.MessageType type;
        private String message;

        public UserMessage(Client.MessageType type, String message) {
            this.type = type;
            this.message = message;
        }
    }
}
