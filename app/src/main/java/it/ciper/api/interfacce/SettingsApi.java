package it.ciper.api.interfacce;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface SettingsApi {
    String serverDomain = "http://192.168.1.107:5000";
    ExecutorService executor = Executors.newWorkStealingPool();



    static String listToString(List<String> list){
        StringBuffer buf = new StringBuffer();
        buf.append("[ ");
        list.stream().forEach(s->buf.append("\""+s+"\", "));
        return buf.append(" ] ").toString();
    }
}
