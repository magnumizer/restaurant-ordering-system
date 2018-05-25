package sample.Model;

public class Product {


    private int id;
    private int productId;
    private String productName;
    private Double price;
    private int categoryId;
    private int providerId;
    private String unit;
    private Double quantity = 1d;
    private String providerName;
    private String categoryName;


    public Product(int id,
                   int productId,
                   String productName,
                   Double price,
                   int categoryId,
                   int providerId,
                   String unit,
                   Double quantity) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.categoryId = categoryId;
        this.providerId = providerId;
        this.unit = unit;
        this.quantity = quantity;

    }

    public Product(int productId,
                   String productName,
                   Double price,
                   int categoryId,
                   int providerId,
                   String unit,
                   Double quantity) {

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.categoryId = categoryId;
        this.providerId = providerId;
        this.unit = unit;
        this.quantity = quantity;

    }

    public Product() {
    }

    public boolean matchProduct(String info) {

        if (info == null || info.isEmpty()) {
            return false;
        }

        if (productName.contains(info) ||
                unit.contains(info) ||
                price.toString().contains(info) ||
                Integer.toString(productId).contains(info)) {
            return true;
        }
        return false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProviderName() {
        return providerName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return this.productName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
