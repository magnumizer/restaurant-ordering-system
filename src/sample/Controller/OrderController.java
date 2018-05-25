package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Model.*;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class OrderController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<Product> productOrderTable;
    @FXML
    private ChoiceBox<Category> categoryChoiceBox;
    @FXML
    private ChoiceBox<Provider> providerChoiceBox;
    @FXML
    private TextField totalField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private AnchorPane quantityPopup;
    @FXML
    private AnchorPane quantityPopupPane;
    @FXML
    private SplitPane splitPane;



    private ProductData productData;
    private ProviderData providerData;
    private CategoryData categoryData;

    private ObservableList<Product> orderList = FXCollections.observableArrayList();
    private ArrayList<Product> allProducts = new ArrayList<>();

    @FXML
    public void initialize() {

        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() &&
                        event.getClickCount() == 2 &&
                        !row.isEmpty()) {
                    spawnQuantityPopup();
                }
            });
            return row;
        });
        productTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (productTable.getSelectionModel().getSelectedItem() != null) {
                    spawnQuantityPopup();
                }
            }
        });

        productOrderTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() &&
                        event.getClickCount() == 2 &&
                        !row.isEmpty()) {
                    removeProductFromList(row.getItem());
                }
            });
            return row;
        });
        productOrderTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Product product = productOrderTable.getSelectionModel().getSelectedItem();
                if (product != null) {
                    removeProductFromList(product);
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                ObservableList<Product> list = FXCollections.observableArrayList();
                for (Product p : productData.getProductList()) {
                    if (p.matchProduct(newValue)) {
                        list.add(p);
                    }
                }
                productTable.setItems(list);
            } else {
                productTable.setItems(productData.getProductList());
            }
        });

        quantityTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                hideQuantityPopup();
            }
        });

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productOrderTable.getSelectionModel().clearSelection();
            }
        });

        productOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productTable.getSelectionModel().clearSelection();
            }
        });

        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d{0,3}");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        quantityTextField.setTextFormatter(formatter);
    }

    private void spawnQuantityPopup() {
        splitPane.setOpacity(0.4);
        quantityPopupPane.setDisable(false);
        quantityPopupPane.setVisible(true);
        quantityPopup.setDisable(false);
        quantityPopup.setVisible(true);
        Platform.runLater(() -> quantityTextField.requestFocus());
    }

    private void hideQuantityPopup() {
        splitPane.setOpacity(1.0);
        quantityPopupPane.setDisable(true);
        quantityPopupPane.setVisible(false);
        quantityPopup.setDisable(true);
        quantityPopup.setVisible(false);
        productTable.requestFocus();
    }


    public void addProductQuantityPopup(ActionEvent actionEvent) {
        if (!quantityTextField.getText().equals("")) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            double quantity = Double.parseDouble(quantityTextField.getText());
            if (product != null) {
                addProductToList(product, quantity);
                hideQuantityPopup();
                productOrderTable.requestFocus();
                productOrderTable.getSelectionModel().selectLast();
            }
        }
    }

    public void cancelQuantityPopup(ActionEvent actionEvent) {
        hideQuantityPopup();
    }

    public void dismissQuantityPopup(MouseEvent mouseEvent) {
        hideQuantityPopup();
    }

    public void filterTable(ActionEvent actionEvent) {
        Category category = categoryChoiceBox.getValue();
        Provider provider = providerChoiceBox.getValue();

        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product product : allProducts) {
            if (category != null) {
                if (product.getCategoryName().equals(category.getName())) {
                    if (provider != null) {
                        if (product.getProviderId() == provider.getId()) {
                            filteredList.add(product);
                        }
                    } else {
                        filteredList.add(product);
                    }
                }
            } else {
                if (provider != null) {
                    if (product.getProviderId() == provider.getId()) {
                        filteredList.add(product);
                    }
                } else {
                    filteredList.add(product);
                }
            }
        }

        Platform.runLater(() -> productTable.getItems().setAll(filteredList));
    }

    public void resetFilters(ActionEvent actionEvent) {
        categoryChoiceBox.getSelectionModel().clearSelection();
        providerChoiceBox.getSelectionModel().clearSelection();
        filterTable(actionEvent);
    }

    private void setChoiceBoxOptions() {
        categoryChoiceBox.setItems(categoryData.getSimpleCategoryList());
        providerChoiceBox.setItems(providerData.getProviderList());
    }

    private void setNamesCatProv() {

        for (Product pr : productData.getProductList()) {

            int provID = pr.getProviderId();
            int catID = pr.getCategoryId();

            for (Provider pro : providerData.getProviderList()) {

                if (provID == pro.getId()) {

                    pr.setProviderName(pro.getName());
                }
            }
            for (MyPair<Integer, String> mp : categoryData.getCategoryList()) {

                if (catID == mp.getKey()) {

                    pr.setCategoryName(mp.getValue());
                }
            }
        }
    }

    private void addProductToList(Product product, double quantity) {
        product.setQuantity(quantity);
        boolean matchingProduct = false;
        for (Product productOrder : orderList) {
            if (product == productOrder) {
                productOrder.setQuantity(quantity);
                matchingProduct = true;
                break;
            }
        }
        if (!matchingProduct) {
            orderList.add(product);
        }
        productOrderTable.getItems().setAll(orderList);
        calculateNewTotal();
    }

    private void removeProductFromList(Product product) {
        orderList.remove(product);
        productOrderTable.getItems().setAll(orderList);
        calculateNewTotal();
    }

    private void calculateNewTotal() {
        double total = 0.0;
        for (Product product : orderList) {
            total += product.getPrice() * product.getQuantity();
        }
        totalField.setText(String.valueOf(total));
    }

    public void placeOrder(ActionEvent actionEvent) {
        Order order = new Order(Double.parseDouble(totalField.getText()));
        for (int i = 0; i < orderList.size(); i++) {
            Product product = orderList.get(i);
            Provider provider = providerData.searchById(product.getProviderId());
            order.addItem(product, provider);
        }


        ArrayList<MyPair<Provider, Double>> minNotMetProviders = order.providerOrderMeetsMinPrice();
        if (minNotMetProviders.size() == 0) {
            order.printOrder();
        } else {
            String errorMsg = "";
            for (MyPair<Provider, Double> myPair : minNotMetProviders) {
                errorMsg += "--------------------------- \n";
                errorMsg += "Minimum order price not met for provider: \n ->" + myPair.getKey().getName() + " \n" +
                        "Provider minimum: \n ->" + myPair.getKey().getMinOrderPrice() + " \n" +
                        "Your order total: \n ->" + myPair.getValue() + " \n";
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not place order!");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        }

    }

    public void setProductData(ProductData data) {
        this.productData = data;
    }

    public void setProviderData(ProviderData data) {
        this.providerData = data;
        providerChoiceBox.setItems(providerData.getProviderList());
    }

    public void setCategoryData(CategoryData data) {
        this.categoryData = data;
        categoryChoiceBox.setItems(categoryData.getSimpleCategoryList());
    }

    public void linkProductData() {
        setNamesCatProv();
        setChoiceBoxOptions();
        allProducts.addAll(productData.getProductList());
        productTable.setItems(productData.getProductList());
    }
}
