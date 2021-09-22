package it.ciper.api.interfacce;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.json.JsonManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public interface UsersInterfaceApi {
    String serverDomain = SettingsApi.serverDomain;
    ExecutorService executor = SettingsApi.executor;


    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }



    static String getServerToken(){
        String buf = null;
        GetServerToken getServerToken = new GetServerToken();
        Future<String> ris = executor.submit(getServerToken);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf==null || buf=="false")
            return null;
        return buf;
    }
    class GetServerToken implements Callable<String> {

        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"client\" : \""+SettingsApi.clientToken+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/token")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string().replace("\"","");
        }
    }

    static String createUser(String token, String username, String email, String name, String surname, String password, String address, Integer avatar){
        String buf = null;
        CreateUser createUser = new CreateUser();
        createUser.setParams(token, username, email, name, surname, password, address, avatar);
        Future<String> ris = executor.submit(createUser);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (buf.compareTo("false")==0)
            return null;
        return buf;
    }
    class CreateUser implements Callable<String> {
        private String token, email, name, surname, password, address, username;
        private Integer avatar;

        void setParams(String serverToken, String username, String email, String name, String surname, String password, String address, Integer avatar) {
            this.token = serverToken;
            this.email = email;
            this.address =address;
            this.avatar = avatar;
            this.name = name;
            this.surname = surname;
            this.username = username;
            this.password = sha256(password+serverToken);             //La password è sha256(password+serverToken)
        }
        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n" +
                    "    \"token\" : \""+token+"\",\n" +
                    "    \"username\" : \""+username+"\",\n" +
                    "    \"password\" : \""+password+"\",\n" +
                    "    \"name\" : \""+name+"\",\n" +
                    "    \"surname\" : \""+surname+"\",\n" +
                    "    \"email\" : \""+email+"\",\n" +
                    "    \"address\" : \""+address+"\",\n" +
                    "    \"avatar\" : "+avatar+" }");
            Request request = new Request.Builder()
                    .url(serverDomain+"/user/create")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }


    static public class ResultLogIn{
        protected String  apiKey;
        protected boolean status;

        public ResultLogIn(String apiKey, boolean status) {
            this.apiKey = apiKey;
            this.status = status;
        }

        public ResultLogIn() {
            super();
        }

        public String getApiKey() {
            return apiKey;
        }

        public boolean isStatus() {
            return status;
        }
    }


    static String login(String serverToken, String username, String password){
        String buf = null;
        Login login = new Login();
        login.setParams(serverToken, username, password);
        Future<String> ris = executor.submit(login);
        try {
            buf =ris.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultLogIn resultLogIn;
        try {
             resultLogIn = JsonManager.parseJsonClass(buf, ResultLogIn.class);
        }catch(IllegalArgumentException e){
            return null;
        }
        if (resultLogIn==null || !resultLogIn.isStatus())
            return null;
        return resultLogIn.getApiKey();
    }
    class Login implements Callable<String> {
        private String user, password, token;

        void setParams(String user, String password, String ServerToken){
            this.user = user;
            this.password = sha256(password+ServerToken);
            this.token = ServerToken;
        }

        @Override
        public String call() throws Exception {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,"{\n" +
                    "    \"token\" : \""+token+"\",\n" +
                    "    \"username\" : \""+user+"\",\n" +
                    "    \"password\" : \""+password+"\"\n" +
                    "}");
            Request request = new Request.Builder()
                    .url(serverDomain+"/user/login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}
