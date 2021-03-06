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

import in.net.maitri.xb.billing.BillItems;
import in.net.maitri.xb.billing.BillSeries;

public class DbHandler extends SQLiteOpenHelper {

    private Context mContext;

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private ArrayList<TaxMst> mTaxList = new ArrayList<>();
    private ArrayList<HsnMst> mHsnList = new ArrayList<>();

    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "XposeBilling";
    // Category table name
    private static final String CATEGORY_TABLE_NAME = "CategoryMst";
    // Category Table Columns names
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_NAME = "category_name";
    private static final String KEY_CAT_IMAGE_PATH = "category_image";
    private static final String KEY_CAT_IS_ACTIVE = "cat_is_active";
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
    private static final String KEY_ITEM_IS_ACTIVE = "item_is_active";
    private static final String KEY_CATE_ID = "category_id";
    private static final String KEY_ITEM_CREATED_AT = "item_createdAt";
    private static final String KEY_ITEM_BARCODE = "item_barcode";
    private static final String KEY_ITEM_GST_ID = "item_gst_id";
    // customer table name
    private static final String CUSTOMER_TABLE_NAME = "CustomerMst1";
    // customer table column names
    private static final String KEY_CST_ID = "id";
    private static final String KEY_CST_NAME = "cst_name";
    private static final String KEY_CST_NUMBER = "cst_number";
    private static final String KEY_CST_EMAIL = "cst_email";
    private static final String KEY_CST_GSTIN = "cst_gstin";
    private static final String KEY_CST_ADDRESS1 = "cst_address1";
    private static final String KEY_CST_ADDRESS2 = "cst_address2";
    private static final String KEY_CST_CITY = "cst_city";
    private static final String KEY_CST_STATE = "cst_state";
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
    private static final String KEY_SM_SALE_BILL_NO = "sm_sale_billNo";
    private static final String KEY_SM_DATE = "sm_date";
    private static final String KEY_SM_QTY = "sm_qty";
    private static final String KEY_SM_NET_AMT = "sm_netAmt";
    private static final String KEY_SM_DISCOUNT = "sm_discount";
    private static final String KEY_SM_PAYMENT_MODE = "sm_paymentMode";
    private static final String KEY_SM_PAYMENT_DET = "sm_paymentDet";
    private static final String KEY_SM_STATUS = "sm_status";
    private static final String KEY_SM_CUSTOMER = "sm_customer";
    private static final String KEY_SM_CUSTOMER_NAME = "sm_customer_name";
    private static final String KEY_SM_CASHIER_NAME = "sm_cashier_name";
    private static final String KEY_SM_SALESMAN = "sm_salesman";
    private static final String KEY_SM_ITEM = "sm_item";
    private static final String KEY_SM_TAX_TYPE = "sm_TaxType";
    private static final String KEY_SM_ROUNDOFF = "sm_RoundOff";
    private static final String KEY_SM_CREATED_AT = "sm_createdAt";
    private static final String KEY_SM_DATETIME = "sm_datetime";
    private static final String KEY_SM_IS_CANCELLED = "sm_isCancelled";
    private static final String KEY_SM_CANCEL_DATETIME = "sm_cancelDateTime";
    private static final String KEY_SM_CANCEL_COMMENT = "sm_cancelComment";
    private static final String KEY_SM_CANCEL_PERSON = "sm_cancelPerson";
    private static final String KEY_SM_TAX_AMT1 = "sm_TaxAmt1";
    private static final String KEY_SM_TAX_AMT2 = "sm_TaxAmt2";
    private static final String KEY_SM_GST_SALE_AMT = "sm_GstSaleAmt";
    // sales mst table name
    private static final String SALES_DET_TABLE_NAME = "SalesDet";
    // sales mst table column names
    private static final String KEY_SD_SL_NO = "sd_slNo";
    private static final String KEY_SD_BILL_NO = "sd_billNo";
    private static final String KEY_SD_CATEGORY = "sd_category";
    private static final String KEY_SD_ITEM = "sd_item";
    private static final String KEY_SD_QTY = "sd_qty";
    private static final String KEY_SD_NET_RATE = "sd_net_rate";
    private static final String KEY_SD_RATE = "sd_rate";
    private static final String KEY_SD_AMOUNT = "sd_amount";
    private static final String KEY_SD_TAX_CODE1 = "sd_TaxCode1";
    private static final String KEY_SD_TAX_RATE1 = "sd_TaxRate1";
    private static final String KEY_SD_TAX_AMT1 = "sd_TaxAmt1";
    private static final String KEY_SD_TAX_CODE2 = "sd_TaxCode2";
    private static final String KEY_SD_TAX_RATE2 = "sd_TaxRate2";
    private static final String KEY_SD_TAX_AMT2 = "sd_TaxAmt2";
    private static final String KEY_SD_GST_SALE_AMT = "sd_GstSaleAmt";
    private static final String KEY_SD_DISCOUNT = "sd_Discount";
    private static final String KEY_SD_DATETIME = "sd_datetime";
    private static final String KEY_SD_CREATED_AT = "sd_createdAt";
    // sales mst table name
    private static final String SYSSPEC_TABLE_NAME = "SysSpec";
    // sales mst table column names
    private static final String KEY_SYS_KEY = "sys_key";
    private static final String KEY_SYS_VALUE = "sd_billNo";
    private static final String KEY_SYS_CATEGORY = "sd_category";
    private static final String KEY_SYS_COMMENT = "sd_item";
    private static final String KEY_SYS_CREATED_AT = "sd_createdAt";

    //Bill series table name
    private static final String BILLSERIES_TABLE_NAME = "BillSeries";
    //Bill Series column names
    private static final String KEY_BS_ID = "bs_id";
    private static final String KEY_BS_NAME = "bs_name";
    private static final String KEY_BS_SHORT_NAME = "bs_short_name";
    private static final String KEY_BS_RESET_TYPE = "bs_reset";
    private static final String KEY_BS_PREFIX = "bs_prefix";
    private static final String KEY_BS_SEED = "bs_seed";
    private static final String KEY_BS_CURRENT_BILL = "bs_current_bill";
    private static final String KEY_BS_CUSOMER_SELECTION = "bs_customer_selection";
    private static final String KEY_BS_CASHIER_SELECTION = "bs_cashier_selection";
    private static final String KEY_BS_ROUND_OFF = "bs_roundOff";
    private static final String KEY_BS_CREATED_AT = "bs_createdAt";
    private static final String KEY_BS_DEFAULT = "bs_default";

    //User Master table name
    private static final String USER_MST_TABLE_NAME = "UserMst";
    //Bill Series column names
    private static final String KEY_UM_ID = "um_id";
    private static final String KEY_UM_USER = "um_user";
    private static final String KEY_UM_PASSWORD = "um_password";
    private static final String KEY_UM_IS_ADMIN = "um_isAdmin";
    private static final String KEY_UM_CREATED_AT = "um_createdAt";

    private List<Item> itemList = new ArrayList<>();
    private List<Item> itemList1 = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> categoryList1 = new ArrayList<>();
    private ArrayList<Customer> customerList = new ArrayList<>();
    private List<Unit> unitList = new ArrayList<>();
    private List<ReportData> totalCategory = new ArrayList<>();
    private List<ReportData> totalItem = new ArrayList<>();
    private List<ReportData> totalReport = new ArrayList<>();
    private List<ReportData> totalReport1 = new ArrayList<>();
    private List<CustomerReport> customerReport = new ArrayList<>();
    private ArrayList<BillSeries> billListSeries = new ArrayList<>();

    //Hsn Master Table Name
    private static final String HSN_MST_TABLE_NAME = "hsnmst";
    //Hsn Master Column Names
    private static final String KEY_HSN_ID = "hsn_id";
    private static final String KEY_HSN_CODE = "hsn_code";
    private static final String KEY_HSN_DESC = "hsn_desc";
    private static final String KEY_HSN_IS_ACTIVE = "hsn_is_active";
    private static final String KEY_HSN_CREATED_AT = "hsn_createdAt";

    // Tax Master Table Name
    private static final String TAX_MST_TABLE_NAME = "taxmst";
    // Tax Master Column Name
    private static final String KEY_TAXM_CODE = "tax_code";
    private static final String KEY_TAXM_TYPE = "tax_type";
    private static final String KEY_TAXM_RATE = "tax_rate";
    private static final String KEY_TAXM_IS_ACTIVE = "taxm_is_active";
    private static final String KEY_TAXM_IGSTCODE = "tax_igstcode";
    private static final String KEY_TAXM_CREATED_AT = "tax_createdAt";


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE_NAME + "("
                + KEY_CAT_ID + " INTEGER PRIMARY KEY,"
                + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_IMAGE_PATH + " TEXT,"
                + KEY_CAT_IS_ACTIVE + " INTEGER, "
                + KEY_CAT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE_NAME + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_UOM + " TEXT,"
                + KEY_ITEM_CP + " FLOAT,"
                + KEY_ITEM_SP + " FLOAT,"
                + KEY_ITEM_HSN + " TEXT,"
                + KEY_ITEM_GST + " FLOAT,"
                + KEY_ITEM_IS_ACTIVE + " INTEGER, "
                + KEY_CATE_ID + " INTEGER,"
                + KEY_IMAGE_PATH + " TEXT, " + KEY_ITEM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_ITEM_TABLE);

        String CREATE_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + UNIT_TABLE_NAME + "("
                + KEY_UNIT_ID + " INTEGER PRIMARY KEY,"
                + KEY_UNIT_DESC + " TEXT,"
                + KEY_UNIT_DECIMAL_ALLOWED + "INTEGER,"
                + KEY_UNIT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_UNIT_TABLE);

        String CREATE_SALES_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + SALES_MST_TABLE_NAME + "("
                + KEY_SM_BILL_NO + " INTEGER PRIMARY KEY,"
                + KEY_SM_DATE + " INTEGER,"
                + KEY_SM_SALE_BILL_NO + " INTEGER,"
                + KEY_SM_QTY + " FLOAT,"
                + KEY_SM_ITEM + " FLOAT,"
                + KEY_SM_NET_AMT + " FLOAT,"
                + KEY_SM_DISCOUNT + " FLOAT,"
                + KEY_SM_SALESMAN + " TEXT,"
                + KEY_SM_CUSTOMER + " INTEGER,"
                + KEY_SM_CUSTOMER_NAME + " TEXT,"
                + KEY_SM_CASHIER_NAME + " TEXT,"
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
                + KEY_SD_NET_RATE + " FLOAT,"
                + KEY_SD_RATE + " FLOAT,"
                + KEY_SD_AMOUNT + " FLOAT,"
                + KEY_SD_DATETIME + " TEXT,"
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

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE_NAME + "("
                + KEY_CST_ID + " INTEGER PRIMARY KEY,"
                + KEY_CST_NAME + " TEXT,"
                + KEY_CST_NUMBER + " TEXT UNIQUE,"
                + KEY_CST_EMAIL + " TEXT,"
                + KEY_CST_GSTIN + " TEXT,"
                + KEY_CST_ADDRESS1 + " TEXT,"
                + KEY_CST_ADDRESS2 + " TEXT,"
                + KEY_CST_CITY + " TEXT,"
                + KEY_CST_STATE + " TEXT,"
                + KEY_CST_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);

        String CREATE_BILL_SERIES_TABLE = "CREATE TABLE IF NOT EXISTS " + BILLSERIES_TABLE_NAME + "("
                + KEY_BS_ID + " INTEGER PRIMARY KEY," + KEY_BS_NAME + " TEXT,"
                + KEY_BS_SHORT_NAME + " TEXT,"
                + KEY_BS_SEED + " INTEGER,"
                + KEY_BS_CURRENT_BILL + " INTEGER,"
                + KEY_BS_RESET_TYPE + " TEXT,"
                + KEY_BS_PREFIX + " TEXT,"
                + KEY_BS_CUSOMER_SELECTION + " TEXT,"
                + KEY_BS_CASHIER_SELECTION + " TEXT,"
                + KEY_BS_DEFAULT + " INTEGER,"
                + KEY_BS_ROUND_OFF + " INTEGER,"
                + KEY_BS_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_BILL_SERIES_TABLE);
        addBillSeries0(new BillSeries("GENERAL", "GEN", "YEARLY", "", 1, 1, "YES", "NO", 0, 1), db);

        String CREATE_USER_MASTER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_MST_TABLE_NAME + "("
                + KEY_UM_ID + " INTEGER PRIMARY KEY,"
                + KEY_UM_USER + " TEXT UNIQUE,"
                + KEY_UM_PASSWORD + " TEXT,"
                + KEY_UM_IS_ADMIN + " INTEGER,"
                + KEY_UM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_USER_MASTER_TABLE);
        ContentValues cv = new ContentValues();
        cv.put(KEY_UM_USER, "maitri");
        cv.put(KEY_UM_PASSWORD, "misplmca");
        cv.put(KEY_UM_IS_ADMIN, 1);
        db.insert(USER_MST_TABLE_NAME, null, cv);
        ContentValues cv1 = new ContentValues();
        cv1.put(KEY_UM_USER, "admin");
        cv1.put(KEY_UM_PASSWORD, "admin");
        cv1.put(KEY_UM_IS_ADMIN, 1);
        db.insert(USER_MST_TABLE_NAME, null, cv1);
        String addBarcodeInItemMst = "ALTER TABLE " + ITEM_TABLE_NAME +
                " ADD COLUMN " + KEY_ITEM_BARCODE + " TEXT ";
        db.execSQL(addBarcodeInItemMst);

        String CREATE_HSN_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + HSN_MST_TABLE_NAME + "("
                + KEY_HSN_ID + " INTEGER PRIMARY KEY,"
                + KEY_HSN_CODE + " INTEGER,"
                + KEY_HSN_DESC + " TEXT,"
                + KEY_HSN_IS_ACTIVE + " INTEGER,"
                + KEY_HSN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_HSN_MST_TABLE);

        String CREATE_TAX_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + TAX_MST_TABLE_NAME + "("
                + KEY_TAXM_CODE + " INTEGER PRIMARY KEY,"
                + KEY_TAXM_TYPE + " TEXT,"
                + KEY_TAXM_RATE + " FLOAT,"
                + KEY_TAXM_IS_ACTIVE + " INTEGER,"
                + KEY_TAXM_IGSTCODE + " INTEGER,"
                + KEY_TAXM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_TAX_MST_TABLE);
        insertIntoTaxMst(db);

        String addGstIdItemMst = "ALTER TABLE " + ITEM_TABLE_NAME +
                " ADD COLUMN " + KEY_ITEM_GST_ID + " INTEGER ";
        db.execSQL(addGstIdItemMst);

        String addTaxTypeToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_TAX_TYPE + " TEXT ";
        db.execSQL(addTaxTypeToSalesMst);
        String addTaxCode1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_CODE1 + " INTEGER ";
        db.execSQL(addTaxCode1SalesDet);
        String addTaxRate1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_RATE1 + " FLOAT ";
        db.execSQL(addTaxRate1SalesDet);
        String addTaxAmt1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_AMT1 + " FLOAT ";
        db.execSQL(addTaxAmt1SalesDet);
        String addTaxCode2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_CODE2 + " INTEGER ";
        db.execSQL(addTaxCode2SalesDet);
        String addTaxRate2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_RATE2 + " FLOAT ";
        db.execSQL(addTaxRate2SalesDet);
        String addTaxAmt2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_TAX_AMT2 + " FLOAT ";
        db.execSQL(addTaxAmt2SalesDet);
        String addGstSaleAmtSalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_GST_SALE_AMT + " FLOAT ";
        db.execSQL(addGstSaleAmtSalesDet);
        String addDiscountSalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                " ADD COLUMN " + KEY_SD_DISCOUNT + " FLOAT ";
        db.execSQL(addDiscountSalesDet);
        String addRoundOffToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_ROUNDOFF + " TEXT ";
        db.execSQL(addRoundOffToSalesMst);

        String addIsBillCancelledToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_IS_CANCELLED + " INTEGER DEFAULT 0";
        db.execSQL(addIsBillCancelledToSalesMst);
        String addBillCancelDateTimeSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_CANCEL_DATETIME + " DATETIME ";
        db.execSQL(addBillCancelDateTimeSalesMst);
        String addBillCancelCommentToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_CANCEL_COMMENT + " TEXT ";
        db.execSQL(addBillCancelCommentToSalesMst);
        String addBillCancelPersonToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_CANCEL_PERSON + " TEXT ";
        db.execSQL(addBillCancelPersonToSalesMst);
        String addTaxAmt1ToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_TAX_AMT1 + " FLOAT ";
        db.execSQL(addTaxAmt1ToSalesMst);
        String addTaxAmt2ToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_TAX_AMT2 + " FLOAT ";
        db.execSQL(addTaxAmt2ToSalesMst);
        String addGstSaleAmtToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                " ADD COLUMN " + KEY_SM_GST_SALE_AMT + " FLOAT ";
        db.execSQL(addGstSaleAmtToSalesMst);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion + 1) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE_NAME + "("
                        + KEY_CST_ID + " INTEGER PRIMARY KEY,"
                        + KEY_CST_NAME + " TEXT,"
                        + KEY_CST_NUMBER + " TEXT UNIQUE,"
                        + KEY_CST_EMAIL + " TEXT,"
                        + KEY_CST_GSTIN + " TEXT,"
                        + KEY_CST_ADDRESS1 + " TEXT,"
                        + KEY_CST_ADDRESS2 + " TEXT,"
                        + KEY_CST_CITY + " TEXT,"
                        + KEY_CST_STATE + " TEXT,"
                        + KEY_CST_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_CUSTOMER_TABLE);
            case 8:
                String addCategoryIsActive = "ALTER TABLE " + CATEGORY_TABLE_NAME +
                        " ADD COLUMN " + KEY_CAT_IS_ACTIVE + " INTEGER ";
                db.execSQL(addCategoryIsActive);
                String addValueToCategoryIsActive = "UPDATE " + CATEGORY_TABLE_NAME +
                        " SET " + KEY_CAT_IS_ACTIVE + " = 1";
                db.execSQL(addValueToCategoryIsActive);
                String addItemIsActive = "ALTER TABLE " + ITEM_TABLE_NAME +
                        " ADD COLUMN " + KEY_ITEM_IS_ACTIVE + " INTEGER ";
                db.execSQL(addItemIsActive);
                String addValueToItemIsActive = "UPDATE " + ITEM_TABLE_NAME +
                        " SET " + KEY_ITEM_IS_ACTIVE + " = 1";
                db.execSQL(addValueToItemIsActive);
            case 9:
                String CREATE_BILL_SERIES_TABLE = "CREATE TABLE IF NOT EXISTS " + BILLSERIES_TABLE_NAME + "("
                        + KEY_BS_ID + " INTEGER PRIMARY KEY," + KEY_BS_NAME + " TEXT,"
                        + KEY_BS_SHORT_NAME + " TEXT,"
                        + KEY_BS_SEED + " INTEGER,"
                        + KEY_BS_CURRENT_BILL + " INTEGER,"
                        + KEY_BS_RESET_TYPE + " TEXT,"
                        + KEY_BS_PREFIX + " TEXT,"
                        + KEY_BS_CUSOMER_SELECTION + " TEXT,"
                        + KEY_BS_DEFAULT + " INTEGER,"
                        + KEY_BS_ROUND_OFF + " INTEGER,"
                        + KEY_BS_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_BILL_SERIES_TABLE);
                addBillSeries0(new BillSeries("GENERAL", "GEN", "YEARLY", "", 1, 1, "YES", "", 0, 1), db);
            case 10:
                String CREATE_USER_MASTER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_MST_TABLE_NAME + "("
                        + KEY_UM_ID + " INTEGER PRIMARY KEY,"
                        + KEY_UM_USER + " TEXT UNIQUE,"
                        + KEY_UM_PASSWORD + " TEXT,"
                        + KEY_UM_IS_ADMIN + " INTEGER,"
                        + KEY_UM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_USER_MASTER_TABLE);
                ContentValues cv = new ContentValues();
                cv.put(KEY_UM_USER, "maitri");
                cv.put(KEY_UM_PASSWORD, "misplmca");
                cv.put(KEY_UM_IS_ADMIN, 1);
                db.insert(USER_MST_TABLE_NAME, null, cv);
                ContentValues cv1 = new ContentValues();
                cv1.put(KEY_UM_USER, "admin");
                cv1.put(KEY_UM_PASSWORD, "admin");
                cv1.put(KEY_UM_IS_ADMIN, 1);
                db.insert(USER_MST_TABLE_NAME, null, cv1);

                String addSaleBillNo = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_SALE_BILL_NO + " INTEGER ";
                db.execSQL(addSaleBillNo);
                String addcashierName = "ALTER TABLE " + BILLSERIES_TABLE_NAME +
                        " ADD COLUMN " + KEY_BS_CASHIER_SELECTION + " TEXT ";
                db.execSQL(addcashierName);
                updateCashierRequired("NO", db);
            case 11:
                String addcustName = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_CUSTOMER_NAME + " TEXT ";
                db.execSQL(addcustName);
                String addcashName = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_CASHIER_NAME + " TEXT ";
                db.execSQL(addcashName);
            case 12:
                String updateSaleBill = "Update " + SALES_MST_TABLE_NAME + " SET " +
                        KEY_SM_SALE_BILL_NO + " = " + KEY_SM_BILL_NO;
                db.execSQL(updateSaleBill);
                String updateCashier = "Update " + SALES_MST_TABLE_NAME + " SET " +
                        KEY_SM_CASHIER_NAME + " =  ''";
                db.execSQL(updateCashier);
                String updateCustomer = "Update " + SALES_MST_TABLE_NAME + " SET " +
                        KEY_SM_CUSTOMER_NAME + " =  ''";
                db.execSQL(updateCustomer);
            case 13:
                String addDateColumn = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_DATETIME + " TEXT ";
                db.execSQL(addDateColumn);
                String dateInSalesDet = "UPDATE SalesDet \n" +
                        "SET  sd_datetime = (SELECT sm_datetime\n" +
                        "                  FROM SalesMst\n" +
                        "                  WHERE  sm_Sale_billNo = salesDet.sd_billNo)";
                db.execSQL(dateInSalesDet);
            case 14:
                String updateCustomer1 = "update salesmst set sm_customer_name = '' where sm_customer_name like '% '";
                db.execSQL(updateCustomer1);
            case 15:
                String addBarcodeInItemMst = "ALTER TABLE " + ITEM_TABLE_NAME +
                        " ADD COLUMN " + KEY_ITEM_BARCODE + " TEXT ";
                ;
                db.execSQL(addBarcodeInItemMst);
            case 16:
                String CREATE_HSN_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + HSN_MST_TABLE_NAME + "("
                        + KEY_HSN_ID + " INTEGER PRIMARY KEY,"
                        + KEY_HSN_CODE + " INTEGER,"
                        + KEY_HSN_DESC + " TEXT,"
                        + KEY_HSN_IS_ACTIVE + " INTEGER,"
                        + KEY_HSN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_HSN_MST_TABLE);

                String CREATE_TAX_MST_TABLE = "CREATE TABLE IF NOT EXISTS " + TAX_MST_TABLE_NAME + "("
                        + KEY_TAXM_CODE + " INTEGER PRIMARY KEY,"
                        + KEY_TAXM_TYPE + " TEXT,"
                        + KEY_TAXM_RATE + " FLOAT,"
                        + KEY_TAXM_IS_ACTIVE + " INTEGER,"
                        + KEY_TAXM_IGSTCODE + " INTEGER,"
                        + KEY_TAXM_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_TAX_MST_TABLE);
                insertIntoTaxMst(db);
                String addGstIdItemMst = "ALTER TABLE " + ITEM_TABLE_NAME +
                        " ADD COLUMN " + KEY_ITEM_GST_ID + " INTEGER ";
                db.execSQL(addGstIdItemMst);

            case 17:
                db.execSQL("DROP TABLE " + UNIT_TABLE_NAME);
                String CREATE_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + UNIT_TABLE_NAME + "("
                        + KEY_UNIT_ID + " INTEGER PRIMARY KEY,"
                        + KEY_UNIT_DESC + " TEXT,"
                        + KEY_UNIT_DECIMAL_ALLOWED + " INTEGER,"
                        + KEY_UNIT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
                db.execSQL(CREATE_UNIT_TABLE);

            case 18:
                String addTaxTypeToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_TAX_TYPE + " TEXT ";
                db.execSQL(addTaxTypeToSalesMst);
                String addTaxCode1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_CODE1 + " INTEGER ";
                db.execSQL(addTaxCode1SalesDet);
                String addTaxRate1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_RATE1 + " FLOAT ";
                db.execSQL(addTaxRate1SalesDet);
                String addTaxAmt1SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_AMT1 + " FLOAT ";
                db.execSQL(addTaxAmt1SalesDet);
                String addTaxCode2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_CODE2 + " INTEGER ";
                db.execSQL(addTaxCode2SalesDet);
                String addTaxRate2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_RATE2 + " FLOAT ";
                db.execSQL(addTaxRate2SalesDet);
                String addTaxAmt2SalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_TAX_AMT2 + " FLOAT ";
                db.execSQL(addTaxAmt2SalesDet);
                String addGstSaleAmtSalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_GST_SALE_AMT + " FLOAT ";
                db.execSQL(addGstSaleAmtSalesDet);
                String addDiscountSalesDet = "ALTER TABLE " + SALES_DET_TABLE_NAME +
                        " ADD COLUMN " + KEY_SD_DISCOUNT + " FLOAT ";
                db.execSQL(addDiscountSalesDet);

            case 19:
                String addRoundOffToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_ROUNDOFF + " TEXT ";
                db.execSQL(addRoundOffToSalesMst);
            case 20:
                String addIsBillCancelledToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_IS_CANCELLED + " INTEGER DEFAULT 0";
                db.execSQL(addIsBillCancelledToSalesMst);
                String addBillCancelDateTimeSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_CANCEL_DATETIME + " DATETIME ";
                db.execSQL(addBillCancelDateTimeSalesMst);
                String addBillCancelCommentToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_CANCEL_COMMENT + " TEXT ";
                db.execSQL(addBillCancelCommentToSalesMst);
                String addBillCancelPersonToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_CANCEL_PERSON + " TEXT ";
                db.execSQL(addBillCancelPersonToSalesMst);
                String addTaxAmt1ToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_TAX_AMT1 + " FLOAT ";
                db.execSQL(addTaxAmt1ToSalesMst);
                String addTaxAmt2ToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_TAX_AMT2 + " FLOAT ";
                db.execSQL(addTaxAmt2ToSalesMst);
                String addGstSaleAmtToSalesMst = "ALTER TABLE " + SALES_MST_TABLE_NAME +
                        " ADD COLUMN " + KEY_SM_GST_SALE_AMT + " FLOAT ";
                db.execSQL(addGstSaleAmtToSalesMst);
                break;
        }
    }

    private void insertIntoTaxMst(SQLiteDatabase db) {
        ContentValues txm = new ContentValues();
        txm.put(KEY_TAXM_CODE, 1);
        txm.put(KEY_TAXM_TYPE, "CGST");
        txm.put(KEY_TAXM_RATE, 0);
        txm.put(KEY_TAXM_IS_ACTIVE, 1);
        txm.put(KEY_TAXM_IGSTCODE, 3);
        db.insert(TAX_MST_TABLE_NAME, null, txm);

        ContentValues txm1 = new ContentValues();
        txm1.put(KEY_TAXM_CODE, 2);
        txm1.put(KEY_TAXM_TYPE, "SGST");
        txm1.put(KEY_TAXM_RATE, 0);
        txm1.put(KEY_TAXM_IS_ACTIVE, 1);
        txm1.put(KEY_TAXM_IGSTCODE, 3);
        db.insert(TAX_MST_TABLE_NAME, null, txm1);

        ContentValues txm2 = new ContentValues();
        txm2.put(KEY_TAXM_CODE, 3);
        txm2.put(KEY_TAXM_TYPE, "IGST");
        txm2.put(KEY_TAXM_RATE, 0);
        txm2.put(KEY_TAXM_IS_ACTIVE, 1);
        txm2.put(KEY_TAXM_IGSTCODE, 3);
        db.insert(TAX_MST_TABLE_NAME, null, txm2);

        ContentValues txm3 = new ContentValues();
        txm3.put(KEY_TAXM_CODE, 4);
        txm3.put(KEY_TAXM_TYPE, "CGST");
        txm3.put(KEY_TAXM_RATE, 1.5);
        txm3.put(KEY_TAXM_IS_ACTIVE, 1);
        txm3.put(KEY_TAXM_IGSTCODE, 6);
        db.insert(TAX_MST_TABLE_NAME, null, txm3);

        ContentValues txm4 = new ContentValues();
        txm4.put(KEY_TAXM_CODE, 5);
        txm4.put(KEY_TAXM_TYPE, "SGST");
        txm4.put(KEY_TAXM_RATE, 1.5);
        txm4.put(KEY_TAXM_IS_ACTIVE, 1);
        txm4.put(KEY_TAXM_IGSTCODE, 6);
        db.insert(TAX_MST_TABLE_NAME, null, txm4);

        ContentValues txm5 = new ContentValues();
        txm5.put(KEY_TAXM_CODE, 6);
        txm5.put(KEY_TAXM_TYPE, "IGST");
        txm5.put(KEY_TAXM_RATE, 3);
        txm5.put(KEY_TAXM_IS_ACTIVE, 1);
        txm5.put(KEY_TAXM_IGSTCODE, 6);
        db.insert(TAX_MST_TABLE_NAME, null, txm5);

        ContentValues txm6 = new ContentValues();
        txm6.put(KEY_TAXM_CODE, 7);
        txm6.put(KEY_TAXM_TYPE, "CGST");
        txm6.put(KEY_TAXM_RATE, 2.5);
        txm6.put(KEY_TAXM_IS_ACTIVE, 1);
        txm6.put(KEY_TAXM_IGSTCODE, 9);
        db.insert(TAX_MST_TABLE_NAME, null, txm6);

        ContentValues txm7 = new ContentValues();
        txm7.put(KEY_TAXM_CODE, 8);
        txm7.put(KEY_TAXM_TYPE, "SGST");
        txm7.put(KEY_TAXM_RATE, 2.5);
        txm7.put(KEY_TAXM_IS_ACTIVE, 1);
        txm7.put(KEY_TAXM_IGSTCODE, 9);
        db.insert(TAX_MST_TABLE_NAME, null, txm7);

        ContentValues txm8 = new ContentValues();
        txm8.put(KEY_TAXM_CODE, 9);
        txm8.put(KEY_TAXM_TYPE, "IGST");
        txm8.put(KEY_TAXM_RATE, 5);
        txm8.put(KEY_TAXM_IS_ACTIVE, 1);
        txm8.put(KEY_TAXM_IGSTCODE, 9);
        db.insert(TAX_MST_TABLE_NAME, null, txm8);

        ContentValues txm9 = new ContentValues();
        txm9.put(KEY_TAXM_CODE, 10);
        txm9.put(KEY_TAXM_TYPE, "CGST");
        txm9.put(KEY_TAXM_RATE, 6);
        txm9.put(KEY_TAXM_IS_ACTIVE, 1);
        txm9.put(KEY_TAXM_IGSTCODE, 12);
        db.insert(TAX_MST_TABLE_NAME, null, txm9);

        ContentValues txm10 = new ContentValues();
        txm10.put(KEY_TAXM_CODE, 11);
        txm10.put(KEY_TAXM_TYPE, "SGST");
        txm10.put(KEY_TAXM_RATE, 6);
        txm10.put(KEY_TAXM_IS_ACTIVE, 1);
        txm10.put(KEY_TAXM_IGSTCODE, 12);
        db.insert(TAX_MST_TABLE_NAME, null, txm10);

        ContentValues txm11 = new ContentValues();
        txm11.put(KEY_TAXM_CODE, 12);
        txm11.put(KEY_TAXM_TYPE, "IGST");
        txm11.put(KEY_TAXM_RATE, 12);
        txm11.put(KEY_TAXM_IS_ACTIVE, 1);
        txm11.put(KEY_TAXM_IGSTCODE, 12);
        db.insert(TAX_MST_TABLE_NAME, null, txm11);

        ContentValues txm12 = new ContentValues();
        txm12.put(KEY_TAXM_CODE, 13);
        txm12.put(KEY_TAXM_TYPE, "CGST");
        txm12.put(KEY_TAXM_RATE, 9);
        txm12.put(KEY_TAXM_IS_ACTIVE, 1);
        txm12.put(KEY_TAXM_IGSTCODE, 15);
        db.insert(TAX_MST_TABLE_NAME, null, txm12);

        ContentValues txm13 = new ContentValues();
        txm13.put(KEY_TAXM_CODE, 14);
        txm13.put(KEY_TAXM_TYPE, "SGST");
        txm13.put(KEY_TAXM_RATE, 9);
        txm13.put(KEY_TAXM_IS_ACTIVE, 1);
        txm13.put(KEY_TAXM_IGSTCODE, 15);
        db.insert(TAX_MST_TABLE_NAME, null, txm13);

        ContentValues txm14 = new ContentValues();
        txm14.put(KEY_TAXM_CODE, 15);
        txm14.put(KEY_TAXM_TYPE, "IGST");
        txm14.put(KEY_TAXM_RATE, 18);
        txm14.put(KEY_TAXM_IS_ACTIVE, 1);
        txm14.put(KEY_TAXM_IGSTCODE, 15);
        db.insert(TAX_MST_TABLE_NAME, null, txm14);

        ContentValues txm15 = new ContentValues();
        txm15.put(KEY_TAXM_CODE, 16);
        txm15.put(KEY_TAXM_TYPE, "CGST");
        txm15.put(KEY_TAXM_RATE, 14);
        txm15.put(KEY_TAXM_IS_ACTIVE, 1);
        txm15.put(KEY_TAXM_IGSTCODE, 18);
        db.insert(TAX_MST_TABLE_NAME, null, txm15);

        ContentValues txm16 = new ContentValues();
        txm16.put(KEY_TAXM_CODE, 17);
        txm16.put(KEY_TAXM_TYPE, "SGST");
        txm16.put(KEY_TAXM_RATE, 14);
        txm16.put(KEY_TAXM_IS_ACTIVE, 1);
        txm16.put(KEY_TAXM_IGSTCODE, 18);
        db.insert(TAX_MST_TABLE_NAME, null, txm16);

        ContentValues txm17 = new ContentValues();
        txm17.put(KEY_TAXM_CODE, 18);
        txm17.put(KEY_TAXM_TYPE, "IGST");
        txm17.put(KEY_TAXM_RATE, 28);
        txm17.put(KEY_TAXM_IS_ACTIVE, 1);
        txm17.put(KEY_TAXM_IGSTCODE, 18);
        db.insert(TAX_MST_TABLE_NAME, null, txm17);
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

    // Getting All categorys
    public List<Category> getAllcategorys1() {
//        categoryList.clear();
        categoryList1.clear();
        Category cat = new Category();
        cat.setCategoryName("All");
        categoryList1.add(cat);
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE_NAME + " ORDER BY " + KEY_CAT_NAME;

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
                categoryList1.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
      /*  Collections.sort(categoryList, new Category.OrderByCatName());
        categoryList1.addAll(categoryList);*/
        // return category list
        return categoryList1;
    }


    public int getDateCount(String date) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "Select Cast ((JulianDay('" + date + "') - JulianDay('1900-01-01')) As Integer) as date";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null)
                cursor.moveToFirst();

            int c = cursor.getInt(0);

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
            cv.put(KEY_ITEM_BARCODE, item.getBarcode());
            cv.put(KEY_ITEM_GST_ID, item.getGstId());
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
            item.setBarcode(c.getString(c.getColumnIndex(KEY_ITEM_BARCODE)));
            item.setGstId(c.getInt(c.getColumnIndex(KEY_ITEM_GST_ID)));
            c.close();
            return item;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return new Item();
    }

    public Item getItemUsingBarCode(String barcode) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME + " WHERE "
                    + KEY_ITEM_BARCODE + " = " + barcode;
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null && c.moveToFirst()) {

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
                item.setBarcode(c.getString(c.getColumnIndex(KEY_ITEM_BARCODE)));
                item.setGstId(c.getInt(c.getColumnIndex(KEY_ITEM_GST_ID)));
                c.close();
                return item;
            }
        } catch (SQLException e) {
            return new Item();
        }
        return new Item();
    }

    // Getting All item
    public List<Item> getAllitems(int categoryId) {
        itemList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME + " WHERE " + KEY_CATE_ID + " =  " + categoryId;
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
                    item.setBarcode(c.getString(c.getColumnIndex(KEY_ITEM_BARCODE)));
                    item.setGstId(c.getInt(c.getColumnIndex(KEY_ITEM_GST_ID)));
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

    public List<Item> getAllitems1() {
        itemList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME;
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
                    item.setBarcode(c.getString(c.getColumnIndex(KEY_ITEM_BARCODE)));
                    item.setGstId(c.getInt(c.getColumnIndex(KEY_ITEM_GST_ID)));
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


    public List<Item> getAllitemC() {
        itemList.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME;
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
                    item.setBarcode(c.getString(c.getColumnIndex(KEY_ITEM_BARCODE)));
                    item.setGstId(c.getInt(c.getColumnIndex(KEY_ITEM_GST_ID)));
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
        values.put(KEY_ITEM_BARCODE, item.getBarcode());
        values.put(KEY_ITEM_GST_ID, item.getGstId());

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

    public long addCustomer(Customer customer) {

        long result = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CST_NAME, customer.getName());
            cv.put(KEY_CST_NUMBER, customer.getMobileno());
            cv.put(KEY_CST_GSTIN, customer.getGstin());
            cv.put(KEY_CST_EMAIL, customer.getEmail());
            cv.put(KEY_CST_ADDRESS1, customer.getAddress1());
            cv.put(KEY_CST_ADDRESS2, customer.getAddress2());
            cv.put(KEY_CST_CITY, customer.getCity());
            cv.put(KEY_CST_STATE, customer.getState());
            result = db.insert(CUSTOMER_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }

        return result;
    }

    public boolean updateCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CST_NAME, customer.getName());
            cv.put(KEY_CST_NUMBER, customer.getMobileno());
            cv.put(KEY_CST_GSTIN, customer.getGstin());
            cv.put(KEY_CST_EMAIL, customer.getEmail());
            cv.put(KEY_CST_ADDRESS1, customer.getAddress1());
            cv.put(KEY_CST_ADDRESS2, customer.getAddress2());
            cv.put(KEY_CST_CITY, customer.getCity());
            cv.put(KEY_CST_STATE, customer.getState());
            db.update(CUSTOMER_TABLE_NAME, cv, KEY_CST_ID + " = ?",
                    new String[]{String.valueOf(customer.getId())});
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return false;
    }

    public String getCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT " + KEY_CST_NUMBER + " FROM " + CUSTOMER_TABLE_NAME + " WHERE " + KEY_CST_ID + " = '" + id + "'";
        Log.d("Query", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        String mobileNumber = c.getString(c.getColumnIndex(KEY_CST_NUMBER));
        Log.d("Value", String.valueOf(mobileNumber));
        c.close();
        return mobileNumber;
    }


    // Getting All Customer
    public ArrayList<Customer> getAllCustomer() {
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
                    customer.setAddress1(c.getString(c.getColumnIndex(KEY_CST_ADDRESS1)));
                    customer.setAddress2(c.getString(c.getColumnIndex(KEY_CST_ADDRESS2)));
                    customer.setCity(c.getString(c.getColumnIndex(KEY_CST_CITY)));
                    customer.setState(c.getString(c.getColumnIndex(KEY_CST_STATE)));
                    customer.setEmail(c.getString(c.getColumnIndex(KEY_CST_EMAIL)));
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


    // Getting All item
    public List<SalesDet> getBillDetails(int billNo, int fromDate, int toDate, String dateTime) {
        //     sdList.clear();
        // Select All Query

        List<SalesDet> sdList = new ArrayList<SalesDet>();
        try {

            String selectQuery = "select sm.sm_date,sd.sd_item,sd.sd_billNo,sd.sd_rate,sd.sd_qty,sd.sd_amount,im.item_name as itm_name " +
                    "from SalesDet sd JOIN ItemMst im on sd.sd_item = im.id " +
                    "join SalesMst sm on sd.sd_billNo = sm.sm_sale_billNo and sd.sd_datetime = sm.sm_datetime " +
                    "where " + KEY_SM_BILL_NO + " = " + billNo + " and " + KEY_SD_DATETIME + "='" + dateTime + "'";

            System.out.println(selectQuery);

            //String selectQuery = "SELECT  * FROM " + SALES_DET_TABLE_NAME + " WHERE " + KEY_SD_BILL_NO + " = " + billNo;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {

                    BillItems bm = new BillItems();
                    // sd.setBillNo(c.getInt(c.getColumnIndex(KEY_SD_BILL_NO)));
                    bm.setRate(c.getFloat(c.getColumnIndex(KEY_SD_RATE)));
                    bm.setQty(c.getInt(c.getColumnIndex(KEY_SD_QTY)));
                    bm.setAmount(c.getFloat(c.getColumnIndex(KEY_SD_AMOUNT)));
                    bm.setDesc(c.getString(c.getColumnIndex("itm_name")));

                    System.out.println("DBDESC " + c.getString(c.getColumnIndex("itm_name")));

                    SalesDet sd = new SalesDet(c.getInt(c.getColumnIndex(KEY_SD_BILL_NO)), bm, dateTime);
                /*

                    item.setCategoryId(c.getInt(c.getColumnIndex(KEY_CATE_ID)));
                    item.setItemCP(c.getFloat(c.getColumnIndex(KEY_ITEM_CP)));
                    item.setItemSP(c.getFloat(c.getColumnIndex(KEY_ITEM_SP)));
                    item.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                    item.setItemImage(c.getString(c.getColumnIndex(KEY_IMAGE_PATH)));
                    item.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                    item.setItemGST(c.getFloat(c.getColumnIndex(KEY_ITEM_GST)));
                    item.setItemHSNcode(c.getString(c.getColumnIndex(KEY_ITEM_HSN)));
                    item.setId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));*/
                    // Adding item to list
                    sdList.add(sd);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return sdList;
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
        int id = c.getInt(c.getColumnIndex(KEY_UNIT_ID));
        Log.d("Value", String.valueOf(id));
        c.close();
        return id;
    }


    public List<SalesMst> getAllBills(int fromDate, int toDate) {
        //  customerList.clear();
        // Select All Query
        List<SalesMst> smList = new ArrayList<SalesMst>();

        try {
            String selectQuery = "SELECT  * FROM " + SALES_MST_TABLE_NAME + " WHERE " + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate;
            Log.d("Query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    SalesMst mst = new SalesMst();
                    mst.setInternalBillNo(c.getInt(c.getColumnIndex(KEY_SM_BILL_NO)));
                    mst.setBillNO(c.getInt(c.getColumnIndex(KEY_SM_SALE_BILL_NO)));
                    mst.setDateTime(c.getString(c.getColumnIndex(KEY_SM_DATETIME)));
                    mst.setNetAmt(c.getFloat(c.getColumnIndex(KEY_SM_NET_AMT)));
                    mst.setDiscount(c.getFloat(c.getColumnIndex(KEY_SM_DISCOUNT)));
                    mst.setPaymentMode(c.getString(c.getColumnIndex(KEY_SM_PAYMENT_MODE)));
                    mst.setQty(c.getInt(c.getColumnIndex(KEY_SM_QTY)));
                    mst.setItems(c.getInt(c.getColumnIndex(KEY_SM_ITEM)));
                    mst.setCustomerId(c.getInt(c.getColumnIndex(KEY_SM_CUSTOMER)));
                    mst.setCustName(c.getString(c.getColumnIndex(KEY_SM_CUSTOMER_NAME)));
                    mst.setCashName(c.getString(c.getColumnIndex(KEY_SM_CASHIER_NAME)));
                    int custId = c.getInt(c.getColumnIndex(KEY_SM_CUSTOMER));
                    if (custId != 0) {
                        mst.setCustomerNumber(getCustomer(custId));
                    }
                    mst.setTaxType(c.getString(c.getColumnIndex(KEY_SM_TAX_TYPE)));
                    mst.setRoundOff(c.getFloat(c.getColumnIndex(KEY_SM_ROUNDOFF)));
                    mst.setTaxAmt1(c.getFloat(c.getColumnIndex(KEY_SM_TAX_AMT1)));
                    mst.setTaxAmt2(c.getFloat(c.getColumnIndex(KEY_SM_TAX_AMT2)));
                    mst.setGstNetAmt(c.getFloat(c.getColumnIndex(KEY_SM_GST_SALE_AMT)));
                    smList.add(mst);
                    System.out.println("dbBillNo " + c.getInt(c.getColumnIndex(KEY_SM_BILL_NO)));
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return smList;
    }

    public long addSalesMst(SalesMst salesMst) {
        long result = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_SM_SALE_BILL_NO, salesMst.getBillNO());
            cv.put(KEY_SM_DATE, salesMst.getDate());
            cv.put(KEY_SM_DATETIME, salesMst.getDateTime());
            cv.put(KEY_SM_QTY, salesMst.getQty());
            cv.put(KEY_SM_NET_AMT, salesMst.getNetAmt());
            cv.put(KEY_SM_DISCOUNT, salesMst.getDiscount());
            cv.put(KEY_SM_CUSTOMER, salesMst.getCustomerId());
            cv.put(KEY_SM_ITEM, salesMst.getItems());
            cv.put(KEY_SM_SALESMAN, salesMst.getSalesPerson());
            cv.put(KEY_SM_PAYMENT_MODE, salesMst.getPaymentMode());
            cv.put(KEY_SM_PAYMENT_DET, salesMst.getPaymentDet());
            cv.put(KEY_SM_STATUS, salesMst.getStatus());
            cv.put(KEY_SM_CASHIER_NAME, salesMst.getCashName());
            cv.put(KEY_SM_CUSTOMER_NAME, salesMst.getCustName());
            cv.put(KEY_SM_TAX_TYPE, salesMst.getTaxType());
            cv.put(KEY_SM_ROUNDOFF, salesMst.getRoundOff());
            cv.put(KEY_SM_TAX_AMT1, salesMst.getTaxAmt1());
            cv.put(KEY_SM_TAX_AMT2, salesMst.getTaxAmt2());
            cv.put(KEY_SM_GST_SALE_AMT, salesMst.getGstNetAmt());
            result = db.insert(SALES_MST_TABLE_NAME, null, cv);
            db.close();
            return result;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return result;
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
            cv.put(KEY_SD_NET_RATE, salesDet.billItems.getNet_rate());
            cv.put(KEY_SD_AMOUNT, salesDet.billItems.getAmount());
            cv.put(KEY_SD_DATETIME, salesDet.getDateTime());
            cv.put(KEY_SD_TAX_RATE1, salesDet.billItems.getTaxRate1());
            cv.put(KEY_SD_TAX_AMT1, salesDet.billItems.getTaxAmt1());
            cv.put(KEY_SD_TAX_RATE2, salesDet.billItems.getTaxRate2());
            cv.put(KEY_SD_TAX_AMT2, salesDet.billItems.getTaxAmt2());
            cv.put(KEY_SD_GST_SALE_AMT, salesDet.billItems.getTaxSaleAmt());
            result = db.insert(SALES_DET_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return result;
    }

    public List<ReportData> getTotalReport(int fromDate, int toDate) {
        totalReport.clear();
        try {
            String selectQuery = "SELECT cast(SUM(SD." + KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
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

    public List<ReportData> getTotalReport1(int fromDate, int toDate) {
        totalReport1.clear();
        try {
            String selectQuery = "SELECT cast(SUM(SM." + KEY_SM_DISCOUNT + ")as text) AS sDISCOUNT, "
                    + "cast(SUM(SM." + KEY_SM_NET_AMT + ")as text) AS sAMT "
                    + "FROM " + SALES_MST_TABLE_NAME + " AS SM "
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate;
            Log.d("Query", selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    ReportData reportData = new ReportData();
                    reportData.setrDiscount(c.getString(c.getColumnIndex("sDISCOUNT")));
                    reportData.setrNetSales(c.getString(c.getColumnIndex("sAMT")));
                    totalReport1.add(reportData);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return totalReport1;
    }

    public List<ReportData> getTotalCategoryReport(int fromDate, int toDate) {
        totalCategory.clear();
        try {
            String selectQuery = "SELECT CAT." + KEY_CAT_NAME
                    + ", cast(SUM(SD." + KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SUM(SD." + KEY_SD_RATE + ")as text) AS sRATE, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
                    + " INNER JOIN " + CATEGORY_TABLE_NAME + " AS CAT ON CAT." + KEY_CAT_ID + " = SD." + KEY_SD_CATEGORY
                    + " INNER JOIN " + SALES_MST_TABLE_NAME + " AS SM ON SM." + KEY_SM_BILL_NO + " = SD." + KEY_SD_BILL_NO
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate
                    + " GROUP BY CAT." + KEY_CAT_NAME;
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

    public List<ReportData> getTotalItemReport(int fromDate, int toDate, String filter) {
        totalItem.clear();
        try {
            String selectQuery = "SELECT CAT." + KEY_CAT_NAME + " ,ITM." + KEY_ITEM_NAME
                    + ", cast(SUM(SD." + KEY_SD_QTY + ")as text) AS sQTY, "
                    + "cast(SD." + KEY_SD_RATE + " as text) AS sRATE, "
                    + "cast(SUM(SD." + KEY_SD_AMOUNT + ")as text) AS sAMT "
                    + "FROM " + SALES_DET_TABLE_NAME + " AS SD "
                    + " INNER JOIN " + CATEGORY_TABLE_NAME + " AS CAT ON CAT." + KEY_CAT_ID + " = SD." + KEY_SD_CATEGORY
                    + " INNER JOIN " + ITEM_TABLE_NAME + " AS ITM ON ITM." + KEY_ITEM_ID + " = SD." + KEY_SD_ITEM
                    + " INNER JOIN " + SALES_MST_TABLE_NAME + " AS SM ON SM." + KEY_SM_SALE_BILL_NO + " = SD." + KEY_SD_BILL_NO + " AND SM." + KEY_SM_DATETIME + " = SD." + KEY_SD_DATETIME
                    + " WHERE SM." + KEY_SM_DATE + " BETWEEN " + fromDate + " AND " + toDate + filter
                    + " GROUP BY CAT." + KEY_CAT_NAME + " ,ITM." + KEY_ITEM_NAME + ",SD." + KEY_SD_RATE;
            Log.d("Query", selectQuery);
            Log.e("Query", selectQuery);
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


    public List<CustomerReport> getTotalCustomerReport(int fromDate, int toDate) {
        customerReport.clear();
        try {
            String query = "select  sum(" + KEY_SM_DISCOUNT + ") as tDiscount,sum(" + KEY_SM_QTY +
                    ") as tQty,sum(" + KEY_SM_NET_AMT + ") as tNetAmt,sum(" + KEY_SM_ITEM +
                    ") as tItem," + KEY_SM_CUSTOMER_NAME + " ,ifnull(cst_number,'') as cst_number from " +
                    SALES_MST_TABLE_NAME + "   LEFT join CustomerMst1 on sm_customer = id   where " +
                    KEY_SM_DATE + " between " + fromDate + " and " + toDate +
                    " group by " + KEY_SM_CUSTOMER_NAME;
            Log.d("CustomerReportQuery", query);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Log.i("cNetAmt", String.valueOf(c.getInt(c.getColumnIndex("tNetAmt"))));
                    CustomerReport cReport = new CustomerReport();
                    cReport.setcName(c.getString(c.getColumnIndex(KEY_SM_CUSTOMER_NAME)));
                    cReport.setcNumber(c.getString(c.getColumnIndex("cst_number")));
                    cReport.setcItemCount(c.getInt(c.getColumnIndex("tItem")));
                    cReport.setcNetAmt(c.getInt(c.getColumnIndex("tNetAmt")));
                    cReport.setcQtyCount(c.getInt(c.getColumnIndex("tQty")));
                    customerReport.add(cReport);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return customerReport;
    }


    public void addBillSeries0(BillSeries bill, SQLiteDatabase db) {
        try {
            //  SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_BS_NAME, bill.getBillName());
            cv.put(KEY_BS_SHORT_NAME, bill.getShortName());
            cv.put(KEY_BS_PREFIX, bill.getPrefix());
            cv.put(KEY_BS_RESET_TYPE, bill.getResetType());
            cv.put(KEY_BS_SEED, bill.getSeed());
            cv.put(KEY_BS_CURRENT_BILL, bill.getCurrentBillNo());
            cv.put(KEY_BS_CUSOMER_SELECTION, bill.getCustomerSelection());
            if (!bill.getCashierSelection().isEmpty()) {
                cv.put(KEY_BS_CASHIER_SELECTION, bill.getCashierSelection());
            }
            cv.put(KEY_BS_ROUND_OFF, bill.getRoundOff());
            cv.put(KEY_BS_DEFAULT, bill.getDefault_bill());
            db.insert(BILLSERIES_TABLE_NAME, null, cv);

        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }


    public void addBillSeries(BillSeries bill) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_BS_NAME, bill.getBillName());
            cv.put(KEY_BS_SHORT_NAME, bill.getShortName());
            cv.put(KEY_BS_PREFIX, bill.getPrefix());
            cv.put(KEY_BS_RESET_TYPE, bill.getResetType());
            cv.put(KEY_BS_SEED, bill.getSeed());
            cv.put(KEY_BS_CURRENT_BILL, bill.getCurrentBillNo());
            cv.put(KEY_BS_CUSOMER_SELECTION, bill.getCustomerSelection());
            cv.put(KEY_BS_ROUND_OFF, bill.getRoundOff());
            cv.put(KEY_BS_DEFAULT, bill.getDefault_bill());
            db.insert(BILLSERIES_TABLE_NAME, null, cv);
            db.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
    }

    public BillSeries getBillSeries(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT  * FROM " + BILLSERIES_TABLE_NAME + " WHERE "
                    + KEY_BS_DEFAULT + " = " + id;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null)
                c.moveToFirst();
            BillSeries billSeries = new BillSeries();
            billSeries.setBillName(c.getString(c.getColumnIndex(KEY_BS_NAME)));
            billSeries.setShortName(c.getString(c.getColumnIndex(KEY_BS_SHORT_NAME)));
            billSeries.setSeed(c.getInt(c.getColumnIndex(KEY_BS_SEED)));
            billSeries.setCurrentBillNo(c.getInt(c.getColumnIndex(KEY_BS_CURRENT_BILL)));
            billSeries.setCustomerSelection(c.getString(c.getColumnIndex(KEY_BS_CUSOMER_SELECTION)));
            billSeries.setCashierSelection(c.getString(c.getColumnIndex(KEY_BS_CASHIER_SELECTION)));
            billSeries.setResetType(c.getString(c.getColumnIndex(KEY_BS_RESET_TYPE)));
            billSeries.setRoundOff(c.getInt(c.getColumnIndex(KEY_BS_ROUND_OFF)));
            billSeries.setPrefix(c.getString(c.getColumnIndex(KEY_BS_PREFIX)));
            billSeries.setDefault_bill(c.getInt(c.getColumnIndex(KEY_BS_DEFAULT)));

            c.close();
            return billSeries;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return new BillSeries();
    }

    public boolean updateBillNo(int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BS_CURRENT_BILL, num);
        long rowid = db.update(BILLSERIES_TABLE_NAME, values, KEY_BS_DEFAULT + "= ?", new String[]{"1"});
        return rowid != -1;
        // updating row
    }

    public boolean updateBillSeries(int num, String prefix, String cust, String cash) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BS_CURRENT_BILL, num);
        values.put(KEY_BS_PREFIX, prefix);
        values.put(KEY_BS_CASHIER_SELECTION, cash);
        values.put(KEY_BS_CUSOMER_SELECTION, cust);
        long rowid = db.update(BILLSERIES_TABLE_NAME, values, KEY_BS_DEFAULT + "= ?", new String[]{"1"});

        return rowid != -1;
        // updating row
    }


    public boolean updateCashierRequired(String cashierReq, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(KEY_BS_CASHIER_SELECTION, cashierReq);

        long rowid = db.update(BILLSERIES_TABLE_NAME, values, KEY_BS_DEFAULT + "= ?", new String[]{"1"});

        return rowid != -1;
        // updating row

    }

    public ArrayList<BillSeries> getAllBillSeries() {
        billListSeries.clear();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + BILLSERIES_TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    BillSeries billSeries = new BillSeries();
                    billSeries.setId(c.getInt(c.getColumnIndex(KEY_BS_ID)));
                    billSeries.setBillName(c.getString(c.getColumnIndex(KEY_BS_NAME)));
                    billSeries.setShortName(c.getString(c.getColumnIndex(KEY_BS_SHORT_NAME)));
                    billSeries.setSeed(c.getInt(c.getColumnIndex(KEY_BS_SEED)));
                    billSeries.setCurrentBillNo(c.getInt(c.getColumnIndex(KEY_BS_CURRENT_BILL)));
                    billSeries.setCustomerSelection(c.getString(c.getColumnIndex(KEY_BS_CUSOMER_SELECTION)));
                    billSeries.setCashierSelection(c.getString(c.getColumnIndex(KEY_BS_CASHIER_SELECTION)));
                    billSeries.setResetType(c.getString(c.getColumnIndex(KEY_BS_RESET_TYPE)));
                    billSeries.setRoundOff(c.getInt(c.getColumnIndex(KEY_BS_ROUND_OFF)));
                    billSeries.setPrefix(c.getString(c.getColumnIndex(KEY_BS_PREFIX)));
                    billSeries.setDefault_bill(c.getInt(c.getColumnIndex(KEY_BS_DEFAULT)));
                    billListSeries.add(billSeries);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return billListSeries;
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


    public String getSysValue(String sysKey) {

        try {
            String selectQuery = "SELECT  " + KEY_SYS_VALUE + " FROM " + SYSSPEC_TABLE_NAME
                    + " WHERE " + KEY_SYS_KEY + " = '" + sysKey + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                return c.getString(c.getColumnIndex(KEY_SYS_VALUE));
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return "";
        }
        return "";
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

    public String getPassword(String userName) {

        try {
            String selectQuery = "SELECT  " + KEY_UM_PASSWORD + " FROM " + USER_MST_TABLE_NAME
                    + " WHERE " + KEY_UM_USER + " = '" + userName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                return c.getString(c.getColumnIndex(KEY_UM_PASSWORD));
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return "";
        }
        return "";
    }


    public boolean isAdmin(String userName) {

        try {
            String selectQuery = "SELECT  " + KEY_UM_IS_ADMIN + " FROM " + USER_MST_TABLE_NAME
                    + " WHERE " + KEY_UM_USER + " = '" + userName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                return c.getInt(c.getColumnIndex(KEY_UM_IS_ADMIN)) == 1;
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return false;
        }
        return false;
    }

    public boolean addUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_UM_USER, user.getUserName());
            cv.put(KEY_UM_PASSWORD, user.getPassword());
            cv.put(KEY_UM_IS_ADMIN, user.getIsAdmin());
            db.insert(USER_MST_TABLE_NAME, null, cv);
            db.close();
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return false;
        }
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_UM_USER + " FROM " + USER_MST_TABLE_NAME
                + " WHERE " + KEY_UM_USER + " NOT IN  ('maitri' , 'admin')";
        Log.d("DU", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(cursor.getColumnIndex(KEY_UM_USER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public boolean updateUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UM_IS_ADMIN, user.getIsAdmin());
            values.put(KEY_UM_USER, user.getUserName());
            db.update(USER_MST_TABLE_NAME, values, KEY_UM_ID + " = ?",
                    new String[]{String.valueOf(user.getUserId())});
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return false;
    }

    public User getUser(String userName) {
        User user = new User();
        try {
            String selectQuery = "SELECT  * FROM " + USER_MST_TABLE_NAME +
                    " WHERE " + KEY_UM_USER + " = '" + userName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                user.setUserId(c.getInt(c.getColumnIndex(KEY_UM_ID)));
                user.setUserName(c.getString(c.getColumnIndex(KEY_UM_USER)));
                user.setIsAdmin(c.getInt(c.getColumnIndex(KEY_UM_IS_ADMIN)));
            }
            c.close();
        } catch (SQLException e) {
            createErrorDialog(e.toString());
        }
        return user;
    }

    public boolean deleteUser(String userName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(USER_MST_TABLE_NAME, KEY_UM_USER + " = ?",
                    new String[]{String.valueOf(userName)});
            db.close();
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return false;
        }
    }

    public boolean changePassword(String userName, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UM_PASSWORD, password);
            db.update(USER_MST_TABLE_NAME, values, KEY_UM_USER + " = ?",
                    new String[]{userName});
            return true;
        } catch (SQLException e) {
            createErrorDialog(e.toString());
            return false;
        }
    }

    public List<TaxMst> getAllTaxRates() {
//        categoryList.clear();
        mTaxList.clear();

        String selectQuery = "SELECT  * FROM " + TAX_MST_TABLE_NAME + " WHERE " + KEY_TAXM_TYPE + " = 'IGST' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaxMst tm = new TaxMst();
                tm.setTaxCode(cursor.getInt(cursor.getColumnIndex(KEY_TAXM_CODE)));
                tm.setTaxType(cursor.getString(cursor.getColumnIndex(KEY_TAXM_TYPE)));
                tm.setTaxRate(cursor.getFloat(cursor.getColumnIndex(KEY_TAXM_RATE)));
                tm.setTaxIgstCode(cursor.getInt(cursor.getColumnIndex(KEY_TAXM_IGSTCODE)));
                tm.setTaxIsActive(cursor.getInt(cursor.getColumnIndex(KEY_TAXM_IS_ACTIVE)));

                // Adding category to list
                mTaxList.add(tm);
            } while (cursor.moveToNext());
        }
        cursor.close();
      /*  Collections.sort(categoryList, new Category.OrderByCatName());
        categoryList1.addAll(categoryList);*/
        // return category list
        return mTaxList;
    }

    public List<HsnMst> getAllHsn() {
        mHsnList.clear();
        String selectQuery = "SELECT  * FROM " + HSN_MST_TABLE_NAME;
        ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HsnMst hm = new HsnMst();
                hm.setHsnId(cursor.getInt(cursor.getColumnIndex(KEY_HSN_ID)));
                hm.setHsnCode(cursor.getInt(cursor.getColumnIndex(KEY_HSN_CODE)));
                hm.setHsnDesc(cursor.getString(cursor.getColumnIndex(KEY_HSN_DESC)));
                hm.setHsnIsActive(cursor.getInt(cursor.getColumnIndex(KEY_HSN_IS_ACTIVE)));

                // Adding category to list
                mHsnList.add(hm);
            } while (cursor.moveToNext());
        }
        cursor.close();
      /*  Collections.sort(categoryList, new Category.OrderByCatName());
        categoryList1.addAll(categoryList);*/
        // return category list
        return mHsnList;
    }


    public void resetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CATEGORY_TABLE_NAME);
        db.execSQL("DELETE FROM " + ITEM_TABLE_NAME);
        db.execSQL("DELETE FROM " + SALES_MST_TABLE_NAME);
        db.execSQL("DELETE FROM " + SALES_DET_TABLE_NAME);
        db.execSQL("DELETE FROM " + CUSTOMER_TABLE_NAME);
        db.execSQL("DELETE FROM " + HSN_MST_TABLE_NAME);
        db.close();
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
