package it.ciper.dataClasses;

import java.util.List;

public class CarrelloJsonFormat {

    protected String titolo;
    protected List<String> prodCod;
    protected Integer sellerCod;

    CarrelloJsonFormat(){
        super();
    }

    public CarrelloJsonFormat(String titolo, List<String> prodCod, Integer sellerCod) {
        this.titolo = titolo;
        this.prodCod = prodCod;
        this.sellerCod = sellerCod;
    }

    public boolean setTitolo(String titolo) {
        //Il titolo deve essere di 20 caratteri massimo
        if (titolo.length()>20)
            return false;
        this.titolo = titolo;
        return true;
    }

    public String getTitolo() {
        return titolo;
    }

    public List<String> getProdCod() {
        return prodCod;
    }

    public void setProdCod(List<String> prodCod) {
        this.prodCod = prodCod;
    }

    public Integer getSellerCod() {
        return sellerCod;
    }

    public void setSellerCod(Integer sellerCod) {
        this.sellerCod = sellerCod;
    }

    @Override
    public String toString() {
        return "CarrelloJsonFormat{" +
                "titolo='" + titolo + '\'' +
                ", prodCod=" + prodCod +
                ", sellerCod=" + sellerCod +
                '}';
    }
}
