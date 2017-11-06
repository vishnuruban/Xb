package in.net.maitri.xb.db;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class Item {

    private int id;
    private String itemName;
    private String itemImage;
    private String itemUOM;
    private float itemCP;
    private float itemSP;
    private String itemHSNcode;
    private float itemGST;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int categoryId;

     public Item()
     {

     }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUOM() {
        return itemUOM;
    }

    public void setItemUOM(String itemUOM) {
        this.itemUOM = itemUOM;
    }

    public float getItemCP() {
        return itemCP;
    }

    public void setItemCP(float itemCP) {
        this.itemCP = itemCP;
    }

    public float getItemSP() {
        return itemSP;
    }

    public void setItemSP(float itemSP) {
        this.itemSP = itemSP;
    }

    public String getItemHSNcode() {
        return itemHSNcode;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public void setItemHSNcode(String itemHSNcode) {
        this.itemHSNcode = itemHSNcode;
    }

    public float getItemGST() {
        return itemGST;
    }

    public void setItemGST(float itemGST) {
        this.itemGST = itemGST;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Item(String itemName, String itemUOM, float itemCP, float itemSP, String itemHSNcode, float itemGST, int categoryId,String itemImage) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemUOM = itemUOM;
        this.itemCP = itemCP;
        this.itemSP = itemSP;
        this.itemHSNcode = itemHSNcode;
        this.itemGST = itemGST;
        this.categoryId = categoryId;
    }
}
