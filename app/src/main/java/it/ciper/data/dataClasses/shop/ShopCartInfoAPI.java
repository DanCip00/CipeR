package it.ciper.data.dataClasses.shop;

public class ShopCartInfoAPI implements Comparable<ShopCartInfoAPI>{
    protected Integer quant ;
    protected Double pricesum;
    protected Integer sellerCod;
    protected String cartCod;
    public ShopCartInfoAPI(Integer quant, Double pricesum) {
        this.quant = quant;
        this.pricesum = pricesum;
    }

    public ShopCartInfoAPI() {
    }

    public Integer getQuant() {
        return quant;
    }

    public Double getPricesum() {
        return pricesum;
    }

    public Integer getSellerCod() {
        return sellerCod;
    }

    public String getCartCod() {
        return cartCod;
    }

    public void setCartCod(String cartCod) {
        this.cartCod = cartCod;
    }

    public void setQuant(Integer quant) {
        this.quant = quant;
    }

    public void setPricesum(Double pricesum) {
        this.pricesum = pricesum;
    }

    public void setSellerCod(Integer sellerCod) {
        this.sellerCod = sellerCod;
    }

    @Override
    public int compareTo(ShopCartInfoAPI shopCartInfoAPI) {
        return this.quant.compareTo(shopCartInfoAPI.getQuant());
    }
}
