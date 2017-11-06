package in.net.maitri.xb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class DbHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "XposeBilling";


    // Category table name
    private static final String CATEGORY_TABLE_NAME = "CategoryMst";



    // Category Table Columns names
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_NAME = "category_name";
    private static final String KEY_CAT_IMAGE_PATH = "category_image";
    private static final String KEY_CAT_CREATED_AT = "category_createdAt";



    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



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


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "("
                + KEY_CAT_ID + " INTEGER PRIMARY KEY," + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_IMAGE_PATH + " TEXT" + KEY_CAT_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
        db.execSQL(CREATE_CATEGORY_TABLE );


        String CREATE_ITEM_TABLE = "CREATE TABLE " + ITEM_TABLE_NAME + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_UOM + " TEXT,"
                + KEY_ITEM_CP + " FLOAT,"
                + KEY_ITEM_SP + " FLOAT,"
                + KEY_ITEM_HSN + " TEXT,"
                + KEY_ITEM_GST+ " FLOAT,"
                + KEY_CATE_ID+ " INTEGER,"
                + KEY_IMAGE_PATH + " TEXT" + KEY_ITEM_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }



    public void addCategory(Category category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CAT_NAME,category.getCategoryName());
        cv.put(KEY_CAT_IMAGE_PATH,category.getCategoryImage());
        db.insert(CATEGORY_TABLE_NAME,null,cv);
        db.close();
    }


    // Getting single category
    public Category getcategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CATEGORY_TABLE_NAME, new String[] { KEY_CAT_ID,
                        KEY_CAT_NAME, KEY_CAT_IMAGE_PATH }, KEY_CAT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2));
        // return category
        return category;
    }




    // Getting All categorys
    public List<Category> getAllcategorys() {
        List<Category> categoryList = new ArrayList<Category>();
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

        // return category list
        return categoryList;
    }

    // Updating single category
    public int updatecategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_NAME, category.getCategoryName());
        values.put(KEY_CAT_IMAGE_PATH, category.getCategoryImage());

        // updating row
        return db.update(CATEGORY_TABLE_NAME, values, KEY_CAT_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
    }

    // Deleting single category
    public void deletecategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORY_TABLE_NAME, KEY_CAT_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
        db.close();
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






    public void addItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ITEM_NAME,item.getItemName());
        cv.put(KEY_IMAGE_PATH,item.getItemImage());
        cv.put(KEY_ITEM_UOM,item.getItemUOM());
        cv.put(KEY_ITEM_CP,item.getItemCP());
        cv.put(KEY_ITEM_SP,item.getItemSP());
        cv.put(KEY_ITEM_HSN,item.getItemHSNcode());
        cv.put(KEY_ITEM_GST,item.getItemGST());
        cv.put(KEY_CATE_ID,item.getCategoryId());
        db.insert(ITEM_TABLE_NAME,null,cv);
        db.close();
    }



    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ITEM_TABLE_NAME + " WHERE "
                + KEY_ITEM_ID + " = " + id;

        Log.d("query", selectQuery);

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

        return item;
    }


    // Getting All item
    public List<Item> getAllitems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
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

                // Adding item to list
                itemList.add(item);
            } while (c.moveToNext());
        }

        // return item list
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
        values.put(KEY_ITEM_HSN, item.getItemHSNcode());
        values.put(KEY_CATE_ID, item.getCategoryId());

        // updating row
        return db.update(ITEM_TABLE_NAME, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }



    // Deleting single item
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE_NAME, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
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
}