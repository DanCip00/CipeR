package it.ciper.data.dataClasses.carrello;



public class CartItemAPI implements Comparable<CartItemAPI>{
    protected String cartcod, productcod, username;
    protected Integer  quantity, sellercod;

    public CartItemAPI(String cartcod, String productcod, String username, Integer quantity, Integer sellercod) {
        this.cartcod = cartcod;
        this.productcod = productcod;
        this.username = username;
        this.quantity = quantity;
        this.sellercod = sellercod;
    }

    public CartItemAPI() {
        super();
    }

    public String getCartcod() {
        return cartcod;
    }

    public String getProductcod() {
        return productcod;
    }

    public String getUsername() {
        return username;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSellercod() {
        return sellercod;
    }

    @Override
    public int compareTo(CartItemAPI cartItemAPI) {
        if (this.cartcod.compareTo(cartItemAPI.getCartcod())==0)
            if (this.productcod.compareTo(cartItemAPI.getProductcod())==0)
                if (this.sellercod.compareTo(cartItemAPI.getSellercod())==0)
                    return this.getUsername().compareTo(cartItemAPI.getUsername());
                else
                    return this.sellercod.compareTo(cartItemAPI.getSellercod());
            else
                this.productcod.compareTo(cartItemAPI.getProductcod());
        return this.cartcod.compareTo(cartItemAPI.getCartcod());
    }
}
