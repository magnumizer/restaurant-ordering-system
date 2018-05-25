package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import sample.DB.DBConn;

public class CategoryData {

    private ObservableList<MyPair<Integer, String>> categoryList;

    public ObservableList<MyPair<Integer, String>> getCategoryList() {
        return categoryList;
    }

    private ObservableList<Category> simpleCategoryList;

    public ObservableList<Category> getSimpleCategoryList() {
        return simpleCategoryList;
    }

    private ObservableList<Integer> idList = FXCollections.observableArrayList();

    public ObservableList<Integer> getIdList() {
        return idList;
    }

    public void loadList() {

        idList = FXCollections.observableArrayList();
        DBConn dbConn = new DBConn();
        categoryList = dbConn.getCategories();
        simpleCategoryList = dbConn.getSimpleCategoryList();
        for (MyPair<Integer, String> p : categoryList) {
            idList.add(p.getKey());
        }

    }

    public Integer searchByName(String name) {
        MyPair<Integer, String> pair  =  categoryList.stream().filter(p -> p.getValue().equals(name)).findFirst().orElse(null);
        if (pair != null) {
            return pair.getKey();
        }
        return null;
    }

    public String searchNameById(int id) {
        MyPair<Integer, String> pair  =  categoryList.stream().filter(p -> p.getKey() == id).findFirst().orElse(null);
        if (pair != null) {
            return pair.getValue();
        }
        return null;
    }
}
