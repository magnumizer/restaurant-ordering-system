package sample.Model;

import javafx.scene.control.Alert;
import sample.DB.DBConn;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {

    private int id;
    private Double price;
    private ArrayList<ProviderOrder> providerOrders;
    private String providerNames;
    private LocalDate date;

    public Order(int id, Double price) {
        this.id = id;
        this.price = price;
        this.providerOrders = new ArrayList<>();
        this.date = LocalDate.now();
    }

    public Order(Double price) {
        this.price = price;
        this.providerOrders = new ArrayList<>();
        this.date = LocalDate.now();
    }

    public Order(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ArrayList<ProviderOrder> getProviderOrders() {
        return providerOrders;
    }

    public String getProviderNames() {
        return providerNames;
    }

    public void setProviderNames(String names) {
        this.providerNames = names;
    }

    public void setProviderOrders(ArrayList<ProviderOrder> providerOrders) {
        this.providerOrders = providerOrders;
    }

    public void setDate (LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addItem(Product pr, Provider provider){
        boolean isThereProvider = false;

        for(int i = 0; i <providerOrders.size(); i++){

            if(pr.getProviderId() == providerOrders.get(i).getProvider().getId()){
                isThereProvider = true;
                providerOrders.get(i).getProviderProducts().add(pr);
                break;
            }

        }

        if(!isThereProvider){
            providerOrders.add(new ProviderOrder(provider));
            addItem(pr, provider);
        }


    }

    public ArrayList<MyPair<Provider, Double>> providerOrderMeetsMinPrice(){

        ArrayList<MyPair<Provider, Double>> minNotMetProviders = new ArrayList<>();
        for (int i = 0; i < providerOrders.size() ; i++) {
            if (!providerOrders.get(i).meetRequiredPrice()){
                MyPair myPair = new MyPair<Provider, Double>(providerOrders.get(i).getProvider(), providerOrders.get(i).getTotal());
                minNotMetProviders.add(myPair);
            }
        }
       return minNotMetProviders;
    }

    public void printOrder(){

        for (int i = 0; i < providerOrders.size() ; i++) {
            System.out.println("Order for provider: " + providerOrders.get(i).getProvider().getName());

            for (int j = 0; j < providerOrders.get(i).getProviderProducts().size() ; j++) {
                Product product = providerOrders.get(i).getProviderProducts().get(j);
                System.out.println("Product: " + product.getProductName());
            }
//            EmailMaker em = new EmailMaker();
//            em.sendMail("munteanbogdan97@gmail.com", em.emailContructor(providerOrders.get(i).getProviderProducts()));
//providerOrders.get(i).getProvider().getEmail()

        }
        new DBConn().addOrder(this);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("Your order has been successfully placed");
        alert.showAndWait();
    }

}
