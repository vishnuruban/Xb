package in.net.maitri.xb.db;

import java.io.Serializable;


public class Item implements Serializable {

    private int id;
    private String itemName;
    private String itemImage;
    private String itemUOM;
    private double itemCP;
    private double itemSP;
    private double itemNetSP;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String barcode;

    public double getItemNetSP() {
        return itemNetSP;
    }

    public void setItemNetSP(double itemNetSP) {
        this.itemNetSP = itemNetSP;
    }

    private String itemHSNcode;
    private double itemGST;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int categoryId;

    public Item(String itemName, String itemImage, double itemSP) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemSP = itemSP;
    }

    public Item() {


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

    public double getItemCP() {
        return itemCP;
    }

    public void setItemCP(double itemCP) {
        this.itemCP = itemCP;
    }

    public double getItemSP() {
        return itemSP;
    }

    public void setItemSP(double itemSP) {
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

    public double getItemGST() {
        return itemGST;
    }

    public void setItemGST(double itemGST) {
        this.itemGST = itemGST;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Item(String itemName, String itemUOM, double itemCP, double itemSP, String itemHSNcode,
                double itemGST, int categoryId, String itemImage, String barcode) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemUOM = itemUOM;
        this.itemCP = itemCP;
        this.itemSP = itemSP;
      //  this.itemNetSP = itemNetSP;
        this.itemHSNcode = itemHSNcode;
        this.itemGST = itemGST;
        this.categoryId = categoryId;
        this.barcode = barcode;
    }
}
