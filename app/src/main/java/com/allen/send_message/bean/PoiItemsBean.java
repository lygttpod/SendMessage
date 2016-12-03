package com.allen.send_message.bean;

/**
 * Created by allen on 2016/12/2.
 */

public class PoiItemsBean {

    private String title;
    private String snippet;
    private String latLonPoint;
    private boolean isSelect;


    public PoiItemsBean() {
    }
    public PoiItemsBean(String title, String snippet, String latLonPoint) {
        this.title = title;
        this.snippet = snippet;
        this.latLonPoint = latLonPoint;
    }

    public PoiItemsBean(String title, String snippet, String latLonPoint, boolean isSelect) {
        this.title = title;
        this.snippet = snippet;
        this.latLonPoint = latLonPoint;
        this.isSelect = isSelect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getLatLonPoint() {
        return latLonPoint;
    }

    public void setLatLonPoint(String latLonPoint) {
        this.latLonPoint = latLonPoint;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "PoiItemsBean{" +
                "title='" + title + '\'' +
                ", snippet='" + snippet + '\'' +
                ", latLonPoint='" + latLonPoint + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
