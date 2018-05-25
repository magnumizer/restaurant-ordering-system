package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DB.DBConn;

public class ProductData {

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public void loadList() {

        DBConn dbConn = new DBConn();
        productList = dbConn.getAllProducts();

    }

    public Product searchById(int id) {

        for (Product pr : productList) {

            if (pr.getId() == id) {

                return pr;
            }
        }
        return null;
    }

    /*public ObservableList<Product> getSearchedList(String search) {

        ObservableList<Product> tempList = FXCollections.observableArrayList();

        for (Product pr : productList) {

            if (pr.matchProductName(search)) {

                tempList.add(pr);

            }
        }
        return tempList;
    }*/

}
