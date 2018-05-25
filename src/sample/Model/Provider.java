package sample.Model;

public class Provider {


    private int id;
    private String name;
    private String email;
    private Double minOrderPrice;

    public Provider(int id, String name, String email, Double minOrderPrice) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.minOrderPrice = minOrderPrice;
    }

    public Provider(String name, String email, Double minOrderPrice) {
        this.name = name;
        this.email = email;
        this.minOrderPrice = minOrderPrice;
    }

    public Provider(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getMinOrderPrice() {
        return minOrderPrice;
    }

    public void setMinOrderPrice(Double minOrderPrice) {
        this.minOrderPrice = minOrderPrice;
    }

    public boolean matchProviderName(String name){

        if(this.name.equals(name))
            return true;
        return false;

    }

    @Override
    public String toString(){
        return this.name;
    }
}
