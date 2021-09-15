package it.ciper.data.dataClasses.carrello;

import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;


public class CartItem extends CartItemAPI{
    protected CartItemAPI itemAPI;
    protected ProductAndPriceAPI productAndPriceAPI;
    protected ShopAPI shopAPI;
    protected CarrelloAPI carrelloAPI;
    protected DataCenter dataCenter;

    CartItem(DataCenter dataCenter, CartItemAPI item){
        super(item.getCartcod(), item.getProductcod(), item.getUsername(), item.getQuantity(), item.getSellercod());
        itemAPI = item;
        this.dataCenter = dataCenter;
        productAndPriceAPI = dataCenter.getProductAndPriceAPI(item.getProductcod(), item.getSellercod());
        shopAPI = dataCenter.getShopAPI(item.getSellercod());
        carrelloAPI = dataCenter.getCarrelloAPI(item.getCartcod());
    }

    public CartItemAPI getItemAPI() {
        return itemAPI;
    }

    public ProductAndPriceAPI getProductAndPriceAPI() {
        return productAndPriceAPI;
    }

    public ShopAPI getShopAPI() {
        return shopAPI;
    }

    public CarrelloAPI getCarrelloAPI() {
        return carrelloAPI;
    }
}
