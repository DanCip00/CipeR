package it.ciper.api.interfacce;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.PriceAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.json.JsonManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public interface ProductInterfaceApi {

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

    //                                  PRODOTTI

    static List<ProductAPI> getAllProduct(String apiKey){
        String buf = null;
        GetAllProducts getAllProducts = new GetAllProducts();
        getAllProducts.setParams(apiKey);
        Future<String> ris = executor.submit(getAllProducts);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProductAPI[] prodotti = JsonManager.parseJsonClass(buf,ProductAPI[].class);
        if (prodotti.length==0)
            return null;
        return Arrays.stream(prodotti).collect(Collectors.toList());
    }
    class GetAllProducts implements Callable<String> {
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
                    .url(serverDomain+"/product/getAllProduct")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    static List<PriceAPI> getAllPrices(String apiKey,String productCod){
        String buf = null;
        GetAllPrices getAllPrices = new GetAllPrices();
        getAllPrices.setParams(apiKey, productCod);
        Future<String> ris = executor.submit(getAllPrices);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PriceAPI[] prezzi = JsonManager.parseJsonClass(buf,PriceAPI[].class);
        if (prezzi.length==0)
            return null;
        return Arrays.stream(prezzi).collect(Collectors.toList());
    }
    class GetAllPrices implements Callable<String> {
        private String apiKey, productcod;

        void setParams(String apiKey, String productCod) {
            this.apiKey = apiKey;
            this.productcod = productCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"apiKey\" : \""+apiKey+"\",\n    \"productCod\" : \""+productcod+"\"\n}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/product/getAllPrices")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static Boolean addToCart(CarrelloAPI cart, ProductAndPriceAPI pp, Integer quantity){
        return addToCart(cart.getUsernamecreator(), pp.getProductcod(), cart.getCartcod(), pp.getPrice().getSellercod(),quantity);
    }
    static Boolean addToSherCart(String apiKey,String cartCod, ProductAndPriceAPI pp, Integer quantity){
        return addToCart(apiKey, pp.getProductcod(), cartCod, pp.getPrice().getSellercod(),quantity);
    }
    static Boolean addToCart(CarrelloAPI cart, ProductAPI product, PriceAPI price, Integer quantity){
        if (product.getProductcod().compareTo(price.getProductcod())!=0)
            return false;
        return addToCart(cart.getUsernamecreator(), product.getProductcod(), cart.getCartcod(), price.getSellercod(),quantity);

    }
    static Boolean addToSherCart(String apiKey,String cartCod, ProductAPI product, PriceAPI price, Integer quantity){
        if (product.getProductcod().compareTo(price.getProductcod())!=0)
            return false;
        return addToCart(apiKey, product.getProductcod(), cartCod, price.getSellercod(),quantity);

    }
    static Boolean addToCart(String apiKey, String productCod, String cartCod, Integer sellerCod,Integer quantity ){
        String buf = null;
        AddToCart addToCart = new AddToCart();
        addToCart.setParams(apiKey, productCod,cartCod, sellerCod, quantity);
        Future<String> ris = executor.submit(addToCart);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("true")==0)
            return true;
        return false;
    }
    class AddToCart implements Callable<String> {
        private String apiKey, productcod, cartcod;
        private Integer sellerCod, quantity;

        void setParams(String apiKey, String productCod, String cartCod, Integer sellerCod,Integer quantity ) {
            this.apiKey = apiKey;
            this.productcod = productCod;
            this.cartcod = cartCod;
            this.sellerCod = sellerCod;
            this.quantity = quantity;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"productCod\" : \""+productcod+"\",\n" +
                    "    \"cartCod\" : \""+cartcod+"\",\n" +
                    "    \"sellerCod\" : "+sellerCod+",\n" +
                    "    \"quantity\" : "+quantity+"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/product/addToCart")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static Boolean remFromCart(String apiKey, CarrelloAPI cart, ProductAndPriceAPI pp){
        return remFromCart(apiKey, pp.getProductcod(), cart.getCartcod(), pp.getPrice().getSellercod());
    }
    static Boolean remFromCart(String apiKey,CarrelloAPI cart, ProductAPI product, PriceAPI price){

        if (product.getProductcod().compareTo(price.getProductcod())!=0)
            return false;
        return remFromCart(apiKey, product.getProductcod(), cart.getCartcod(), price.getSellercod());

    }
    static Boolean remFromCart(String apiKey, String productCod, String cartCod, Integer sellerCod){
        String buf = null;
        RemFromCart remFromCart = new RemFromCart();
        remFromCart.setParams(apiKey, productCod,cartCod, sellerCod);
        Future<String> ris = executor.submit(remFromCart);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("true")==0)
            return true;
        return false;
    }
    class RemFromCart implements Callable<String> {
        private String apiKey, productcod, cartcod;
        private Integer sellerCod;

        void setParams(String apiKey, String productCod, String cartCod, Integer sellerCod) {
            this.apiKey = apiKey;
            this.productcod = productCod;
            this.cartcod = cartCod;
            this.sellerCod = sellerCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"productCod\" : \""+productcod+"\",\n" +
                    "    \"cartCod\" : \""+cartcod+"\",\n" +
                    "    \"sellerCod\" : "+sellerCod+"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/product/remFromCart")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    static ProductAPI getProduct(String apiKey, String productCod){
        String buf = null;
        GetProducts getProducts = new GetProducts();
        getProducts.setParams(apiKey, productCod);
        Future<String> ris = executor.submit(getProducts);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProductAPI[] prodotti = JsonManager.parseJsonClass(buf,ProductAPI[].class);
        if (prodotti.length==0)
            return null;
        return Arrays.stream(prodotti).collect(Collectors.toList()).get(0);
    }
    class GetProducts implements Callable<String> {
        private String apiKey, productCod;

        void setParams(String apiKey, String productCod) {
            this.apiKey = apiKey;
            this.productCod = productCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"apiKey\" : \""+apiKey+"\",\n    \"productcod\" : \""+productCod+"\"\n}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/product/getAllProduct")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static PriceAPI getPrice(String apiKey, String productCod, String sellerCod){
        String buf = null;
        GetPrice GetPrice = new GetPrice();
        GetPrice.setParams(apiKey, productCod, sellerCod);
        Future<String> ris = executor.submit(GetPrice);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PriceAPI[] prodotti = JsonManager.parseJsonClass(buf,PriceAPI[].class);
        if (prodotti.length==0)
            return null;
        return Arrays.stream(prodotti).collect(Collectors.toList()).get(0);
    }
    class GetPrice implements Callable<String> {
        private String apiKey, productCod, sellerCod;

        void setParams(String apiKey, String productCod, String sellerCod) {
            this.apiKey = apiKey;
            this.sellerCod = sellerCod;
            this.productCod = productCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"productCod\" : \""+productCod+"\",\n" +
                    "    \"sellerCod\" : "+sellerCod+"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/product/getPrice")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

}
