package it.ciper.data.dataClasses.product;

public class ProductAPI {
    protected String productcod, summaryname, name, description, srcimage, producer;

    public ProductAPI(String productcod, String summaryname, String name, String description, String srcimage, String producer) {
        this.productcod = productcod;
        this.summaryname = summaryname;
        this.name = name;
        this.description = description;
        this.srcimage = srcimage;
        this.producer = producer;
    }

    public ProductAPI() {
        super();
    }

    public String getProductcod() {
        return productcod;
    }

    public String getSummaryname() {
        return summaryname;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSrcimage() {
        return srcimage;
    }

    public String getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        return "ProductAPI{" +
                "productcod='" + productcod + '\'' +
                ", summaryname='" + summaryname + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", srcimage='" + srcimage + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }
}
