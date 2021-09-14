package it.ciper.data.dataClasses.shop;

public class ShopCartInfoAPI implements Comparable<ShopCartInfoAPI>{
    protected Integer quant ,pricesum;

    public ShopCartInfoAPI(Integer quant, Integer pricesum) {
        this.quant = quant;
        this.pricesum = pricesum;
    }

    public ShopCartInfoAPI() {
    }

    public Integer getQuant() {
        return quant;
    }

    public Integer getPricesum() {
        return pricesum;
    }

    @Override
    public int compareTo(ShopCartInfoAPI shopCartInfoAPI) {
        return this.quant.compareTo(shopCartInfoAPI.getQuant());
    }
}
