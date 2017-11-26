package in.net.maitri.xb.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private Context mContext;

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "XposeBilling";
    // Category table name
    private static final String CATEGORY_TABLE_NAME = "CategoryMst";
    // Category Table Columns names
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_NAME = "category_name";
    private static final String KEY_CAT_IMAGE_PATH = "category_image";
    private static final String KEY_CAT_CREATED_AT = "category_createdAt";
    // item table name
    private static final String ITEM_TABLE_NAME = "ItemMst";
    // item Table Columns names
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_IMAGE_PATH = "item_image";
    private static final String KEY_ITEM_UOM = "item_uom";
    private static final String KEY_ITEM_CP = "item_cp";
    private static final String KEY_ITEM_SP = "item_sp";
    private static final String KEY_ITEM_HSN = "item_hsn";
    private static final String KEY_ITEM_GST = "item_gst";
    private static final String KEY_CATE_ID = "category_id";
    private static final String KEY_ITEM_CREATED_AT = "item_createdAt";
    // customer table name
    private static final String CUSTOMER_TABLE_NAME = "CustomerMst";
    // customer table column names
    private static final String KEY_CST_ID = "id";
    private static final String KEY_CST_NAME = "cst_name";
    private static final String KEY_CST_NUMBER = "cst_number";
    private static final String KEY_CST_GSTIN = "cst_gstin";
    private static final String KEY_CST_ADDRESS = "cst_address";
    private static final String KEY_CST_CREATED_AT = "cst_createdAt";
    // unit table name
    private static final String UNIT_TABLE_NAME = "UnitMst";
    // unit table column names
    private static final String KEY_UNIT_ID = "unit_id";
    private static final String KEY_UNIT_DESC = "unit_desc";
    private static final String KEY_UNIT_DECIMAL_ALLOWED = "item_decimalAllowed";
    private static final String KEY_UNIT_CREATED_AT = "unit_createdAt";
    // sales mst table name
    private static final String SALES_MST_TABLE_NAME = "SalesMst";
    // sales mst table column names
    private static final String KEY_SM_BILL_NO = "sm_billNo";
    private static final String KEY_SM_DATE = "sm_date";
    private static final String KEY_SM_QTY = "sm_qty";
    private static final String KEY_SM_NET_AMT = "sm_netAmt";
    private static final String KEY_SM_DISCOUNT = "sm_discount";
    private static final String KEY_SM_PAYMENT_MODE = "sm_paymentMode";
    private static final String KEY_SM_PAYMENT_DET = "sm_paymentDet";
    private static final String KEY_SM_STATUS = "sm_status";
    private static final String KEY_SM_CUSTOMER = "sm_customer";
    private static final String KEY_SM_SALESMAN = "sm_salesman";
    private static final String KEY_SM_CREATED_AT = "sm_createdAt";
    private static final String KEY_SM_DATETIME = "sm_datetime";
    // sales mst table name
    private static final String SALES_DET_TABLE_NAME = "SalesDet";
    // sales mst table column names
    private static final String KEY_SD_SL_NO = "sd_slNo";
    private static final String KEY_SD_BILL_NO = "sd_billNo";
    private static final String KEY_SD_CATEGORY = "sd_category";
    private static final String KEY_SD_ITEM = "sd_item";
    private static final String KEY_SD_QTY = "sd_qty";
    private static final String KEY_SD_RATE = "sd_rate";
    private static final String KEY_SD_AMOUNT = "sd_amount";
    private static final String KEY_SD_CREATED_AT = "sd_createdAt";
    // sales mst table name
    private static final String SYSSPEC_TABLE_NAME = "SysSpec";
    // sales mst table column names
    private static final String KEY_SYS_KEY = "sys_key";
    private static final String KEY_SYS_VALUE = "sd_billNo";
    private static final String KEY_SYS_CATEGORY = "sd_category";
    private static final String KEY_SYS_COMMENT = "sd_item";
    private static final String KEY_SYS_CREATED_AT = "sd_createdAt";


    private List<Item> itemList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<Unit> unitList = new ArrayList<>();
    private List<ReportData> totalCategory = new ArrayList<>();
    private List<ReportData> totalItem = new ArrayList<>();
    private List<ReportData> totalReport = new ArrayList<>();

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE_NAME + "("
                + KEY_CAT_ID + " INTEGER PRIMARY KEY," + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_IMAGE_PATH + " TEXT" + KEY_CAT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE_NAME + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_UOM + " TEXT,"
                + KEY_ITEM_CP + " FLOAT,"
                + KEY_ITEM_SP + " FLOAT,"
                + KEY_ITEM_HSN + " TEXT,"
                + KEY_ITEM_GST + " FLOAT,"
                + KEY_CATE_ID + " INTEGER,"
                + KEY_IMAGE_PATH + " TEXT" + KEY_ITEM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_ITEM_TABLE);

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE_NAME + "("
                + KEY_CST_ID + " INTEGER PRIMARY KEY,"
                + KEY_CST_NAME + " TEXT,"
                + KEY_CST_NUMBER + " TEXT UNIQUE,"
                + KEY_CST_GSTIN + " TEXT,"
                + KEY_CST_ADDRESS + " TEXT,"
                + KEY_CST_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);

        String CREATE_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + UNIT_TABLE_NAME + "("
                + KEY_UNIT_ID + " INTEGER PRIMARY KEY,"
                + KEY_UNIT_DESC + " TEXT,"
                + KEY_UNIT_DECIMAL_ALLOWED + "INTEGER,"
                + KEY_UNIT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_UNIT_TABLE);

        String CREATE_SALES_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + SALES_MST_TABLE_NAME + "("
                + KEY_SM_BILL_NO + " INTEGER PRIMARY KEY,"
                + KEY_SM_DATE + " INTEGER,"
                + KEY_SM_QTY + " FLOAT,"
                + KEY_SM_NET_AMT + " FLOAT,"
                + KEY_SM_DISCOUNT + " FLOAT,"
                + KEY_SM_SALESMAN + " TEXT,"
                + KEY_SM_CUSTOMER + " INTEGER,"
                + KEY_SM_PAYMENT_MODE + " TEXT,"
                + KEY_SM_PAYMENT_DET + " TEXT,"
                + KEY_SM_STATUS + " TEXT,"
                + KEY_SM_DATETIME + " TEXT,"
                + KEY_SM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_SALES_MST_TABLE);

        String CREATE_SALES_DET_TABLE = "CREATE TABLE IF NOT EXISTS " + SALES_DET_TABLE_NAME + "("
                + KEY_SD_SL_NO + " INTEGER PRIMARY KEY,"
                + KEY_SD_BILL_NO + " INTEGER,"
                + KEY_SD_CATEGORY + " FLOAT,"
                + KEY_SD_ITEM + " FLOAT,"
                + KEY_SD_QTY + " FLOAT,"
                + KEY_SD_RATE + " FLOAT,"
                + KEY_SD_AMOUNT + " FLOAT,"
                + KEY_SD_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + " FOREIGN KEY (" + KEY_SD_BILL_NO + ") REFERENCES " + SALES_DET_TABLE_NAME + "(" + KEY_SM_BILL_NO + "))";
        db.execSQL(CREATE_SALES_DET_TABLE);

        String CREATE_SYSSPEC_TABLE = "CREATE TABLE IF NOT EXISTS " + SYSSPEC_TABLE_NAME + "("
                + KEY_SYS_KEY + " TEXT PRIMARY KEY,"
                + KEY_SYS_VALUE + " TEXT,"
                + KEY_SYS_CATEGORY + " TEXT,"
                + KEY_SYS_COMMENT + " TEXT,"
                + KEY_SYS_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_SYSSPEC_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  if (newVersion > oldVersion) {
         //   db.execSQL("ALTER TABLE " + UNIT_TABLE_NAME + " ADD COLUMN " +  KEY_UNIT_DECIMAL_ALLOWED + " INTEGER DEFAULT 0");
       // }

        db.execSQL("DROP TABLE IF EXISTS " +  SALES_MST_TABLE_NAME);
        onCreate(db);
    }

    public void addCategory(Category category) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CAT_NAME, category.getCategoryName());
            cv.put(KEY_CAT_IMAGE_PATH, category.getCategoryImage());
            db.insert(CATEGORY_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    // Getting single category
    public Category getCategory(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(CATEGORY_TABLE_NAME, new String[]{KEY_CAT_ID,
                            KEY_CAT_NAME, KEY_CAT_IMAGE_PATH}, KEY_CAT_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Category category = new Category(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
            cursor.close();
            // return category
            return category;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return new Category();
    }

    // Getting All categorys
    public List<Category> getAllcategorys() {
        categoryList.clear();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setCategoryName(cursor.getString(1));
                category.setCategoryImage(cursor.getString(2));
                // Adding category to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return category list
        return categoryList;
    }




    public int  getDateCount(String date) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "Select Cast ((JulianDay('"+date+"') - JulianDay('1900-01-01')) As Integer) as date";

            Cursor cursor = db.rawQuery(query,null);
            if (cursor != null)
                cursor.moveToFirst();

            int c =cursor.getInt(0);

            cursor.close();
            // return category
            return c;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return 0;
    }




















    // Updating single category
    public int updatecategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_NAME, category.getCategoryName());
        values.put(KEY_CAT_IMAGE_PATH, category.getCategoryImage());

        // updating row
        return db.update(CATEGORY_TABLE_NAME, values, KEY_CAT_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    // Deleting single category
    public void deletecategory(int categoryId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CATEGORY_TABLE_NAME, KEY_CAT_ID + " = ?",
                    new String[]{String.valueOf(categoryId)});
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    // Getting categorys Count
    public int getcategorysCount() {
        String countQuery = "SELECT  * FROM " + CATEGORY_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void addItem(Item item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_ITEM_NAME, item.getItemName());
            cv.put(KEY_IMAGE_PATH, item.getItemImage());
            cv.put(KEY_ITEM_UOM, item.getItemUOM());
            cv.put(KEY_ITEM_CP, item.getItemCP());
            cv.put(KEY_ITEM_SP, item.getItemSP());
            cv.put(KEY_ITEM_HSN, item.getItemHSNcode());
            cv.put(KEY_ITEM_GST, item.getItemGST());
            cv.put(KEY_CATE_ID, item.getCategoryId());
            db.insert(ITEM_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    public Item getItem(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME + " WHERE "
                    + KEY_ITEM_ID + " = " + id;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null)
                c.moveToFirst();
            Item item = new Item();
            item.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATE_ID)));
            item.setItemCP(c.getFloat(c.getColumnIndex(KEY_ITEM_CP)));
            item.setItemSP(c.getFloat(c.getColumnIndex(KEY_ITEM_SP)));
            item.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
            item.setItemImage(c.getString(c.getColumnIndex(KEY_IMAGE_PATH)));
            item.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
            item.setItemGST(c.getFloat(c.getColumnIndex(KEY_ITEM_GST)));
            item.setItemHSNcode(c.getString(c.getColumnIndex(KEY_ITEM_HSN)));
            item.setId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            c.close();
            return item;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return new Item();
    }

    // Getting All item
    public List<Item> getAllitems(int categoryId) {
        itemList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME + " WHERE " + KEY_CATE_ID + " = " + categoryId;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATE_ID)));
                    item.setItemCP(c.getFloat(c.getColumnIndex(KEY_ITEM_CP)));
                    item.setItemSP(c.getFloat(c.getColumnIndex(KEY_ITEM_SP)));
                    item.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                    item.setItemImage(c.getString(c.getColumnIndex(KEY_IMAGE_PATH)));
                    item.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                    item.setItemGST(c.getFloat(c.getColumnIndex(KEY_ITEM_GST)));
                    item.setItemHSNcode(c.getString(c.getColumnIndex(KEY_ITEM_HSN)));
                    item.setId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                    // Adding item to list
                    itemList.add(item);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return itemList;
    }

    // Updating single item
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_IMAGE_PATH, item.getItemImage());
        values.put(KEY_ITEM_CP, item.getItemCP());
        values.put(KEY_ITEM_SP, item.getItemSP());
        values.put(KEY_ITEM_GST, item.getItemGST());
        values.put(KEY_ITEM_UOM, item.getItemUOM());
        values.put(KEY_ITEM_HSN, item.getItemHSNcode());
        values.put(KEY_CATE_ID, item.getCategoryId());
        // updating row
        return db.update(ITEM_TABLE_NAME, values, KEY_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // Deleting single item
    public void deleteItem(int item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(ITEM_TABLE_NAME, KEY_ITEM_ID + " = ?",
                    new String[]{String.valueOf(item)});
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    // Getting item Count
    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + ITEM_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public void addCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CST_NAME, customer.getName());
            cv.put(KEY_CST_NUMBER, customer.getMobileno());
            cv.put(KEY_CST_GSTIN, customer.getGstin());
            cv.put(KEY_CST_ADDRESS, customer.getAddress());
            db.insert(CUSTOMER_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    public boolean updateCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CST_NAME, customer.getName());
            cv.put(KEY_CST_NUMBER, customer.getMobileno());
            cv.put(KEY_CST_GSTIN, customer.getGstin());
            cv.put(KEY_CST_ADDRESS, customer.getAddress());
            db.update(CUSTOMER_TABLE_NAME, cv, KEY_CST_ID + " = ?",
                    new String[]{String.valueOf(customer.getId())});
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return false;
    }

    // Getting All Customer
    public List<Customer> getAllCustomer() {
        customerList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setName(c.getString(c.getColumnIndex(KEY_CST_NAME)));
                    customer.setMobileno(c.getString(c.getColumnIndex(KEY_CST_NUMBER)));
                    customer.setGstin(c.getString(c.getColumnIndex(KEY_CST_GSTIN)));
                    customer.setAddress(c.getString(c.getColumnIndex(KEY_CST_ADDRESS)));
                    customer.setId(c.getInt(c.getColumnIndex(KEY_CST_ID)));
                    customerList.add(customer);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return customerList;
    }

    public void addUnit(Unit unit) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_UNIT_DESC, unit.getDesc());
            cv.put(KEY_UNIT_DECIMAL_ALLOWED, unit.getDecimalAllowed());
            db.insert(UNIT_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    // Getting All Customer
    public List<Unit> getAllUnit() {
        unitList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + UNIT_TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    Unit unit = new Unit();
                    unit.setId(c.getInt(c.getColumnIndex(KEY_UNIT_ID)));
                    unit.setDesc(c.getString(c.getColumnIndex(KEY_UNIT_DESC)));
                    unit.setDecimalAllowed(c.getInt(c.getColumnIndex(KEY_UNIT_DECIMAL_ALLOWED)));
                    unitList.add(unit);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return unitList;
    }

    public int getUomId(String uom) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT " + KEY_UNIT_ID + " FROM " + UNIT_TABLE_NAME + " WHERE " + KEY_UNIT_DESC + " = '" + uom + "'";
        Log.d("Query", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex(KEY_UNIT_ID));Log.d("Value", String.valueOf(id));
        c.close();
        return id;
    }


    public long addSalesMst(SalesMst salesMst) {
        long result = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_SM_BILL_NO, salesMst.getBillNO());
            cv.put(KEY_SM_DATE, salesMst.getDate());
            cv.put(KEY_SM_DATETIME, salesMst.getDateTime());
            cv.put(KEY_SM_QTY, salesMst.getQty());
            cv.put(KEY_SM_NET_AMT, salesMst.getNetAmt());
            cv.put(KEY_SM_DISCOUNT, salesMst.getDiscount());
            cv.put(KEY_SM_CUSTOMER, salesMst.getCustomerId());
            cv.put(KEY_SM_SALESMAN, salesMst.getSalesPerson());
            cv.put(KEY_SM_PAYMENT_MODE, salesMst.getPaymentMode());
            cv.put(KEY_SM_PAYMENT_DET, salesMst.getPaymentDet());
            cv.put(KEY_SM_STATUS, salesMst.getStatus());
            result =   db.insert(SALES_MST_TABLE_NAME, null, cv);
         db.close();
            return result;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return  result;
    }

    public long addSalesDet(SalesDet salesDet) {


        long result = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_SD_BILL_NO, salesDet.getBillNo());
            cv.put(KEY_SD_CATEGORY, salesDet.billItems.getCat_id());
            cv.put(KEY_SD_ITEM, salesDet.billItems.getItem_id());
            cv.put(KEY_SD_QTY, salesDet.billItems.getQty());
            cv.put(KEY_SD_RATE, salesDet.billItems.getRate());
            cv.put(KEY_SD_AMOUNT,salesDet.billItems.getAmount());
           result = db.insert(SALES_DET_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return  result;
    }

    public List<ReportData> getTotalReport(int fromDate, int toDate) {
        totalReport.clear();
        try {
            String selectQuery = "SELECT cast(SUM(SD." + KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SUM(SD." + KEY_SD_RATE + ")as text) AS sRATE, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
                    + " INNER JOIN " + CATEGORY_TABLE_NAME + " AS CAT ON CAT." + KEY_CAT_ID + " = SD." + KEY_SD_CATEGORY
                    + " INNER JOIN " + SALES_MST_TABLE_NAME + " AS SM ON SM." + KEY_SM_BILL_NO + " = SD." + KEY_SD_BILL_NO
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate;
            Log.d("Query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    ReportData reportData = new ReportData();
                    reportData.setrDescription("Grand Total");
                    reportData.setrQty(c.getString(c.getColumnIndex("sQTY")));
                    reportData.setrMrp(c.getString(c.getColumnIndex("sRATE")));
                    reportData.setrNetSales(c.getString(c.getColumnIndex("sAMT")));
                    totalReport.add(reportData);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return totalReport;
    }

    public List<ReportData> getTotalCategoryReport(int fromDate, int toDate){
        totalCategory.clear();
        try {
            String selectQuery = "SELECT CAT." + KEY_CAT_NAME
                    + ", cast(SUM(SD."+ KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SUM(SD." + KEY_SD_RATE + ")as text) AS sRATE, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
                    + " INNER JOIN " + CATEGORY_TABLE_NAME +" AS CAT ON CAT." + KEY_CAT_ID + " = SD." + KEY_SD_CATEGORY
                    + " INNER JOIN " + SALES_MST_TABLE_NAME+" AS SM ON SM." + KEY_SM_BILL_NO + " = SD." + KEY_SD_BILL_NO
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate
                    + " GROUP BY CAT." + KEY_CAT_NAME ;
            Log.d("Query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    ReportData reportData = new ReportData();
                    reportData.setrDescription(c.getString(c.getColumnIndex(KEY_CAT_NAME)));
                    reportData.setrCategory(c.getString(c.getColumnIndex(KEY_CAT_NAME)));
                    reportData.setrQty(c.getString(c.getColumnIndex("sQTY")));
                    reportData.setrMrp(c.getString(c.getColumnIndex("sRATE")));
                    reportData.setrNetSales(c.getString(c.getColumnIndex("sAMT")));
                    totalCategory.add(reportData);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return totalCategory;
    }

    public List<ReportData> getTotalItemReport(int fromDate, int toDate){
        totalItem.clear();
        try {
            String selectQuery = "SELECT CAT." + KEY_CAT_NAME + " ,ITM." + KEY_ITEM_NAME
                    + ", cast(SUM(SD."+ KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SUM(SD." + KEY_SD_RATE + ")as text) AS sRATE, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
                    + " INNER JOIN " + CATEGORY_TABLE_NAME +" AS CAT ON CAT." + KEY_CAT_ID + " = SD." + KEY_SD_CATEGORY
                    + " INNER JOIN " + ITEM_TABLE_NAME +" AS ITM ON ITM." + KEY_ITEM_ID+ " = SD." + KEY_SD_ITEM
                    + " INNER JOIN " + SALES_MST_TABLE_NAME+" AS SM ON SM." + KEY_SM_BILL_NO + " = SD." + KEY_SD_BILL_NO
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate
                    + " GROUP BY CAT." + KEY_CAT_NAME + " ,ITM." + KEY_ITEM_NAME;
            Log.d("Query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    ReportData reportData = new ReportData();
                    reportData.setrDescription(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                    reportData.setrCategory(c.getString(c.getColumnIndex(KEY_CAT_NAME)));
                    reportData.setrItem(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                    reportData.setrQty(c.getString(c.getColumnIndex("sQTY")));
                    reportData.setrMrp(c.getString(c.getColumnIndex("sRATE")));
                    reportData.setrNetSales(c.getString(c.getColumnIndex("sAMT")));
                    totalItem.add(reportData);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return totalItem;
    }


    public void addSysSpec(SysSpec sysSpec) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_SYS_KEY, sysSpec.getSysKey());
            cv.put(KEY_SYS_VALUE, sysSpec.getSysValue());
            cv.put(KEY_SYS_CATEGORY, sysSpec.getSysCategory());
            cv.put(KEY_SYS_COMMENT, sysSpec.getSysComment());
            db.insert(SYSSPEC_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    public boolean updateSysValue(String sysKey, String sysValue) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SYS_VALUE, sysValue);
            db.update(SYSSPEC_TABLE_NAME, values, KEY_ITEM_ID + " = ?",
                    new String[]{sysKey});
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return false;
    }


    private void createErrorDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("SQL Error")
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
