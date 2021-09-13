package it.ciper.data.dataClasses.carrello;

import it.ciper.data.dataClasses.product.ProductAPI;
import it.ciper.data.dataClasses.product.ProductAndPriceAPI;
import it.ciper.data.dataClasses.shop.ShopAPI;
import it.ciper.dataClasses.Shop;

public class CartItem extends CartItemAPI{
    protected CartItemAPI itemAPI;
    protected ProductAndPriceAPI productAndPriceAPI;
    protected ShopAPI shopAPI;
    protected CarrelloAPI carrelloAPI;


    CartItem(CartItemAPI item){
        super(item.getCartcod(), item.getProductcod(), item.getUsername(), item.getQuantity(), item.getSellercod());
        itemAPI = item;

    }
}
