package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import sample.Model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class LogController {

    @FXML
    public ChoiceBox<String> yearChoiceBox;
    @FXML
    public ChoiceBox<String> monthChoiceBox;
    @FXML
    public ChoiceBox<String> dayChoiceBox;
    @FXML
    public TableView<Order> orderTable;
    @FXML
    public TableView<Product> orderProductTable;

    private Order currentSelectedOrder = null;
    private OrderData orderData = new OrderData();

    private ObservableList<Order> allOrders = FXCollections.observableArrayList();

    private ObservableList<String> years = FXCollections.observableArrayList(
            "2016", "2017", "2018", "2019" );
    private ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" );
    private ObservableList<String> days = FXCollections.observableArrayList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
    @FXML
    public void initialize() {
        orderData.loadList();
        allOrders.setAll(orderData.getOrderList().keySet());
        orderTable.setItems(allOrders);

        yearChoiceBox.setItems(years);
        monthChoiceBox.setItems(months);
        dayChoiceBox.setItems(days);
    }

    public void filterTable(ActionEvent actionEvent) throws Exception {
        boolean yearSelected = !yearChoiceBox.getSelectionModel().isEmpty();
        boolean monthSelected = !monthChoiceBox.getSelectionModel().isEmpty();
        boolean daySelected = !dayChoiceBox.getSelectionModel().isEmpty();

        String year = yearChoiceBox.getValue();
        String month = String.valueOf(monthChoiceBox.getSelectionModel().getSelectedIndex() + 1);
        String day = dayChoiceBox.getValue();

        ObservableList<Order> filteredList = FXCollections.observableArrayList();

        if (yearSelected) {
            if (monthSelected) {
                if (daySelected) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

                    Date selectedDate = format.parse(year + "/" + month + "/" + day);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy/MM");

                    Date selectedDate = format.parse(year + "/" + month);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
            }
            else {
                if (daySelected) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/dd");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy/dd");

                    Date selectedDate = format.parse(year + "/" + day);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy");

                    Date selectedDate = format.parse(year);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
            }
        }
        else {
            if (monthSelected) {
                if (daySelected) {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("MM/dd");

                    Date selectedDate = format.parse(month + "/" + day);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("MM");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("MM");

                    Date selectedDate = format.parse(month);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
            }
            else {
                if (daySelected) {
                    SimpleDateFormat format = new SimpleDateFormat("dd");
                    DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd");

                    Date selectedDate = format.parse(day);

                    filteredList.addAll(searchOrder(format, format2, selectedDate));
                }
                else {
                    filteredList.addAll(allOrders);
                }
            }
        }

        orderTable.setItems(filteredList);
    }

    private ArrayList<Order> searchOrder(SimpleDateFormat dateType, DateTimeFormatter dateType1, Date selectedDate) {
        ArrayList<Order> list = new ArrayList<>();

        //change allorders to orderData.getOrderList()
        for (Order order : allOrders) {
            Date date = null;
            try {
                date = dateType.parse(order.getDate().format(dateType1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date.compareTo(selectedDate) == 0) {
                list.add(order);
            }
        }

        return list;
    }

    public void resetFilters(ActionEvent actionEvent) {
        yearChoiceBox.getSelectionModel().clearSelection();
        monthChoiceBox.getSelectionModel().clearSelection();
        dayChoiceBox.getSelectionModel().clearSelection();
    }

    public void onOrderTableClick(MouseEvent mouseEvent) {
        if (orderTable.getSelectionModel().isEmpty()) {
            return;
        }

        Order order = orderTable.getSelectionModel().getSelectedItem();

        //deselect
        if (order == currentSelectedOrder) {
            orderTable.getSelectionModel().clearSelection();
            currentSelectedOrder = null;
            orderProductTable.setItems(FXCollections.observableArrayList());
            return;
        }
        else {
            currentSelectedOrder = order;
        }

        ObservableList<Product> productsToDisplay = FXCollections.observableArrayList();
        productsToDisplay.addAll(orderData.getOrderList().get(order));
        orderProductTable.setItems(productsToDisplay);
    }

}
