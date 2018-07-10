package com.scheidtbachmann.entervocheckoutplugin.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SBCheckOutTransaction {

    @SerializedName("success")
    @Expose
    private boolean success = false;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("facility")
    @Expose
    private String facility;

    @SerializedName("epan")
    @Expose
    private String epan;

    @SerializedName("entrytime")
    @Expose
    private String entrytime;

    @SerializedName("operator")
    @Expose
    private String operator;

    @SerializedName("braintree_transaction_id")
    @Expose
    private String braintree_transaction_id;

    @SerializedName("unique_pay_id")
    @Expose
    private String unique_pay_id;

    @SerializedName("vat_amount")
    @Expose
    private double vat_amount;

    @SerializedName("vat_rate")
    @Expose
    private String vat_rate;

    @SerializedName("transactionTime")
    @Expose
    private String transactionTime;

    @SerializedName("currency")
    @Expose
    private String currency;

    @Override
    public String toString() {

        String me = "{\"SBCheckOutTransaction\":{";
        me += "\"success\":" + (success ? "true": "false") + ",";
        me += "\"amount\":" + String.valueOf(amount) + ",";
        me += "\"currency\":\"" + currency + "\",";
        me += "\"facility\":\"" + facility+ "\",";
        me += "\"duration\":\"" + duration + "\",";
        me += "\"epan\":\"" + epan + "\",";
        me += "\"entrytime\":\"" + entrytime+ "\",";
        me += "\"operator\":\"" + operator+ "\",";
        me += "\"braintree_transaction_id\":\"" + braintree_transaction_id+ "\",";
        me += "\"unique_pay_id\":\"" + unique_pay_id+ "\",";
        me += "\"vat_amount\":" + String.valueOf(vat_amount) + ",";
        me += "\"vat_rate\":\"" + vat_rate + "\",";
        me += "\"transactionTime\":\"" + transactionTime + "\"";
        me += "}}";
        return me;
    }

    public static SBCheckOutTransaction fromClassificationResult( ClassificationResult result) {

        SBCheckOutTransaction txn = new SBCheckOutTransaction();
        if ( result != null) {
            txn.setSuccess( false);
            txn.setAmount( result.getAmount());
            txn.setDuration( result.getDuration());
            txn.setFacility( result.getFacility());
            txn.setEpan( result.getEpan());
            txn.setEntrytime(result.getEntrytime());
            txn.setOperator(result.getOperator());
            txn.setBraintree_transaction_id(null);
            txn.setUnique_pay_id("");
            txn.setVat_amount(0.0);
            txn.setVat_rate("");
            txn.setTransactionTime("");
            txn.setCurrency(result.getCurrency());
        }
        return txn;
    }

    public void setSuccess( boolean success) { this.success = success;}
    public void setAmount( double amount) {this.amount = amount;}
    public void setDuration( String duration) { this.duration = duration;}
    public void setFacility( String facility) {this.facility = facility;}
    public void setEpan( String epan) {this.epan = epan;}
    public void setEntrytime( String entrytime) {this.entrytime = entrytime;}
    public void setOperator( String operator) { this.operator = operator;}
    public void setBraintree_transaction_id( String braintree_transaction_id) {this.braintree_transaction_id = braintree_transaction_id;}
    public void setUnique_pay_id( String unique_pay_id) {this.unique_pay_id = unique_pay_id;}
    public void setVat_amount( double vat_amount) {this.vat_amount = vat_amount;}
    public void setVat_rate( String vat_rate) {this.vat_rate = vat_rate;}
    public void setTransactionTime( String transactionTime) {this.transactionTime = transactionTime;}
    public void setCurrency( String currency) { this.currency = currency;}

    public boolean getSuccess() {return success;}
    public double getAmount() {return amount;}
    public String getDuration() { return duration;}
    public String getFacility() {return facility;}
    public String getEpan() { return epan;}
    public String getEntrytime() {return entrytime;}
    public String getOperator() {return operator;}
    public String getBraintree_transaction_id() {return braintree_transaction_id;}
    public String getUnique_pay_id() { return unique_pay_id;}
    public double getVat_amount() {return vat_amount;}
    public String getVat_rate(){ return vat_rate;}
    public String getTransactionTime() { return transactionTime;}
    public String getCurrency() { return currency;}
}
