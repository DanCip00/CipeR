package it.ciper.dataClasses;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LoadedData {

    public HashMap<String, Product> prodotti = new HashMap<>();
    public HashMap<Integer, Shop> negozi = new HashMap<>();
    public LinkedList<Offerts> offerte = new LinkedList<>();
    public List<Carrello> carrelli = new LinkedList<>();

    public LoadedData(){
        super();
    }


}
