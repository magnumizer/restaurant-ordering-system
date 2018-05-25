package sample.DB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import sample.Model.*;

import java.sql.*;
import java.util.ArrayList;

public class DBConn {


    private final String URL = "jdbc:mysql://sql11.freemysqlhosting.net:3306/";
    private final String URL_DB_EXTRA = "?allowMultiQueries=true&rewriteBatchedStatements=true";
    private final String DB_NAME = "sql11204786";
    private final String USER = "sql11204786";
    private final String PASS = "ZtdlZGkr9i";


    Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    this.URL + this.DB_NAME + this.URL_DB_EXTRA,
                    this.USER,
                    this.PASS);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public ObservableList<Product> getAllProducts() {

        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT provider_product.providerId, provider_product.productId, products.external_id, products.name, products.price,\n" +
                "  products.unit, products.quantity, categories.category_id, categories.category_name FROM provider_product JOIN products ON provider_product.productId = products.id\n" +
                "JOIN categories ON categories.category_id = products.category_id;";

        try {
            System.out.println("retrieving products from db...");
            Connection conn = getConn();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Product product = new Product(
                        resultSet.getInt("productId"),
                        resultSet.getInt("external_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("providerId"),
                        resultSet.getString("unit"),
                        resultSet.getDouble("quantity"));
                productList.add(product);
            }
            conn.close();
            System.out.println("products retrieved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public void addProduct(Product product) {

        String _name = product.getProductName();
        double _price = product.getPrice();
        String _unit = product.getUnit();
        double _quantity = product.getQuantity();
        int _externalId = product.getProductId();
        int _categoryId = product.getCategoryId();
        int _providerId = product.getProviderId();


        String sql = "BEGIN; " +
                "INSERT INTO products ( `name`, `price`, `unit`, `quantity`, `external_id`,  `category_id`) " +
                "VALUES (?,?,?,?,?,?);" +
                "INSERT INTO provider_product (providerId, productId)" +
                "VALUES(?, LAST_INSERT_ID());" +
                "COMMIT;";

        try {
            System.out.println("adding product to db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, _name);
            ps.setDouble(2, _price);
            ps.setString(3, _unit);
            ps.setDouble(4, _quantity);
            ps.setInt(5, _externalId);
            ps.setInt(6, _categoryId);
            ps.setInt(7, _providerId);

            ps.executeUpdate();
            ps.close();
            conn.close();
            System.out.println("added product to db!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {

        int _id = product.getId();
        String _name = product.getProductName();
        double _price = product.getPrice();
        String _unit = product.getUnit();
        double _quantity = product.getQuantity();
        int _externalId = product.getProductId();
        int _categoryId = product.getCategoryId();


        String sql = "UPDATE products SET name = ?, price = ?, unit = ?," +
                " quantity = ?, external_id = ?, category_id = ?" +
                " WHERE id = ?;";

        try {
            System.out.println("updating product in db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, _name);
            ps.setDouble(2, _price);
            ps.setString(3, _unit);
            ps.setDouble(4, _quantity);
            ps.setInt(5, _externalId);
            ps.setInt(6, _categoryId);
            ps.setInt(7, _id);

            ps.execute();
            ps.close();
            conn.close();
            System.out.println("product updated in db!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean deleteProduct(Product product) {

        int _id = product.getId();

        String sqlTxt = "DELETE FROM products" +
                "  WHERE id = ?;";

        try {
            Connection conn = getConn();
            PreparedStatement prepStmt = conn.prepareStatement(sqlTxt);
            prepStmt.setInt(1, _id);
            prepStmt.execute();
            prepStmt.close();
            conn.close();
            System.out.println(product.getProductName() + " deleted from db!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void addProvider(Provider provider) {

        String _name = provider.getName();
        String _email = provider.getEmail();
        double _minorderprice = provider.getMinOrderPrice();

        String sql = "INSERT INTO providers ( `name`, `email`, `minorderprice`) VALUES (?,?,?)";

        try {
            System.out.println("adding provider to db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, _name);
            ps.setString(2, _email);
            ps.setDouble(3, _minorderprice);

            ps.executeUpdate();
            ps.close();
            conn.close();
            System.out.println("added provider to db!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProvider(Provider provider) {

        int _id = provider.getId();
        String _name = provider.getName();
        String _email = provider.getEmail();
        double _minorderprice = provider.getMinOrderPrice();


        String sql = "UPDATE providers SET name = ?, email = ?, minorderprice = ?" +
                " WHERE id = ?;";

        try {
            System.out.println("updating provider in db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, _name);
            ps.setString(2, _email);
            ps.setDouble(3, _minorderprice);
            ps.setInt(4, _id);

            ps.execute();
            ps.close();
            conn.close();
            System.out.println("provider updated in db!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean deleteProvider(Provider provider) {

        int _id = provider.getId();


        String sql = "DELETE FROM providers" +
                "  WHERE id = ?;";

        try {
            Connection conn = getConn();
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1, _id);
            prepStmt.execute();
            prepStmt.close();
            conn.close();
            System.out.println(provider.getName() + " deleted from db!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public ObservableList<Provider> getAllProviders() {

        ObservableList<Provider> providerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM providers";

        try {
            System.out.println("retrieving providers from db...");
            Connection conn = getConn();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Provider provider = new Provider(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getDouble("minorderprice")
                );
                providerList.add(provider);
            }
            conn.close();
            System.out.println("providers retrieved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return providerList;
    }


    public void addOrder(Order order) {

        String insertValuesStatement = "VALUES";
        for (ProviderOrder po : order.getProviderOrders()) {
            for(Product p : po.getProviderProducts()){
                insertValuesStatement += " (LAST_INSERT_ID(), " + p.getId() + ", "+ p.getQuantity() + "),";
            }
        }
        insertValuesStatement = insertValuesStatement.substring(0, insertValuesStatement.length() - 1);
        insertValuesStatement += ";";
        String sql = "BEGIN;" +
                "INSERT INTO orders (  order_date , price) " +
                "      VALUES (?, ?);" +
                "      INSERT INTO orderproducts ( orderid , productid, quantity) " +
                insertValuesStatement +
                " COMMIT;";
        try {
            System.out.println("adding orders to db...");

            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1,Timestamp.valueOf(order.getDate().atStartOfDay()));
            ps.setDouble(2, order.getPrice());

            ps.executeUpdate();
            ps.close();
            conn.close();
            System.out.println("added order to db!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableMap<Order, ArrayList<Product>> getAllOrders() {

        ObservableMap<Order, ArrayList<Product>> orderList = FXCollections.observableHashMap();

        String sql = "SELECT * FROM orders";

        String sql2 = "SELECT products.external_id, products.name, products.price, orderproducts.quantity, categories.category_name, providers.name " +
                "      FROM orders JOIN orderproducts ON orders.id = orderproducts.orderid " +
                "                  JOIN products ON orderproducts.productid = products.id " +
                "                  JOIN categories ON products.category_id = categories.category_id " +
                "                  JOIN provider_product ON products.id = provider_product.productId " +
                "                  JOIN providers ON provider_product.providerId = providers.id" +
                "      WHERE orders.id = ?";

        try {
            System.out.println("retrieving orders from db...");
            Connection conn = getConn();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Order> tempOrders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("id"),
                        resultSet.getDouble("price")
                );
                order.setDate(resultSet.getDate("order_date").toLocalDate());

                tempOrders.add(order);
            }
            for (Order o:tempOrders){
                PreparedStatement ps = conn.prepareStatement(sql2);
                ps.setInt(1, o.getId());
                ResultSet rs = ps.executeQuery();
                ArrayList<Product> products = new ArrayList<>();

                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("external_id"),
                            rs.getString("products.name"),
                            rs.getDouble("price"),
                            0,0,"",
                            rs.getDouble("quantity")
                    );
                    product.setProviderName(rs.getString("providers.name"));
                    product.setCategoryName(rs.getString("category_name"));

                    products.add(product);
                }
                orderList.put(o, products);

            }
            conn.close();
            System.out.println("orders retrieved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public void addCategory(String categoryName) {

        String sql = "INSERT INTO categories ( `category_name` ) VALUES (?)";

        try {
            System.out.println("adding category to db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, categoryName);

            ps.executeUpdate();
            ps.close();
            conn.close();
            System.out.println("added category to db!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean deleteCategory(int categoryId) {

        String sql = "DELETE FROM categories" +
                "  WHERE category_id = ?;";

        try {
            Connection conn = getConn();
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1, categoryId);
            prepStmt.execute();
            prepStmt.close();
            conn.close();
            System.out.println("category id " + categoryId + " deleted from db!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCategory(String categoryName) {

        String sql = "DELETE FROM categories" +
                "  WHERE category_name = ?;";

        try {
            Connection conn = getConn();
            PreparedStatement prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, categoryName);
            prepStmt.execute();
            prepStmt.close();
            conn.close();
            System.out.println(categoryName + " deleted from db!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<MyPair<Integer, String>> getCategories() {
        ObservableList<MyPair<Integer, String>> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM categories";
        try {
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                MyPair<Integer, String> category = new MyPair<>(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name")
                );

                list.add(category);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public ObservableList<Category> getSimpleCategoryList() {
        ObservableList<Category> categoryList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM categories";
        try {
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getString("category_name"),
                        resultSet.getInt("category_id")
                );

                categoryList.add(category);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categoryList;
    }

    public void updateCategory(int categoryId, String newName) {

        String sql = "UPDATE categories SET category_name = ? WHERE category_id = ?;";

        try {
            System.out.println("updating categories in db...");
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newName);
            ps.setInt(2, categoryId);

            ps.execute();
            ps.close();
            conn.close();
            System.out.println("provider categories in db!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
