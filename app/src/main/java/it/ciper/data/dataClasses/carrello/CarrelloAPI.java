package it.ciper.data.dataClasses.carrello;

public class CarrelloAPI implements Comparable<CarrelloAPI>{
    protected String cartcod, usernamecreator, titolo;

    public CarrelloAPI(String cartcod, String usernamecreator, String titolo) {
        this.cartcod = cartcod;
        this.usernamecreator = usernamecreator;
        this.titolo = titolo;
    }

    public CarrelloAPI() {
        super();
    }

    public String getCartcod() {
        return cartcod;
    }

    public String getUsernamecreator() {
        return usernamecreator;
    }

    public String getTitolo() {
        return titolo;
    }

    @Override
    public String toString() {
        return "CarrelloAPI{" +
                "cartcod='" + cartcod + '\'' +
                ", usernamecreator='" + usernamecreator + '\'' +
                ", titolo='" + titolo + '\'' +
                '}';
    }

    @Override
    public int compareTo(CarrelloAPI carrelloAPI) {
        return this.cartcod.compareTo(carrelloAPI.getCartcod());
    }
}
