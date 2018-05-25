package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import sample.Model.CategoryData;
import sample.Model.OrderData;
import sample.Model.ProductData;
import sample.Model.ProviderData;

import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane menuAnchor;

    private ProductData productData = new ProductData();
    private ProviderData providerData = new ProviderData();
    private CategoryData categoryData = new CategoryData();

    @FXML
    public void initialize() {
        productData.loadList();
        providerData.loadList();
        categoryData.loadList();
    }

    @FXML
    private void toOrderScreen() {
        AnchorPane tempAnchor;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/View/makeOrder.fxml"));
            tempAnchor = fxmlLoader.load();
            OrderController controller = fxmlLoader.getController();
            controller.setProductData(this.productData);
            controller.setCategoryData(this.categoryData);
            controller.setProviderData(this.providerData);
            controller.linkProductData();
            menuAnchor.getChildren().setAll(tempAnchor);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toItemScreen() {
        AnchorPane tempAnchor;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/View/manageItems.fxml"));
            tempAnchor = fxmlLoader.load();
            ItemController controller = fxmlLoader.getController();
            controller.setProductData(this.productData);
            controller.setProviderData(this.providerData);
            controller.setCategoryData(this.categoryData);
            controller.linkProductData();
            menuAnchor.getChildren().setAll(tempAnchor);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toLogScreen() {
        AnchorPane tempAnchor;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            tempAnchor = fxmlLoader.load(getClass().getResourceAsStream("/sample/View/log.fxml"));

            menuAnchor.getChildren().setAll(tempAnchor);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}