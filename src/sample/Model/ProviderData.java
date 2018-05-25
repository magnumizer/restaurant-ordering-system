package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DB.DBConn;

public class ProviderData {

    private ObservableList<Provider> providerList = FXCollections.observableArrayList();

    private ObservableList<Integer> idList = FXCollections.observableArrayList();

    public ObservableList<Provider> getProviderList() {
        return providerList;
    }

    public ObservableList<Integer> getIdList() {
        return idList;
    }

    public void loadList() {

        idList = FXCollections.observableArrayList();
        DBConn dbConn = new DBConn();
        providerList = dbConn.getAllProviders();
        for (Provider p : providerList) {
            idList.add(p.getId());
        }

    }

    public Integer searchByName(String name) {
        Provider provider =  providerList.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
        if (provider != null) {
            return provider.getId();
        }
        return null;
    }

    public String searchNameById(int id) {
        Provider provider =  providerList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (provider != null) {
            return provider.getName();
        }
        return null;
    }

    public Provider searchById(int id){

        for(Provider pr:providerList){

            if(pr.getId() == id){

                return pr;
            }
        }
        return null;
    }
    public ObservableList<Provider> getSearchedList(String search){

        ObservableList<Provider> tempList = FXCollections.observableArrayList();

        for(Provider pr:providerList){

            if(pr.matchProviderName(search)){

                tempList.add(pr);

            }
        }
        return tempList;
    }
}
