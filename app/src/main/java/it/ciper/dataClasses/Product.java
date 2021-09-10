package it.ciper.dataClasses;
public class Product implements Comparable<Product>{


    String productCod;
    String summaryName;
    String name;
    String description;
    Double price;
    String srcImage;
    String producer;

    Product(String productCod, String summaryName, String name, String desctiption, Double price, String srcImage, String producer){
        super();
        this.productCod = productCod;
        this.summaryName = summaryName;
        this.name = name;
        this. description = desctiption;
        this.price = price;
        this.srcImage = srcImage;
        this.producer = producer;
    }
    Product(){
        super();
    }
    @Override
    public String toString() {
        return "Product{" +
                "productCod='" + productCod + '\'' +
                ", summaryName='" + summaryName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", srcImage='" + srcImage + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }

    public String getProductCod() {
        return productCod;
    }

    public String getSummaryName() {
        return summaryName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public String getProducer() {
        return producer;
    }

    @Override
    public int compareTo(Product o) {
        return this.productCod.compareTo(o.getProductCod());
    }
}
