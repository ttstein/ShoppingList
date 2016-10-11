package com.example.gordoff2.jsongetdata;

/**
 * Created by Administrator on 06.09.2016.
 */
public class ShoppingMemo {
    private String product;
    private int quantity;
    private double price;
    private long id;
    private boolean bought;

    public ShoppingMemo() {
    }

    public ShoppingMemo(String product, int quantity, double price, long id, boolean bought) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        this.bought = bought;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public String getProduct() {

        return product;
    }

    public void setProduct(String product) {

        this.product = product;
    }

    public int getQuantity() {

        return quantity;
    }

    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public boolean isBought() {

        return bought;
    }

    public void setBought(boolean bought) {

        this.bought = bought;
    }

    /* ------------------ Daten anzeigen -------------------------*/
    @Override
    public String toString() {
        return quantity + " x " + product + " (Preis: " + price + ")";
    }
}