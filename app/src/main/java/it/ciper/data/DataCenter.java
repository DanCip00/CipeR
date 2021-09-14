package it.ciper.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.api.interfacce.ShopInterfaceApi;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.PriceAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.dataClasses.Product;

public class DataCenter {

    TreeMap<String, Map<Integer, PriceAPI>> prices = new TreeMap<>(); //@key ->ProductCod @value ->Map of PriceAPI;
    TreeMap<Integer, ShopAPI> shopsAPI = new TreeMap<>();
    TreeMap<String, CarrelloAPI> cartsAPI = new TreeMap<>();
    TreeMap<String, Map<Integer, ProductAndPriceAPI>> prodAndPriceCache = new TreeMap<>(); //@key _> ProductCod
    TreeMap<String, ProductAPI> productsAPI = new TreeMap<>();

    TreeSet<ProductAndPriceAPI> topOfferts = new TreeSet<>();
    private int numOfferts = 0;
    private String apiKey=null;
    DataCenter(String apiKey){
        super();
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
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
    public List<PriceAPI> getAllPriceAPI (ProductAPI productAPI){
        List<PriceAPI> list = ProductInterfaceApi.getAllPrices(apiKey,productAPI.getProductcod());
        list.forEach(p->addPriceAPI(p));
        return list;
    }


    public PriceAPI getPriceAPI(String productCod,Integer sellerCod){
        Map<Integer, PriceAPI> map;
        if (prices.containsKey(productCod) && (map = prices.get(productCod)).containsKey(sellerCod))
            return map.get(sellerCod);
        PriceAPI price =ProductInterfaceApi.getPrice(apiKey, productCod, sellerCod.toString());
        prices.computeIfAbsent(productCod,(s)->new TreeMap<>());
        prices.get(productCod).put(sellerCod, price);
        return price;
    }

    public void addPriceAPI(PriceAPI price){
        if(prices.containsKey(price.getProductcod()) && prices.get(price.getProductcod()).containsKey(price.getSellercod()))
            return;
        prices.computeIfAbsent(price.getProductcod(),(s)->new TreeMap<>());
        prices.get(price.getProductcod()).put(price.getSellercod(), price);
    }

    public ProductAndPriceAPI getProductAndPriceAPI(String productCod,Integer sellerCod){
        Map<Integer, ProductAndPriceAPI> map;
        if( prodAndPriceCache.containsKey(productCod) && (map=prodAndPriceCache.get(productCod)).containsKey(sellerCod))
            return map.get(sellerCod);
        ProductAndPriceAPI pap = new ProductAndPriceAPI(getProductAPI(productCod),getPriceAPI(productCod, sellerCod));
        prodAndPriceCache.computeIfAbsent(productCod,(s)->new TreeMap<Integer, ProductAndPriceAPI>());
        prodAndPriceCache.get(productCod).put(sellerCod,pap);
        return pap;
    }
    public void addProductAndPriceAPI(ProductAndPriceAPI pap){
        if( prodAndPriceCache.containsKey(pap.getProductcod()) && prodAndPriceCache.get(pap.getProductcod()).containsKey(pap.getPrice().getSellercod()))
            return;
        prodAndPriceCache.computeIfAbsent(pap.getProductcod(),(s)->new TreeMap<Integer, ProductAndPriceAPI>());
        prodAndPriceCache.get(pap.getProductcod()).put(pap.getPrice().getSellercod(),pap);
    }
                        //Offert
    /**
     * Le offerte vengono aggiunte 10 per volta
     */
    private void addOfferts(){
        numOfferts+=10;
        ProductInterfaceApi.getBestOfferts(apiKey,numOfferts).stream()
                .peek(p->addPriceAPI(p))
                .map(o->{
                    ProductAndPriceAPI pap = getProductAndPriceAPI(o.getProductcod(),o.getSellercod());
                    return pap;
                })
                .forEach(o->topOfferts.add(o));
    }

    public List<ProductAndPriceAPI> getTopFiveOfferts(){
        if (numOfferts<5)
            addOfferts();
        return topOfferts.stream().limit(5)
                .collect(Collectors.toList());
    }

                        //Shop

    public ShopAPI getShopAPI(Integer sellerCod){
        if (shopsAPI.containsKey(sellerCod))
            return shopsAPI.get(sellerCod);
        ShopAPI sh = ShopInterfaceApi.getAllShop(apiKey).stream().filter(s->s.getSellercod().compareTo(sellerCod)==0).findFirst().get();
        shopsAPI.put(sh.getSellercod(),sh);
        return sh;
    }

                        //Carrello
    public List<CarrelloAPI> getAllCarrelli(){
        upDateCartsInfo();
        return cartsAPI.values().stream().collect(Collectors.toList());
    }

    public void upDateCartsInfo(){
        cartsAPI.clear();
        CarrelliInterfaceApi.getAllCarrelli(apiKey).stream().forEach(c->cartsAPI.put(c.getCartcod(), c));
    }

    public CarrelloAPI getCarrelloAPI(String cartCod){
        if (cartsAPI.containsKey(cartCod))
            return cartsAPI.get(cartCod);
        upDateCartsInfo();
        if (cartsAPI.containsKey(cartCod))
            return cartsAPI.get(cartCod);
        return null;
    }

    public boolean remCarrello(String cartCod){
        boolean ris= CarrelliInterfaceApi.delCarrello(apiKey, cartCod);
        upDateCartsInfo();
        return ris;
    }
    public boolean remCarrello(CarrelloAPI cart){
        boolean ris= CarrelliInterfaceApi.delCarrello(cart);
        upDateCartsInfo();
        return ris;
    }
}
