package in.net.maitri.xb.db;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class Category  {


    public Category() {
    }

    public Category(int id, String categoryName, String categoryImage) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public Category(String categoryName, String categoryImage) {

        this.categoryName = categoryName;
        this.categoryImage = categoryImage;

    }

    public Category(String categoryName, String categoryImage, String categoryTime) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryTime = categoryTime;
    }

    private int id;
    private String categoryName;
    private String categoryImage;
    private String categoryTime;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryTime() {
        return categoryTime;
    }

    public void setCategoryTime(String categoryTime) {
        this.categoryTime = categoryTime;
    }


}
