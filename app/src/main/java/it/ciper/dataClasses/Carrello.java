package it.ciper.dataClasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Carrello extends CarrelloJsonFormat {


     private Double costoTotale = null;
     private Long numOggetti = null;
     private Shop shop = null;
     private boolean modified = true;

    private TreeSet<Product> prodotti = new TreeSet<>();;
    private HashMap<Product, Long> quantity = new HashMap<>();


    Carrello(String titolo, Shop shop){
        super();
        this.shop = shop;
        this.titolo = titolo;
    }

    public Carrello(String titolo, List<String> prodCod, Integer sellerCod, Shop shop, TreeSet<Product> prodotti, HashMap<Product, Long> quantity) {
        super(titolo, prodCod, sellerCod);
        this.shop = shop;
        this.prodotti = prodotti;
        this.quantity = quantity;
    }

    public Shop getShop() {
        compute();
        return shop;
    }

    public List<Product> getProdotti() {
        return prodotti.stream().collect(Collectors.toList());
    }

    private void compute(){
        costoTotale=prodotti.stream().mapToDouble(p->{
            return Double.valueOf(p.getPrice()*quantity.get(p));
        }).sum();
        numOggetti = quantity.values().stream().mapToLong(q->q).sum();
        modified = false;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setProdotti(TreeSet<Product> prodotti) {
        this.prodotti = prodotti;
    }

    public void setQuantity(HashMap<Product, Long> quantity) {
        this.quantity = quantity;
    }

    public Double getCostoTotale(){
        if (!modified)
            return costoTotale;
        compute();
        return costoTotale;
    }

    public long getNumOggetti(){
        if (!modified)
            return numOggetti;
        compute();
        return numOggetti;
    }

    public void addProdotto(Product prodotto, Long quantity){

        modified = true;
        if (prodotti.contains(prodotto)){
            this.quantity.computeIfPresent(prodotto,(p,q)->Long.valueOf(q+quantity));
            return;
        }
        prodotti.add(prodotto);
        this.quantity.put(prodotto,quantity);
        return;
    }

    public void removeProdotto(Product prodotto, Long quantity){

        if(!prodotti.contains(prodotto))
            return;
        modified = true;
        if (quantity.compareTo(this.quantity.get(prodotto))>=0){
            prodotti.remove(prodotto);
            this.quantity.remove(prodotto);
            return;
        }
        this.quantity.compute(prodotto,(p,q)->Long.valueOf(q-quantity));
        return;
    }

    @Override
    public String toString() {
        return "Carrello{" +
                "titolo='" + titolo + '\'' +
                ", codProdotti=" + prodCod +
                ", sellerCod=" + sellerCod +
                ", costoTotale=" + costoTotale +
                ", numOggetti=" + numOggetti +
                ", shop=" + shop +
                ", modified=" + modified +
                ", prodotti=" + prodotti +
                ", quantity=" + quantity +
                '}';
    }
}
