package it.ciper.api.interfacce;

import static it.ciper.api.interfacce.SettingsApi.listToString;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.carrello.CartItemAPI;
import it.ciper.data.dataClasses.product.PriceAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.data.dataClasses.shop.ShopCartInfoAPI;

import it.ciper.json.JsonManager;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public interface CarrelliInterfaceApi {

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

    //CARRELLO


    /**
    @return cartCod
     */
    static String addCart(String apiKey, String titolo){

        AddCart addCart = new AddCart();
        addCart.setParams(apiKey, titolo);
        Future<String> ris = executor.submit(addCart);
        try {
            return ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "false";
    }
    class AddCart implements Callable<String>{
        private String apiKey, titolo;

         void setParams(String apiKey, String titolo){
            this.apiKey = apiKey;
            this.titolo = titolo;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"apiKey\" : \""+apiKey+"\",\n    \"titolo\" : \""+titolo+"\"\n}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/create")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    /**
     @return cartCod
     */
    static String renameCart(String apiKey, String cartCod, String titolo){

        RenameCart renameCart = new RenameCart();
        renameCart.setParams(apiKey,cartCod,titolo);
        Future<String> ris = executor.submit(renameCart);
        try {
            return ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "false";
    }
    class RenameCart implements Callable<String>{
        private String apiKey, titolo, cartCod;

        void setParams(String apiKey, String cartCod,String titolo){
            this.apiKey = apiKey;
            this.cartCod = cartCod;
            this.titolo = titolo;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"apiKey\" : \""+apiKey+"\",\n    \"cartCod\" : \""+cartCod+"\" ,\n    \"titolo\" : \""+titolo+"\"\n}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/rename")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static List<CarrelloAPI> getAllCarrelli(String apiKey){
        String buf = null;
        GetAllCarrelli getAllCarrelli = new GetAllCarrelli();
        getAllCarrelli.setParams(apiKey);
        Future<String> ris = executor.submit(getAllCarrelli);

        try {
            buf=ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CarrelloAPI[] carrelli = JsonManager.parseJsonClass(buf,CarrelloAPI[].class);
        if (carrelli==null || carrelli.length==0)
            return null;
        return Arrays.stream(carrelli).collect(Collectors.toList());
    }
    class GetAllCarrelli implements Callable<String>{
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
                    .url(serverDomain+"/cart/get")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    /**
     * NB Solo se creatore può eliminare il carrello
     * @param cart
     * @return bool
     */
    static boolean delCarrello(String apiKey,CarrelloAPI cart){
        String buf = null;
        DelCarrello delCarrello = new DelCarrello();
        delCarrello.setParams(apiKey, cart.getCartcod());
        Future<String> ris = executor.submit(delCarrello);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("true")==0)
            return true;
        return false;
    }
    /**
     * NB Solo se creatore può eliminare il carrello
     * @param apiKey cartCod
     * @return bool
     */
    static boolean delCarrello(String apiKey, String cartCod){
        String buf = null;
        DelCarrello delCarrello = new DelCarrello();
        delCarrello.setParams(apiKey, cartCod);
        Future<String> ris = executor.submit(delCarrello);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("true")==0)
            return true;
        return false;
    }                   //da controllare
    class DelCarrello implements Callable<String>{
        private String apiKey, cartCod;
        void setParams(String apiKey, String cartCod) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/delete")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static boolean shereCarrello(String apiKey, String cartCod, List<String> users){
        String buf = null;
        ShereCarrello shereCarrello = new ShereCarrello();
        shereCarrello.setParams(apiKey, cartCod, users);
        Future<String> ris = executor.submit(shereCarrello);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("true")==0)
            return true;
        return false;
    } //da controllare
    class ShereCarrello implements Callable<String>{
        private String apiKey, cartCod, users;
        void setParams(String apiKey, String cartCod, List<String> users) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
            this.users = listToString(users);
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\"\n" +
                    "    \"newUsers\": \""+users+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/get")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static List<CartItemAPI> allProducts(String apiKey, CarrelloAPI cart){
        return cartProducts(apiKey, cart.getCartcod());
    }
    static List<CartItemAPI> cartProducts(String apiKey, String cartCod){
        String buf = null;
        AllProducts allProducts = new AllProducts();
        allProducts.setParams(apiKey, cartCod);
        Future<String> ris = executor.submit(allProducts);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CartItemAPI[] prodotti = JsonManager.parseJsonClass(buf,CartItemAPI[].class);
        if (prodotti ==null || prodotti.length==0)
            return null;
        return Arrays.stream(prodotti).collect(Collectors.toList());
    }
    class AllProducts implements Callable<String>{
        private String apiKey, cartCod;
        void setParams(String apiKey, String cartCod) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/allProducts")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static List<ShopCartInfoAPI> getCartSellersInfoList(String apiKey, String cartCod){
        List<ShopAPI> shops = getCartSellers(apiKey, cartCod);
        LinkedList<ShopCartInfoAPI> infoList = new LinkedList<>();
        if (shops==null || shops.size()==0)
            return infoList;
        shops.forEach((s)-> infoList.add(getProductForSellerInfo(apiKey,cartCod,s.getSellercod())));
        infoList.forEach(s->System.out.println("\tcart->"+cartCod+"\n\tseller->"+s.getSellerCod()+"\n\tquantity->"+s.getQuant()));
        return infoList;
    }

    static List<ShopAPI> getCartSellers(String apiKey,CarrelloAPI cart){
        return getCartSellers(apiKey,cart.getCartcod());
    }
    static List<ShopAPI> getCartSellers(String apiKey, String cartCod){
        String buf = null;
        GetCartSellers getCartSellers = new GetCartSellers();
        getCartSellers.setParams(apiKey, cartCod);
        Future<String> ris = executor.submit(getCartSellers);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShopAPI[] shops = JsonManager.parseJsonClass(buf,ShopAPI[].class);
        if (shops ==null || shops.length==0)
            return null;
        return Arrays.stream(shops).collect(Collectors.toList());
    }
    class GetCartSellers implements Callable<String>{
        private String apiKey, cartCod;
        void setParams(String apiKey, String cartCod) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/getSellers")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    static List<CartItemAPI> getProductForSeller(String apiKey, String cartCod, Integer sellerCod){
        String buf = null;
        GetProductForSeller getProductForSeller = new GetProductForSeller();
        getProductForSeller.setParams(apiKey, cartCod, sellerCod);
        Future<String> ris = executor.submit(getProductForSeller);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CartItemAPI[] cartItems = JsonManager.parseJsonClass(buf,CartItemAPI[].class);
        if (cartItems ==null || cartItems.length==0)
            return null;
        return Arrays.stream(cartItems).collect(Collectors.toList());
    }
    class GetProductForSeller implements Callable<String>{
        private String apiKey, cartCod;
        private Integer sellerCod;
        void setParams(String apiKey, String cartCod, Integer sellerCod) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
            this.sellerCod = sellerCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\",\n" +
                    "    \"sellerCod\": \""+sellerCod+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/getProductForSeller")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    static ShopCartInfoAPI getProductForSellerInfo(String apiKey, String cartCod, Integer sellerCod){
        String buf = null;
        GetProductForSellerInfo getProductForSellerInfo = new GetProductForSellerInfo();
        getProductForSellerInfo.setParams(apiKey, cartCod, sellerCod);
        Future<String> ris = executor.submit(getProductForSellerInfo);
        try {
            buf = ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ShopCartInfoAPI[] cartItemsInfo = JsonManager.parseJsonClass(buf,ShopCartInfoAPI[].class);
        if (cartItemsInfo ==null || cartItemsInfo.length==0)
            return null;
        ShopCartInfoAPI item = Arrays.stream(cartItemsInfo).collect(Collectors.toList()).get(0);
        item.setSellerCod(sellerCod);
        item.setCartCod(cartCod);
        if (item.getQuant()==null)
            item.setQuant(0);
        if (item.getPricesum()==null)
            item.setPricesum(Double.valueOf(0));
        return item;
    }
    class GetProductForSellerInfo implements Callable<String>{
        private String apiKey, cartCod;
        private Integer sellerCod;
        void setParams(String apiKey, String cartCod, Integer sellerCod) {
            this.apiKey = apiKey;
            this.cartCod = cartCod;
            this.sellerCod = sellerCod;
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"apiKey\" : \""+apiKey+"\",\n" +
                    "    \"cartCod\": \""+cartCod+"\",\n" +
                    "    \"sellerCod\": "+sellerCod+"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/cart/getProductForSellerInfo")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}

