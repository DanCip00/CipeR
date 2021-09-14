package it.ciper.data.dataClasses.product;

public class PriceAPI implements Comparable<PriceAPI>{
    protected String productcod;
    protected Integer sellercod;
    protected Double price, offertprice;
    protected Boolean offert;

    public PriceAPI(String productcod, Integer sellercod, Double price, Double offertprice, Boolean offert) {
        this.productcod = productcod;
        this.sellercod = sellercod;
        this.price = price;
        this.offertprice = offertprice;
        this.offert = offert;
    }

    public PriceAPI() {
        super();
    }

    public String getProductcod() {
        return productcod;
    }

    public Integer getSellercod() {
        return sellercod;
    }

    public Double getPrice() {
        return price;
    }

    public Double getOffertprice() {
        return offertprice;
    }

    public Boolean getOffert() {
        return offert;
    }

    public boolean isOffert(){
        return offert;
    }

    @Override
    public String toString() {
        return "PriceAPI{" +
                "productcod='" + productcod + '\'' +
                ", sellercod=" + sellercod +
                ", price=" + price +
                ", offertprice=" + offertprice +
                ", offert=" + offert +
                '}';
    }

    @Override
    public int compareTo(PriceAPI priceAPI) {
        if (this.getProductcod().compareTo(priceAPI.getProductcod())==0)
            return this.getSellercod().compareTo(priceAPI.getSellercod());
        return this.getProductcod().compareTo(priceAPI.getProductcod());
    }
}
