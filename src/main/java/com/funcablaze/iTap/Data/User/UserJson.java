package com.funcablaze.iTap.Data.User;

import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserJson {

    private static final File file = new File("./E.Desk/user.json");

    /**
     * 读取user.json文件，返回二维数组
     *
     * @return Map<String: 用户ip, String [ ] : 用户name, 用户iCode> 用户信息
     */
    public static Map<String, String[]> readUserJson() {
        Map<String, String[]> user = new HashMap<>();
        if (file.exists()) {
            try {
                JSONObject usersObject = new JSONObject(getJSON());
                for (String name : JSONObject.getNames(usersObject)) {
                    JSONObject userJson = usersObject.getJSONObject(name);
                    String[] userInfo = new String[]{userJson.getString("iCode"), userJson.getString("ip")};
                    user.put(name, userInfo);
                }
            } catch (Exception ignored) {
            }
            return user;
        } else {
            if (createUserJson()) {
                return readUserJson();
            } else {
                return user;
            }
        }
    }

    /**
     * 从user.json文件中移除用户
     *
     * @param ip 用户ip
     * @return boolean 是否移除成功
     */
    public static boolean removeUserJson(String ip) {
        if (file.exists()) {
            try {
                JSONObject jsonObject = new JSONObject(getJSON());
                if (jsonObject.has(ip)) {
                    jsonObject.remove(ip);
                }
                OutputStream os = new FileOutputStream(file);
                os.write(jsonObject.toString().getBytes());
                os.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            if (createUserJson()) {
                return removeUserJson(ip);
            } else {
                return false;
            }
        }
    }

    /**
     * 添加用户到user.json文件
     *
     * @param ip   用户ip
     * @param name 用户名
     * @return boolean 是否添加成功
     */
    public static boolean addUserJson(String ip, String name) {
        if (file.exists()) {
            try {
                JSONObject jsonObject = new JSONObject(getJSON());
                JSONObject user = new JSONObject();
                user.put("ip", ip);
                user.put("iCode", "123456");
                jsonObject.put(name, user);
                OutputStream os = new FileOutputStream(file);
                os.write(jsonObject.toString().getBytes());
                os.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            if (createUserJson()) {
                return addUserJson(ip, name);
            } else {
                return false;
            }
        }
    }

    /**
     * 创建user.json文件
     *
     * @return boolean 是否创建成功
     */
    public static boolean createUserJson() {
        try {
            file.getParentFile().mkdirs();
            if (file.createNewFile()) {
                OutputStream os = new FileOutputStream(file);
                System.out.println(file.getPath());
                os.write("{}".getBytes());
                os.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 读取文件内容
     *
     * @return String 文件内容
     */
    private static String getJSON() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ignored) {
        }
        return sb.toString();
    }
}
