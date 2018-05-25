package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sample.DB.DBConn;
import sample.Model.*;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ItemController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> providerColumn;
    @FXML
    private TableColumn<Product, Integer> categoryColumn;
    @FXML
    private TableColumn<Product, String> unitColumn;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private ChoiceBox<Provider> providerChoiceBox;
    @FXML
    private TextField providerNameField;
    @FXML
    private TextField providerEmailField;
    @FXML
    private TextField providerMinAmount;

    @FXML
    private TextField categoryNameField;
    @FXML
    private ChoiceBox<MyPair<Integer, String>> categoryChoiceBox;

    private ProductData productData;
    private ProviderData providerData;
    private CategoryData categoryData;
    private DBConn dbConn = new DBConn();

    private Pattern priceFormat = Pattern.compile("\\d*|\\d+\\.\\d{0,3}");

    @FXML
    public void initialize() {

        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return priceFormat.matcher(change.getControlNewText()).matches() ? change : null;
        });
        providerMinAmount.setTextFormatter(formatter);
    }


    @SuppressWarnings("Duplicates")
    private void setupTableFunctionality() {
        productIdColumn.setCellFactory(new Callback<TableColumn<Product, Integer>, TableCell<Product, Integer>>()  {
            @Override
            public TextFieldTableCell<Product, Integer> call(TableColumn<Product, Integer> productIDTableColumn) {
                return new TextFieldTableCell<Product, Integer>(new IntegerStringConverter()) {
                    @Override
                    public void updateItem(Integer id, boolean empty) {
                        addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (!event.getCharacter().matches("[0123456789]")) {
                                    event.consume();
                                }
                            }
                        });

                        super.updateItem(id, empty);

                        if (!empty) {
                            if (getText().equals("") || getText().equals("0")) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            if (id != null) {
                                setText(id.toString());
                            }
                        }
                    }
                };
            }
        });

        nameColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>()  {
            @Override
            public TextFieldTableCell<Product, String> call(TableColumn<Product, String> productNameTableColumn) {
                return new TextFieldTableCell<Product, String>(new DefaultStringConverter()) {
                    @Override
                    public void updateItem(String id, boolean empty) {
                        super.updateItem(id, empty);
                        if (!empty) {
                            if (getText().equals("")) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            if (id != null) {
                                setText(id);
                            }
                        }
                    }
                };
            }
        });

        priceColumn.setCellFactory(new Callback<TableColumn<Product, Double>, TableCell<Product, Double>>()  {
            @Override
            public TextFieldTableCell<Product, Double> call(TableColumn<Product, Double> productPriceTableColumn) {
                return new TextFieldTableCell<Product, Double>(new DoubleStringConverter()) {
                    @Override
                    public void updateItem(Double id, boolean empty) {
                        addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (!event.getCharacter().matches("[0123456789]|[.]")) {
                                    event.consume();
                                }
                                else {
                                    TextField tf = (TextField) getGraphic();
                                    TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
                                        return priceFormat.matcher(change.getControlNewText()).matches() ? change : null;
                                    });
                                    tf.setTextFormatter(formatter);
                                }
                            }
                        });

                        super.updateItem(id, empty);

                        if (!empty) {
                            if (getText().equals("") || getText().equals("0.0")) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            if (id != null) {
                                setText(id.toString());
                            }
                        }
                    }
                };
            }
        });

        StringConverter<Integer> providerConverter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return providerData.searchNameById(object);
            }

            @Override
            public Integer fromString(String string) {
                return providerData.searchByName(string);
            }
        };

        providerColumn.setCellFactory(new Callback<TableColumn<Product, Integer>, TableCell<Product, Integer>>()  {
            @Override
            public ChoiceBoxTableCell<Product, Integer> call(TableColumn<Product, Integer> productProviderTableColumn) {
                return new ChoiceBoxTableCell<Product, Integer>(providerConverter, providerData.getIdList()) {
                    @Override
                    public void updateItem(Integer id, boolean empty) {
                        super.updateItem(id, empty);
                        if (!empty && id != null) {
                            if (getItem() == null || getItem() == 0 || providerConverter.toString(id) == null) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            setText(providerConverter.toString(id));
                        }
                    }
                };
            }
        });

        StringConverter<Integer> categoryConverter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return categoryData.searchNameById(object);
            }

            @Override
            public Integer fromString(String string) {
                return categoryData.searchByName(string);
            }
        };

        categoryColumn.setCellFactory(new Callback<TableColumn<Product, Integer>, TableCell<Product, Integer>>()  {
            @Override
            public ChoiceBoxTableCell<Product, Integer> call(TableColumn<Product, Integer> productCategoryTableColumn) {
                return new ChoiceBoxTableCell<Product, Integer>(categoryConverter, categoryData.getIdList()) {
                    @Override
                    public void updateItem(Integer id, boolean empty) {
                        super.updateItem(id, empty);
                        if (!empty && id != null) {
                            if (getItem() == null || getItem() == 0 || categoryConverter.toString(id) == null) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            setText(categoryConverter.toString(id));
                        }
                    }
                };
            }
        });

        unitColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>()  {
            @Override
            public TextFieldTableCell<Product, String> call(TableColumn<Product, String> productUnitTableColumn) {
                return new TextFieldTableCell<Product, String>(new DefaultStringConverter()) {
                    @Override
                    public void updateItem(String id, boolean empty) {
                        super.updateItem(id, empty);
                        if (!empty) {
                            if (getText().equals("")) {
                                getStyleClass().add("highlightCell");
                            }
                            else {
                                getStyleClass().clear();
                            }
                            if (id != null) {
                                setText(id);
                            }
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void reloadProducts() {
        productData.loadList();
        providerData.loadList();
        categoryData.loadList();
        linkProductData();
    }

    @FXML
    private void removeRow() {
        Product tempProduct = productTable.getSelectionModel().getSelectedItem();
        if (tempProduct != null) {
            productData.getProductList().remove(tempProduct);
            if (tempProduct.getId() != 0) {
                dbConn.deleteProduct(tempProduct);
            }
        }
    }

    @FXML
    private void saveChanges() {
        for (Product product : productData.getProductList()) {
            String name = product.getProductName() == null ? "A product" : product.getProductName();

            if (product.getProductId() != 0) {
                if (product.getProductName() != null && !product.getProductName().equals("")) {
                    if (product.getPrice() != null && product.getPrice() != 0.0) {
                        if (product.getProviderId() != 0) {
                            if (product.getCategoryId() != 0) {
                                if (product.getUnit() != null && !product.getUnit().equals("")) {
                                    if (product.getId() == 0) {
                                        dbConn.addProduct(product);
                                    } else {
                                        dbConn.updateProduct(product);
                                    }
                                }
                                else {
                                    showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " must have a unit");
                                }
                            }
                            else {
                                showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " must have a category");
                            }
                        }
                        else {
                            showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " must have a provider");
                        }
                    }
                    else {
                        showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " must have a price greater than 0.0");
                    }
                }
                else {
                    showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " must have a name");
                }
            }
            else {
                showAlert(Alert.AlertType.ERROR, "ERROR", name + " has not been saved to the database!", name + " can't have an id of 0");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void addEmptyRow() {
        Product product = new Product();
        productData.getProductList().add(product);
        productTable.requestFocus();
        productTable.getSelectionModel().selectLast();
        productTable.getFocusModel().focusLeftCell();
    }

    @FXML
    private void changeProductName(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            product.setProductName(editEvent.getNewValue().toString());
        } else {
            editEvent.consume();
            productTable.refresh();
        }
    }

    @FXML
    private void changeProductPrice(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            Double temp = Double.parseDouble(editEvent.getNewValue().toString());
            product.setPrice(temp);
        } else {
            editEvent.consume();
            productTable.refresh();
        }
    }

    @FXML
    private void changeProviderName(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            product.setProviderId((Integer) editEvent.getNewValue());
        } else {
            editEvent.consume();
            productTable.refresh();
        }
    }

    @FXML
    private void changeProductCategory(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            product.setCategoryId((Integer) editEvent.getNewValue());
        } else {
            editEvent.consume();
            productTable.refresh();
        }
    }

    @FXML
    public void changeProductID(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            product.setProductId((Integer) editEvent.getNewValue());
        } else {
            editEvent.consume();
            productTable.refresh();
        }
    }

    @FXML
    private void changeProductUnit(CellEditEvent editEvent) {
        if (editEvent.getNewValue() != null) {
            Product product = productTable.getSelectionModel().getSelectedItem();
            product.setUnit(editEvent.getNewValue().toString());
        } else {
            editEvent.consume();
            productTable.refresh();
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
        categoryChoiceBox.setItems(categoryData.getCategoryList());
    }

    public void linkProductData() {
        providerChoiceBox.setItems(providerData.getProviderList());
        categoryChoiceBox.setItems(categoryData.getCategoryList());
        productTable.setItems(productData.getProductList());
        setupTableFunctionality();
    }

    public void displayProviderFields(ActionEvent actionEvent) {

        if (providerChoiceBox.getSelectionModel().isEmpty())
            return;

        providerNameField.setText(providerChoiceBox.getSelectionModel().getSelectedItem().getName());
        providerEmailField.setText(providerChoiceBox.getSelectionModel().getSelectedItem().getEmail());
        providerMinAmount.setText(providerChoiceBox.getSelectionModel().getSelectedItem().getMinOrderPrice().toString());


    }

    public void fillCategoryFields(ActionEvent actionEvent) {
        if(categoryChoiceBox.getSelectionModel().isEmpty())
            return;

        categoryNameField.setText(categoryChoiceBox.getSelectionModel().getSelectedItem().getValue());
    }

    public void addProvider(ActionEvent actionEvent) {

        Provider provider = new Provider(providerNameField.getText(),
                providerEmailField.getText(),
                Double.parseDouble(providerMinAmount.getText()));

        dbConn.addProvider(provider);
        providerData.loadList();
        providerChoiceBox.setItems(providerData.getProviderList());
        clearProviderFields();
        setupTableFunctionality();

    }

    public void updateProvider(ActionEvent actionEvent) {

        Provider provider = providerChoiceBox.getSelectionModel().getSelectedItem();
        provider.setName(providerNameField.getText());
        provider.setEmail(providerEmailField.getText());
        provider.setMinOrderPrice(Double.parseDouble(providerMinAmount.getText()));
        dbConn.updateProvider(provider);
        providerData.loadList();
        providerChoiceBox.setItems(providerData.getProviderList());
        clearProviderFields();
        setupTableFunctionality();

    }

    public void deleteProvider(ActionEvent actionEvent) {
        Provider provider = providerChoiceBox.getSelectionModel().getSelectedItem();
        provider.setName(providerNameField.getText());
        provider.setEmail(providerEmailField.getText());
        provider.setMinOrderPrice(Double.parseDouble(providerMinAmount.getText()));
        dbConn.deleteProvider(provider);
        providerData.loadList();
        providerChoiceBox.setItems(providerData.getProviderList());
        clearProviderFields();
        setupTableFunctionality();
    }

    private void clearProviderFields() {
        providerNameField.clear();
        providerEmailField.clear();
        providerMinAmount.clear();
        //providerChoiceBox.getSelectionModel().clearSelection();
    }


    public void saveCategory(ActionEvent actionEvent) {
        if (categoryChoiceBox.getSelectionModel().isEmpty()) {
            return;
        }
        dbConn.updateCategory(categoryChoiceBox.getSelectionModel().getSelectedItem().getKey(), categoryNameField.getText());
        categoryData.loadList();
        categoryChoiceBox.setItems(categoryData.getCategoryList());
        clearCategoryFields();
        setupTableFunctionality();
    }

    public void addCategory(ActionEvent actionEvent) {
        if(!categoryNameField.getText().equals("")) {
            Category category = new Category(categoryNameField.getText());

            dbConn.addCategory(category.getName());
            categoryData.loadList();
            categoryChoiceBox.setItems(categoryData.getCategoryList());
            clearCategoryFields();
            setupTableFunctionality();
        }
    }

    private void clearCategoryFields() {
        categoryNameField.clear();
    }

    public void removeCategory(ActionEvent actionEvent) {

        if(!categoryNameField.getText().equals("")){

            dbConn.deleteCategory(categoryNameField.getText());
            categoryData.loadList();
            categoryChoiceBox.setItems(categoryData.getCategoryList());
            clearCategoryFields();
            setupTableFunctionality();
        }

    }
}
