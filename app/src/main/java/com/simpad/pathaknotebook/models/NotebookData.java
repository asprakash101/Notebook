package com.simpad.pathaknotebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class NotebookData implements Serializable, Parcelable
{

    @SerializedName("serial number")
    @Expose
    private String serialNumber;
    @SerializedName("Item")
    @Expose
    private String item;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("Des 1")
    @Expose
    private String des1;
    @SerializedName("Des 2")
    @Expose
    private String des2;
    @SerializedName("Des 3")
    @Expose
    private String des3;
    @SerializedName("Des4")
    @Expose
    private String des4;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("Sale Per Pcs")
    @Expose
    private String salePerPcs;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String quantity;
    public final static Parcelable.Creator<NotebookData> CREATOR = new Creator<NotebookData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NotebookData createFromParcel(Parcel in) {
            return new NotebookData(in);
        }

        public NotebookData[] newArray(int size) {
            return (new NotebookData[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8665645823678031268L;

    protected NotebookData(Parcel in) {
        this.serialNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.item = ((String) in.readValue((String.class.getClassLoader())));
        this.size = ((String) in.readValue((String.class.getClassLoader())));
        this.des1 = ((String) in.readValue((String.class.getClassLoader())));
        this.des2 = ((String) in.readValue((String.class.getClassLoader())));
        this.des3 = ((String) in.readValue((String.class.getClassLoader())));
        this.des4 = ((String) in.readValue((String.class.getClassLoader())));
        this.imgUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.salePerPcs = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NotebookData() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDes1() {
        return des1;
    }

    public void setDes1(String des1) {
        this.des1 = des1;
    }

    public String getDes2() {
        return des2;
    }

    public void setDes2(String des2) {
        this.des2 = des2;
    }

    public String getDes3() {
        return des3;
    }

    public void setDes3(String des3) {
        this.des3 = des3;
    }

    public String getDes4() {
        return des4;
    }

    public void setDes4(String des4) {
        this.des4 = des4;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSalePerPcs() {
        return salePerPcs;
    }

    public void setSalePerPcs(String salePerPcs) {
        this.salePerPcs = salePerPcs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("serialNumber", serialNumber).append("item", item).append("size", size).append("des1", des1).append("des2", des2).append("des3", des3).append("des4", des4).append("imgUrl", imgUrl).append("salePerPcs", salePerPcs).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(serialNumber);
        dest.writeValue(item);
        dest.writeValue(size);
        dest.writeValue(des1);
        dest.writeValue(des2);
        dest.writeValue(des3);
        dest.writeValue(des4);
        dest.writeValue(imgUrl);
        dest.writeValue(salePerPcs);
    }

    public int describeContents() {
        return 0;
    }

}