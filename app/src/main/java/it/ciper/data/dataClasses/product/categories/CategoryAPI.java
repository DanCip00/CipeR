package it.ciper.data.dataClasses.product.categories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class CategoryAPI {

    protected Double categoryid;
    protected String name;
    protected String productcod = null;

    public CategoryAPI() {
    }

    public CategoryAPI(String name,String productcod, Double categoryid) {
        this.name = name;
        this.productcod = productcod;
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public Double getCategoryid() {
        return categoryid;
    }

    public String getProductcod() {
        return productcod;
    }
}
