package in.net.maitri.xb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class CategoryMst extends SQLiteOpenHelper {



    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_NAME = "CustomerMst";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CAT_NAME = "category_name";
    private static final String KEY_IMAGE_PATH = "category_image";
    private static final String KEY_CREATED_AT = "category_createdAt";
    public CategoryMst(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAT_NAME + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT" + KEY_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }



    public void addCategory(Category category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CAT_NAME,category.getCategoryName());
        cv.put(KEY_IMAGE_PATH,category.getCategoryImage());
        db.insert(TABLE_NAME,null,cv);
        db.close();
    }


    // Getting single category
    public Category getcategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_CAT_NAME, KEY_IMAGE_PATH }, KEY_ID + "=?",
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
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

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
        values.put(KEY_IMAGE_PATH, category.getCategoryImage());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
    }

    // Deleting single category
    public void deletecategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
        db.close();
    }


    // Getting categorys Count
    public int getcategorysCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



}
