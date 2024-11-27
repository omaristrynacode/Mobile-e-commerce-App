package com.example.mobileproject.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GroceryStoreDB";
    private static final int DATABASE_VERSION = 2;
    // Product Table
    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS Product ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "title TEXT,"
            + "description TEXT,"
            + "picUrl TEXT,"
            + "review INTEGER,"
            + "score REAL,"
            + "numberInCart INTEGER,"
            + "price REAL);";

    // Cart Table
    private static final String CREATE_CART_TABLE = "CREATE TABLE IF NOT EXISTS Cart ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "product_id INTEGER,"
            + "user_id INTEGER,"
            + "quantity INTEGER,"
            + "price_total REAL,"
            + "FOREIGN KEY (product_id) REFERENCES Product(id),"
            + "FOREIGN KEY (user_id) REFERENCES Users(username));";

    // Users Table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users ("
            + "username TEXT PRIMARY KEY,"
            + "password TEXT,"
            + "location TEXT,"
            + "payment_method TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        populateDBItems();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }
    // Create a new product
    public long createProduct(String title, String description, String picUrl, String review, double score, int numberInCart, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("picUrl", picUrl);
        values.put("review", review);
        values.put("score", score);
        values.put("numberInCart", numberInCart);
        values.put("price", price);
        return db.insert("Product", null, values);
    }

    // Create a new user
    public long createUser(String username, String password, String location, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("location", location);
        values.put("payment_method", paymentMethod);
        return db.insert("Users", null, values);
    }

    // Delete a product
    public int deleteProduct(long productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Product", "id = ?", new String[]{String.valueOf(productId)});
    }

    // Delete a user
    public int deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Users", "username = ?", new String[]{String.valueOf(username)});
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Product", null);
    }

    // Get all users
    public Cursor getUsers(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
    }
    public int updateUsername(String oldUsername, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        return db.update("Users", values, "username = ?", new String[]{oldUsername});
    }
    public int updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        return db.update("Users", values, "username = ?", new String[]{username});
    }

    public int updateLocation(String username, String newLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("location", newLocation);
        return db.update("Users", values, "username = ?", new String[]{username});
    }

    public int updatePaymentMethod(String username, String newPaymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("payment_method", newPaymentMethod);
        return db.update("Users", values, "username = ?", new String[]{username});
    }

    public void populateDBItems(){
        createProduct("apples", "The finest apples on the market grown and harvested locally, sold increments of 100g","apples", String.valueOf(1),4.8,0,2.3);
        createProduct("Appricot", "Delicious and juicy apricots perfect for snacking or adding to your favorite recipes.", "appricot", String.valueOf(10), 4.5, 0, 3.5);
        createProduct("Banana", "Fresh and ripe bananas, a healthy and convenient snack for any time of the day.", "banana", String.valueOf(8), 4.2, 0, 1.2);
        createProduct("Carrot", "Crunchy and nutritious carrots, great for salads, snacks, or as a side dish.", "carrots", String.valueOf(5), 4.0, 0, 1.0);
        createProduct("Corn", "Sweet and tender corn kernels, perfect for adding a burst of flavor to your meals.", "corn", String.valueOf(7), 4.3, 0, 2.0);
        createProduct("Cereal", "A delicious and nutritious cereal blend with a mix of grains and dried fruits.", "cornflakes", String.valueOf(12), 4.7, 0, 4.5);
        createProduct("Cream Cheese", "Smooth and creamy cream cheese, ideal for spreading on bagels or using in recipes.", "creamcheese", String.valueOf(6), 4.1, 0, 2.8);
        createProduct("Doritos Chips", "Crunchy and flavorful Doritos chips, perfect for snacking during movie nights.", "doritos", String.valueOf(15), 4.8, 0, 3.0);
        createProduct("Grapes", "Sweet and juicy grapes, a refreshing and healthy snack for all occasions.", "grapes", String.valueOf(9), 4.4, 0, 2.5);
        createProduct("Greek Yogurt", "Creamy and delicious yogurt, a versatile and nutritious dairy product.", "greekyogurt", String.valueOf(11), 4.6, 0, 2.2);
        createProduct("Haribo Gummies", "Colorful and chewy gummy candies, a fun treat for kids and adults alike.", "haribogummies", String.valueOf(13), 4.9, 0, 1.5);
        createProduct("Ice Cream", "Rich and indulgent ice cream, available in various flavors to satisfy your sweet tooth.", "icecream", String.valueOf(20), 4.9, 0, 5.0);
        createProduct("Kitkat Chunky", "Crunchy and chocolatey Kitkat Chunky bars, a classic favorite for break time.", "kitkatchunky", String.valueOf(18), 4.7, 0, 2.7);
        createProduct("Milka Chocolate", "Smooth and velvety Milka chocolate, a delightful treat for chocolate lovers.", "milkachocolate", String.valueOf(14), 4.5, 0, 3.8);
        createProduct("Oreo", "Classic Oreo cookies with a creamy filling, perfect for dunking in milk.", "oreo", String.valueOf(16), 4.6, 0, 2.0);
        createProduct("Potatoes", "Fresh and versatile potatoes, ideal for roasting, mashing, or frying.", "potato", String.valueOf(8), 4.0, 0, 1.5);
        createProduct("Strawberries", "Plump and juicy strawberries, a sweet and nutritious addition to your fruit bowl.", "strawberries", String.valueOf(12), 4.3, 0, 3.2);
        createProduct("Tomatoes", "Ripe and flavorful tomatoes, perfect for salads, sandwiches, or cooking.", "tomatoes", String.valueOf(10), 4.2, 0, 1.8);
        createProduct("Twix", "Crunchy biscuit bars with caramel and chocolate, a satisfying treat for any occasion.", "twix", String.valueOf(15), 4.8, 0, 2.5);
        createProduct("Watermelon", "Sweet and hydrating watermelon slices, a refreshing choice for hot days.", "watermelon", String.valueOf(7), 4.4, 0, 4.0);
        createProduct("Nescafe Latte", "The Nescafe Iced Latte made from the most premium coffee beans for your enjoyment", "nescafe", String.valueOf(3), 4.4, 0, 1.75);
    }
    public void clearAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Product");
        db.execSQL("DELETE FROM Cart");
        db.execSQL("DELETE FROM Users");
    }
    public void clearProductTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Product");
    }

}
