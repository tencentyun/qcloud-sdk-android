package com.tencent.qcloud.network.sonar.dns;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("MissingPermission")
public class Dns {
    public static List<String> getLocalHost() {
        List<String> list = new ArrayList<>();
        String mapsFilename = "/system/etc/hosts";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
