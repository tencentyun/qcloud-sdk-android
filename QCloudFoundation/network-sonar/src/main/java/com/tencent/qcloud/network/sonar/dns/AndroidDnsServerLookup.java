package com.tencent.qcloud.network.sonar.dns;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;

import org.minidns.dnsserverlookup.AbstractDnsServerLookupMechanism;
import org.minidns.util.PlatformDetection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * mindns的默认实现版本太旧，获取不到本地的dns服务器
 * <p>
 * Created by jordanqin on 2024/9/12 14:36.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
public class AndroidDnsServerLookup  extends AbstractDnsServerLookupMechanism {
    public static final int PRIORITY = 1000;
    private final Context context;

    public AndroidDnsServerLookup(Context context) {
        super(AndroidDnsServerLookup.class.getSimpleName(), PRIORITY);
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean isAvailable() {
        return PlatformDetection.isAndroid();
    }

    @Override
    public List<String> getDnsServerAddresses() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Network network = cm.getActiveNetwork();
                LinkProperties linkProperties = cm.getLinkProperties(network);
                List<String> servers = new ArrayList<>();
                for (InetAddress inetAddress: linkProperties.getDnsServers()){
                    servers.add(inetAddress.getHostAddress());
                }
                return servers;
            } catch (Exception e) {
                // we might trigger some problems this way
                LOGGER.log(Level.WARNING, "Exception in findDNSByReflection", e);
            }
        }
        return null;
    }
}
