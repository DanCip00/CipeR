package it.ciper.dataClasses;

public class Offerts{
    String productCod;
    Product prodotto =null;
    Double newPrice;
    int sellerCod;
    Shop seller;

    public Offerts(String productCod, Double newPrice, int sellerCod) {
        this.productCod = productCod;
        this.newPrice = newPrice;
        this.sellerCod = sellerCod;
    }
    public  Offerts(){
        super();
    }

    public Product getProdotto() {
        return prodotto;
    }

    public void setProdotto(Product prodotto) {
        this.prodotto = prodotto;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public int getSellerCod() {
        return sellerCod;
    }

    public String getProductCod() {
        return productCod;
    }
    public void setProductCod(String productCod) {
        this.productCod = productCod;
    }

    public Shop getSeller() {
        return seller;
    }

    public void setSellerCod(int sellerCod) {
        this.sellerCod = sellerCod;
    }

    public void setSeller(Shop seller){
        this.seller = seller;
    }

}
