package it.ciper.data.dataClasses.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.ciper.api.interfacce.ProductInterfaceApi;
import it.ciper.data.DataCenter;
import it.ciper.data.dataClasses.shop.ShopAPI;

public class Product extends ProductAPI {
    protected DataCenter dataCenter;
    protected ProductAPI productAPI;

    Map<ShopAPI, PriceAPI> prices = null;

    Product(DataCenter dataCenter, ProductAPI productAPI){
        super(productAPI.getProductcod(), productAPI.getSummaryname(), productAPI.getName(), productAPI.getDescription(), productAPI.getSrcimage(), productAPI.getProducer());
        this.dataCenter = dataCenter;
        this.productAPI = productAPI;

        prices = dataCenter.getAllPriceAPI(productAPI).stream()
                .collect(Collectors.toMap(p->dataCenter.getShopAPI(p.getSellercod()),p->p));

    }

    List<PriceAPI> getAllPrices(){
        return prices.values().stream().collect(Collectors.toList());
    }
}
