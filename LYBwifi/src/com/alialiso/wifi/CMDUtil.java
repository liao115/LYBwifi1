package com.alialiso.wifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//存储所有命令
public class CMDUtil {

    public static final String GET_WIFI_LIST = "netsh wlan show networks mode=bssid";//查找wifi列表的命令
    /**
     * 连接wifi
     */
    public static final String CONNECT = "netsh wlan connect name=SSID_NAME";

    /**
     * 列出接口
     */
    public static final String SHOW_INTERFACE = "netsh wlan show interface";

    /**
     * 添加配置文件
     */
    public static final String ADD_PROFILE = "netsh wlan add profile filename=FILE_NAME";

    /**
     * 开启接口
     */
    public static final String INTERFACEC_ENABLE = "netsh interface set interface \"Interface Name\" enabled";

    /**
     * 添加配置文件
     *
     * @param profileName 添加配置文件
     */
    private static boolean addProfile(String profileName) {
        String cmd = ADD_PROFILE.replace("FILE_NAME", profileName);
        List<String> result = execute(cmd, "配置文件路径【p");
        if (result != null && result.size() > 0) {
            if (result.get(0).contains("添加到接口")) {
                return true;
            }
        }
        return false;
    }
    /**
     * 执行器
     *
     * @param cmd      CMD命令
     * @param filePath 需要在哪个目录下执行
     */
    public static List<String> execute(String cmd, String filePath) {
        Process process = null;
        List<String> result = new ArrayList<String>();
        try {
            if (filePath != null) {
                process = Runtime.getRuntime().exec(cmd, null, new File(filePath));
            } else {
                process = Runtime.getRuntime().exec(cmd);
            }
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 连接wifi
     *
     * @param ssid 添加配置文件
     */
    public static boolean connect(String ssid) {
        boolean connected = false;
        String cmd = CONNECT.replace("SSID_NAME", ssid);
        List<String> result = execute(cmd, null);
        if (result != null && result.size() > 0) {
            if (result.get(0).contains("已成功完成")) {
                connected = true;
            }
        }
        return connected;
    }

    /**
     * ping 校验
     */
    public static boolean ping() {
        boolean pinged = false;
        String cmd = "ping www.baidu.com";
        List<String> result = execute(cmd, null);
        if (result != null && result.size() > 0) {
            for (String item : result) {
                if (item.contains("来自")) {
                    pinged = true;
                    break;
                }
            }
        }
        return pinged;
    }

    /**
     * 列出所有信号较好的ssid
     *
     * @return 所有ssid
     */
    public static List<String> listSsid() {
        List<String> wifis = new ArrayList<String>();
        String cmd = GET_WIFI_LIST;
        List<String> result = execute(cmd, null);
        if (result != null && result.size() > 0) {
            System.out.println(result.toString());
        }
        return wifis;
    }
}
