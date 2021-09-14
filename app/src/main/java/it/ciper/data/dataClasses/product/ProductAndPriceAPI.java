package it.ciper.data.dataClasses.product;

public class ProductAndPriceAPI extends ProductAPI {
    protected ProductAPI product;
    protected PriceAPI price;
    public ProductAndPriceAPI(ProductAPI prod, PriceAPI price){
        super(prod.getProductcod(),prod.getSummaryname(), prod.getName(), prod.getDescription(), prod.getSrcimage(), prod.getProducer());
        this.price=price;
        this.product = prod;
    }

    public ProductAPI getProduct() {
        return product;
    }

    public PriceAPI getPrice() {
        return price;
    }

    public boolean isOffert(){
        return price.offert;
    }

    /**
     * @Override
     * @Param ProductAPI è un antenato di ProductAndPriceAPI
     */
    public int compareTo(ProductAPI prod){
        ProductAndPriceAPI productAndPriceAPI = (ProductAndPriceAPI) prod;
        if (this.product.compareTo(productAndPriceAPI.getProduct())==0)
            return this.price.compareTo(productAndPriceAPI.getPrice());
        return this.product.compareTo(productAndPriceAPI.getProduct());
    }
}
