package com.scheidtbachmann.entervocheckoutplugin.dto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassificationResult {

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("facility_id")
    @Expose
    private String facility_id;

    @SerializedName("epan")
    @Expose
    private String epan;

    @SerializedName("entrytime")
    @Expose
    private String entrytime;

    @SerializedName("operator")
    @Expose
    private String operator;

    @SerializedName("cell")
    @Expose
    private String cell;

    @SerializedName("base_url")
    @Expose
    private String base_url;

    @SerializedName("nonce")
    @Expose
    private String nonce;

    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("store_in_vault")
    @Expose
    private boolean store_in_vault;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("device")
    @Expose
    private String device;

    public double getAmount() {return amount;}
    public String getDuration() {return duration;}
    public String getFacility() {return facility;}
    public String getFacility_id() {return facility_id;}
    public String getEpan() {return epan;}
    public String getEntrytime() {return entrytime;}
    public String getOperator() {return operator;}
    public String getCell() {return cell;}
    public String getBase_url() {return base_url;}
    public String getNonce() {return nonce;}
    public String getCustomer_id() {return customer_id;}
    public boolean getStore_in_vault() {return store_in_vault;}
    public String getCurrency() {return currency;}
    public String getDevice() { return device;}

    public void setAmount( double amount) { this.amount = amount;}
    public void setDuration( String duration) {this.duration = duration;}
    public void setFacility( String facility) {this.facility = facility;}
    public void setFacility_id( String facility_id) {this.facility_id =facility_id;}
    public void setEpan( String epan) { this.epan = epan;}
    public void setEntrytime( String entrytime) { this.entrytime = entrytime;}
    public void setOperator( String operator) { this.operator = operator;}
    public void setCell( String cell) {this.cell = cell;}
    public void setBase_url( String base_url) { this.base_url = base_url;}
    public void setNonce( String nonce) { this.nonce = nonce;}
    public void setCustomer_id( String customer_id) { this.customer_id = customer_id;}
    public void setStore_in_vault( boolean store_in_vault) { this.store_in_vault = store_in_vault;}
    public void setCurrency( String currency) { this.currency = currency;}
    public void setDevice( String device) { this.device = device;}
}
