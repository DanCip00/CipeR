package it.ciper.data;

import android.location.Location;
import android.location.LocationManager;

import java.util.Comparator;
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
import it.ciper.data.dataClasses.carrello.Carrello;
import it.ciper.data.dataClasses.carrello.CarrelloAPI;
import it.ciper.data.dataClasses.product.PriceAPI;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.data.dataClasses.*;


public class DataCenter {

    TreeMap<String, Map<Integer, PriceAPI>> prices = new TreeMap<>(); //@key ->ProductCod @value ->Map of PriceAPI;
    TreeMap<Integer, ShopAPI> shopsAPI = new TreeMap<>();

    TreeMap<String, CarrelloAPI> cartsAPI = new TreeMap<>();
    TreeMap<String, Carrello> carts = new TreeMap<>();

    TreeMap<String, Map<Integer, ProductAndPriceAPI>> prodAndPriceCache = new TreeMap<>(); //@key _> ProductCod
    TreeMap<String, ProductAPI> productsAPI = new TreeMap<>();

    LinkedList<ProductAndPriceAPI> topOfferts = new LinkedList<>();

    protected TreeMap<Integer,Float> distances = new TreeMap<>();

    protected Location location;

    private int numOfferts = 0;
    private String apiKey=null;

    public DataCenter(String apiKey){
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    //Price and ProductAndPriceAPI
    public List<PriceAPI> getAllPriceAPI (ProductAPI productAPI){
        List<PriceAPI> list = ProductInterfaceApi.getAllPrices(apiKey,productAPI.getProductcod());
        if (list ==null)
            return null;
        list.forEach(p->addPriceAPI(p));
        return list;
    }


    public PriceAPI getPriceAPI(String productCod,Integer sellerCod){
        Map<Integer, PriceAPI> map;
        if (prices.containsKey(productCod) && (map = prices.get(productCod)).containsKey(sellerCod))
            return map.get(sellerCod);
        PriceAPI price =ProductInterfaceApi.getPrice(apiKey, productCod, sellerCod.toString());
        if (price==null)
            return null;
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
        PriceAPI price = getPriceAPI(productCod, sellerCod);
        if (price==null)
            return null;
        ProductAndPriceAPI pap = new ProductAndPriceAPI(getProductAPI(productCod),price);
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

    public List<ProductAndPriceAPI> getAllProductAndPriceAPI(ProductAPI productAPI){
        List<PriceAPI> priceAPIList = getAllPriceAPI(productAPI);

        if (priceAPIList==null || priceAPIList.size()==0)
            return null;
        return  priceAPIList.stream().map(p->getProductAndPriceAPI(productAPI.getProductcod(),p.getSellercod())).collect(Collectors.toList());
    }
                        //Offert
    /**
     * Le offerte vengono aggiunte 10 per volta
     */
    private void addOfferts(){
        numOfferts+=10;
        //TODO Se offline è un disastro
        ProductInterfaceApi.getBestOfferts(apiKey,numOfferts).stream()
                .peek(p->addPriceAPI(p))
                .map(o->getProductAndPriceAPI(o.getProductcod(),o.getSellercod()))
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
        shopsAPI.clear();
        ShopInterfaceApi.getAllShop(apiKey).stream().forEach(s->shopsAPI.put(s.getSellercod(),s));
        return shopsAPI.get(sellerCod);
    }


    public List<ShopAPI> getCartSellers(CarrelloAPI cart){
        List<ShopAPI> shops = CarrelliInterfaceApi.getCartSellers(apiKey, cart);
        shops.forEach(s->{
            if (!shopsAPI.containsKey(s.getSellercod()))
                shopsAPI.put(s.getSellercod(),s);
        });
        return shops;
    }

                        //Carrello
    public List<CarrelloAPI> getAllCarrelliAPI(){
        upDateCartsInfo();
        if (cartsAPI.size()==0)
            return new LinkedList<>();
        return cartsAPI.values().stream().collect(Collectors.toList());
    }

    public Carrello getCarrello(CarrelloAPI carrelloAPI){
        if (carts.containsKey(carrelloAPI.getCartcod()))
            return carts.get(carrelloAPI.getCartcod());
        upDateCartsInfo();
        Carrello cart = new Carrello(this, carrelloAPI);
        carts.put(cart.getCartcod(), cart);
        return cart;
    }
    public Carrello getCarrello(String cartCod){
        return getCarrello(getCarrelloAPI(cartCod));
    }

    private void upDateCartsInfo(){
        cartsAPI.clear();
        List<CarrelloAPI> list =CarrelliInterfaceApi.getAllCarrelli(apiKey);
        if (list==null || list.size()==0)
            return;
        list.stream().forEach(c->cartsAPI.put(c.getCartcod(), c));
    }

    public CarrelloAPI getCarrelloAPI(String cartCod){
        if (cartsAPI.containsKey(cartCod))
            return cartsAPI.get(cartCod);
        upDateCartsInfo();
        if (cartsAPI.containsKey(cartCod))
            return cartsAPI.get(cartCod);
        return null;
    }

    public boolean renameCart(String cartCod, String titolo){
        CarrelloAPI cart;
        if ((cart=getCarrelloAPI(cartCod)).getTitolo().compareTo(titolo)==0)
            return true ;
        cart.setTitolo(titolo);
        if (CarrelliInterfaceApi.renameCart(apiKey, cartCod, titolo).toString().compareTo("false")==0)
            return false;
        if (carts.containsKey(cartCod))
            carts.get(cartCod).upDateTitolo();
        return true;
    }

    public boolean remCarrello(String cartCod){
        boolean ris= CarrelliInterfaceApi.delCarrello(apiKey, cartCod);

        if (carts.containsKey(cartCod))
            carts.remove(cartCod);
        upDateCartsInfo();
        return ris;
    }
    public boolean remCarrello(CarrelloAPI cart){
        boolean ris= CarrelliInterfaceApi.delCarrello(apiKey,cart);
        if (carts.containsKey(cart.getCartcod()))
            carts.remove(cart.getCartcod());
        upDateCartsInfo();
        return ris;
    }
                            //distances
    public float getDistace(ShopAPI shop){

        if (distances.containsKey(shop.getSellercod()))
            return distances.get(shop.getSellercod());
        if (location ==null )
            return 0;

        Location destination = new Location(LocationManager.GPS_PROVIDER);
        destination.setLatitude(shop.getLat());
        destination.setLongitude(shop.getLng());
        float metri = location.distanceTo(destination);
        if (metri==0)
            return -1;
        distances.put(shop.getSellercod(),metri);
        return metri;
    }
                    //Search

    static final int SIZE_LIMIT=8;

    public List<ProductAPI> searchProduct(String req){
        String buf = req.toLowerCase()+"%";
        List<ProductAPI> list = ProductInterfaceApi.searchProduct(apiKey,buf,SIZE_LIMIT);
        if (list!=null)
            list = list.stream().collect(Collectors.toCollection(()->new LinkedList<>()));
        List<ProductAPI> listSec = ProductInterfaceApi.searchProduct(apiKey,"%"+buf,SIZE_LIMIT);
        if (list!=null)
            if (listSec!=null) {
                list.addAll(listSec);
                return list;
            }else
                return list;
        if (listSec!=null)
            return listSec;
        return new LinkedList<>();
    }
}
