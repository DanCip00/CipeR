package it.ciper.data.dataClasses.carrello;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import it.ciper.api.interfacce.CarrelliInterfaceApi;
import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.data.dataClasses.shop.ShopCartInfoAPI;

public class Carrello extends CarrelloAPI{
    protected CarrelloAPI carrelloAPI;
    protected DataCenter dataCenter;

    protected TreeMap<ShopAPI,List<CartItem>> cartItems = new TreeMap<ShopAPI, List<CartItem>>();
    protected TreeMap<ShopAPI, ShopCartInfoAPI> cartShopInfo = new TreeMap<>();

    public Carrello(DataCenter dataCenter, CarrelloAPI carrelloAPI){
        super(carrelloAPI.getCartcod(), carrelloAPI.getUsernamecreator(), carrelloAPI.getTitolo());
        this.dataCenter = dataCenter;
        this.carrelloAPI = carrelloAPI;
        upDateCartItems();
        upDateCartShopInfo();
    }

    public void upDateTitolo(){
        carrelloAPI = dataCenter.getCarrelloAPI(this.cartcod);
        this.titolo = carrelloAPI.getTitolo();
    }

    public void upDateCartItems(){
        cartItems = new TreeMap<>();
        List<ShopAPI> shops = CarrelliInterfaceApi.getCartSellers(dataCenter.getApiKey(), carrelloAPI);
        if (shops==null || shops.size()==0)
            return;
        shops.stream()
                .forEach(s->cartItems.put(s,new LinkedList<>()));

        cartItems.keySet().stream()
                .forEach((s)->{
                    CarrelliInterfaceApi.getProductForSeller(dataCenter.getApiKey(), cartcod, s.getSellercod())
                            .forEach(p->cartItems.get(s).add(new CartItem(dataCenter, p)));
                });
    }

    void upDateCartShopInfo(){
        cartShopInfo = new TreeMap<>();
        List<ShopAPI> list =CarrelliInterfaceApi.getCartSellers(dataCenter.getApiKey(), carrelloAPI);
        if (list ==null || list.size()==0)
            return;
        list.stream()
                .forEach(s->cartShopInfo.put(s,CarrelliInterfaceApi.getProductForSellerInfo(dataCenter.getApiKey(), cartcod, s.getSellercod())));
    }

    Double getPriceSum(){
        upDateCartShopInfo();
        return cartShopInfo.values().stream()
                .mapToDouble(i->i.getPricesum())
                .sum();
    }

    Integer getQuantity(){
        upDateCartShopInfo();
        return cartShopInfo.values().stream()
                .mapToInt(i->i.getQuant())
                .sum();
    }

    public TreeMap<ShopAPI, List<CartItem>> getCartItems() {
        return cartItems;
    }

    public TreeMap<ShopAPI, ShopCartInfoAPI> getCartShopInfo() {
        return cartShopInfo;
    }

    public int isPresentPrdoductAndPriceAPI(ProductAndPriceAPI productAndPriceAPI){
        ShopAPI shop = dataCenter.getShopAPI(productAndPriceAPI.getPrice().getSellercod());

        if (!cartItems.containsKey(shop))
            return 0;
        List<CartItem> list = cartItems.get(shop);
        if (!list.stream().anyMatch(cartItem->{
            return cartItem.getProductcod().compareTo(productAndPriceAPI.getProductcod())==0;
            }))
            return 0;

        CartItem item = list.stream().filter(cartItem->cartItem.getProductcod().compareTo(productAndPriceAPI.getProductcod())==0)
                .findFirst().get();
        return item.getQuantity();
    }
}
