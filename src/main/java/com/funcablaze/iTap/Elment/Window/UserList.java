package com.funcablaze.iTap.Elment.Window;

import com.funcablaze.iTap.Data.User.UserJson;
import com.funcablaze.iTap.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;

public class UserList extends JFrame{
    private static SystemTray st;
    private static PopupMenu pm;
    private static TrayIcon trayIcon;

    public UserList(String title, int x, int y, int width, int height) {
        setTitle(title);
        setBounds(x, y, width, height);
        bindSystemTray();
        setIconImage(Main.AppIcon);
        addList();
        setVisible(true);
    }

    private void addList() {
        Map<String, String[]> userList = UserJson.readUserJson();
        String[] ls = new String[userList.size()];
        int i = 0;
        for (Map.Entry<String, String[]> entry : userList.entrySet()) {
            ls[i] = entry.getKey();
            i++;
        }
        JList friendList = new JList(ls);
        friendList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int i = friendList.getSelectedIndex();
                    Main.openChatWindow(ls[i], userList.get(ls[i])[1]);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        friendList.setCellRenderer(new JListRenderer());
        JScrollPane fl = new JScrollPane(friendList);
        fl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//        fl.
        add(fl);
    }

    private void bindSystemTray() {
        if (SystemTray.isSupported()) {
            st = SystemTray.getSystemTray();
            pm = new PopupMenu();
            MenuItem open = new MenuItem("Open UserList");
            open.addActionListener(e -> {
                if (Main.UserList == null) {
                    Main.openMainWindow();
                } else if (!isVisible()) {
                    setVisible(true);
                }
            });
            MenuItem notification = new CheckboxMenuItem("Notification Info", Main.Notice);
            notification.addActionListener(e -> Main.Notice = !Main.Notice);
            MenuItem exitMenu = new MenuItem("Exit App");
            exitMenu.addActionListener(e -> Main.exitApp());
            pm.add(open);
            pm.add(notification);
            pm.addSeparator();
            pm.add(exitMenu);
            trayIcon = new TrayIcon(Main.AppIcon, Main.AppName, pm);
            trayIcon.setImageAutoSize(true);
            try {
                st.add(trayIcon);
            } catch (AWTException ignored) {
            }
        }
    }

    public static void sendNotice(String Title, String Content, TrayIcon.MessageType type) {
        if (Main.Notice) {
            trayIcon.displayMessage(Title, Content, type);
        }
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
}
