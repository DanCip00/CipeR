package it.ciper.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.PriceAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.dataClasses.Product;

public class DataCenter {

    TreeMap<String, Map<Integer, PriceAPI>> prices = new TreeMap<>(); //@key ->ProductCod @value ->Map of PriceAPI;
    TreeMap<String, ShopAPI> shopsAPI = new TreeMap<>();
    TreeMap<String, CarrelloAPI> cartsAPI = new TreeMap<>();
    TreeMap<String, Map<Integer, ProductAndPriceAPI>> prodAndPriceCache = new TreeMap<>(); //@key _> ProductCod
    TreeMap<String, ProductAPI> productsAPI = new TreeMap<>();

    private String apiKey=null;
    DataCenter(String apiKey){
        super();
        this.apiKey = apiKey;
    }



                        //ProductAPI

    public ProductAPI getProductAPI(String productCod){
        if (productsAPI.containsKey(productCod))
            return productsAPI.get(productCod);
        ProductAPI pr = ProductInterfaceApi.getProduct(apiKey, productCod);
        productsAPI.put(productCod, pr);
        return pr;
    }




                        //Price and ProductAndPriceAPI
    public PriceAPI getPriceAPI(String productCod,Integer sellerCod){
        Map<Integer, PriceAPI> map;
        if (prices.containsKey(productCod) && (map = prices.get(productCod)).containsKey(sellerCod))
            return map.get(sellerCod);
        PriceAPI price =ProductInterfaceApi.getPrice(apiKey, productCod, sellerCod.toString());
    }

    public ProductAndPriceAPI getProductAndPriceAPI(String productCod,Integer sellerCod){
        Map<Integer, ProductAndPriceAPI> map;
        if( prodAndPriceCache.containsKey(productCod) && (map=prodAndPriceCache.get(productCod)).containsKey(sellerCod))
            return map.get(sellerCod);
        ProductAndPriceAPI pap = new ProductAndPriceAPI(getProductAPI(productCod),getPriceAPI(productCod, sellerCod));
        prodAndPriceCache. //COMPUTE
    }
}
