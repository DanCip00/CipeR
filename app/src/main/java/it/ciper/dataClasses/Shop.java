package it.ciper.dataClasses;

public class Shop {
    Integer sellerCod;
    String logo;
    String sellerName;

    public Shop(int sellerCod, String logo, String sellerName) {
        super();
        this.sellerCod = sellerCod;
        this.logo = logo;
        this.sellerName = sellerName;
    }

    public Shop(){
        super();
    }

    public int getSellerCod() {
        return sellerCod;
    }

    public void setSellerCod(int sellerCod) {
        this.sellerCod = sellerCod;
    }

    public String getLogo() {
        return logo;
    }

    public void Logo(String srcLogo) {
        this.logo = srcLogo;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
