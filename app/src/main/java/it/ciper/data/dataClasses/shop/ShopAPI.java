package it.ciper.data.dataClasses.shop;

public class ShopAPI {
    protected Integer sellercod;
    protected String srclogo, sellername, address;
    protected Double lat, lng;

    public ShopAPI(Integer sellercod, String srclogo, String sellername, String address, Double lat, Double lng) {
        this.sellercod = sellercod;
        this.srclogo = srclogo;
        this.sellername = sellername;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public ShopAPI() {
        super();
    }

    public Integer getSellercod() {
        return sellercod;
    }

    public String getSrclogo() {
        return srclogo;
    }

    public String getSellername() {
        return sellername;
    }

    public String getAddress() {
        return address;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "ShopAPI{" +
                "sellercod=" + sellercod +
                ", srclogo='" + srclogo + '\'' +
                ", sellername='" + sellername + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
