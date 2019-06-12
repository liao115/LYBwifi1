package com.alialiso.wifi;

import java.util.concurrent.Callable;

public class CrackThread implements Callable<Boolean> {

    private String wifiName;
    private String password;

    public CrackThread(String wifiName, String password) {
        this.wifiName = wifiName;
        this.password = password;
    }

    public Boolean call() {
        try {
            if (CMDUtil.connect(wifiName)) {
                Thread.sleep(50);
                if (CMDUtil.ping()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
