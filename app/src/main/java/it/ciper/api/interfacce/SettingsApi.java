package it.ciper.api.interfacce;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface SettingsApi {
    String serverDomain = "http://192.168.1.107:5000";
    ExecutorService executor = Executors.newWorkStealingPool();
    String apiKey = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";


    static String listToString(List<String> list){
        StringBuffer buf = new StringBuffer();
        buf.append("[ ");
        list.stream().forEach(s->buf.append("\""+s+"\", "));
        return buf.append(" ] ").toString();
    }
}
