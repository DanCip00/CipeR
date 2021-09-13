package it.ciper.data.dataClasses.carrello;



public class CartItemAPI {
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
}
