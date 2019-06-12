package com.alialiso.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.List;

public class AppMain {

    private static Logger logger = Logger.getLogger("AppMain");

    public static String wifiName = "609";//wifi名称
    public static Integer threadNum = 4;//线程数
    public static Long speed = 100L;//执行速度

    public static final String APP_DIR = System.getProperty("user.dir");//app地址
    private ExecutorService executorService = Executors.newFixedThreadPool(threadNum);//多线程执行任务

    //入口
    public static void main(String[] args) {
        //开启应用页面 并获取wifi列表

        //获取用户输入 wifi名称 线程数 执行速率

        //每次读取密码一千个密码
        long t1 = System.currentTimeMillis();
        //遍历密码，生成请求xml，发起cmd请求
        boolean next = true;
        int pageSize = 1;
        outer:
        while(next){
            List<String> passwords = FileUtils.readLine(APP_DIR+"/password.txt", pageSize * 1000, (pageSize + 1) * 1000 - 1);
            if (passwords != null && passwords.size() != 0){
                for (String password : passwords) {
                    String profileContent = Profile.PROFILE.replace(Profile.WIFI_NAME, wifiName);
                    profileContent = profileContent.replace(Profile.WIFI_PASSWORD, password);
                    System.out.println(profileContent);
                    AppMain appMain = new AppMain();
                    boolean isPassword = appMain.createThread(wifiName, profileContent);
                    if (isPassword){
                        next = false;
                        System.out.println("密码是："+wifiName);
                        break outer;
                    }
                    return;
                }
            }else{
                next = false;
            }
        }
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

    public boolean createThread(String wifiName,String profileContent){
        CrackThread crackThread = new CrackThread(wifiName,profileContent);
        Future<Boolean> isPassword = executorService.submit(crackThread);
        try {
            if (isPassword.get()) {
                return isPassword.get();
            }
        } catch (Exception e) {
            System.out.println("校验出错 passord=>" + profileContent);
        }
        return false;
    };

}
