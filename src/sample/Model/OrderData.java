package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import sample.DB.DBConn;

import java.util.ArrayList;

public class OrderData {

    private ObservableMap<Order, ArrayList<Product>> orderList = FXCollections.observableHashMap();

    public ObservableMap<Order, ArrayList<Product>> getOrderList() {
        return orderList;
    }

    public void loadList() {

        DBConn dbConn = new DBConn();
        orderList = dbConn.getAllOrders();

        for (Order order : orderList.keySet()) {
            String providerNames = "";
            ArrayList<String> providers = new ArrayList<>();

            for (Product product : orderList.get(order)) {

                if (!providers.contains(product.getProviderName())) {
                    providers.add(product.getProviderName());
                }
            }

            for (int i = 0; i < providers.size(); i++) {
                providerNames += providers.get(i);

                if (i < providers.size() - 1) {
                    providerNames += ", ";
                }
            }

            order.setProviderNames(providerNames);
        }
    }
}
