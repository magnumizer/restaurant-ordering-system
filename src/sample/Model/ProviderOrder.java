package sample.Model;

import java.util.ArrayList;

public class ProviderOrder {

    private Provider provider;
    private ArrayList<Product> providerProducts = new ArrayList<>();

    public ProviderOrder(Provider provider, ArrayList<Product> providerProducts){

        this.provider = provider;
        this.providerProducts = providerProducts;
    }

    public Provider getProvider() {
        return provider;
    }

    public ProviderOrder(Provider provider){

        this.provider = provider;
    }

    public void setProviderId(Provider provider) {
        this.provider = provider;
    }

    public ArrayList<Product> getProviderProducts() {
        return providerProducts;
    }

    public void setProviderProducts(ArrayList<Product> providerProducts) {
        this.providerProducts = providerProducts;
    }

    public Double getTotal(){


        double total = 0;
        for(Product p:providerProducts){

            total += p.getPrice() * p.getQuantity();
        }

        return total;
    }

    public boolean meetRequiredPrice(){

        if(getTotal() >= provider.getMinOrderPrice())
            return true;

        return false;

    }

    @Override
    public String toString() {
        return getProvider().getName();
    }
}
