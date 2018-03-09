package in.net.maitri.xb.billReports;


class FilterModel {

    private String name;
    private int catId, itmId;
    private boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getItmId() {
        return itmId;
    }

    public void setItmId(int itmId) {
        this.itmId = itmId;
    }

}
