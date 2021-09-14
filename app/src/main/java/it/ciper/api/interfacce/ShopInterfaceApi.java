package it.ciper.api.interfacce;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.json.JsonManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public interface ShopInterfaceApi {

    String serverDomain = SettingsApi.serverDomain;
    ExecutorService executor = SettingsApi.executor;
    static class GetBodyBuilder extends Request.Builder {
        public Request.Builder get(RequestBody body) {
            this.post(body);
            try {
                Field field = Request.Builder.class.getDeclaredField("method");
                field.setAccessible(true);
                field.set(this, "GET");
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.println("PORCO DIO");
            }
            return this;
        }
    }



    static List<ShopAPI> getAllShop(String apiKey){
        String buf = null;
        GetAllShop getAllShop = new GetAllShop();
        getAllShop.setParams(apiKey);
        Future<String> ris = executor.submit(getAllShop);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShopAPI[] shops = JsonManager.parseJsonClass(buf,ShopAPI[].class);
        if (shops.length==0)
            return null;
        return Arrays.stream(shops).peek(p->System.out.println("!!!!!!!"+p)).collect(Collectors.toList());
    }
    class GetAllShop implements Callable<String> {
        private String apiKey;

        void setParams(String apiKey) {
            this.apiKey = apiKey;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"apiKey\" : \""+apiKey+"\"\n}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/shop/getAllShop")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}
