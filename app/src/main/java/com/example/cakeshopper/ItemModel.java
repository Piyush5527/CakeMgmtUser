package com.example.cakeshopper;

public class ItemModel {
    private String name;
    private String flavour;
    private String startPrice;
    private String id;
    private String url;

    public ItemModel(String name, String startPrice, String flavour, String url,String id) {
        this.name = name;
        this.flavour = flavour;
        this.startPrice = startPrice;
        this.url = url;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }
}
