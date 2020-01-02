package com.zeed.models;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFilter("sensitiveDataFilter")
public class RoutingInformation {

    private int sourceInterchangeId;
    private String receivingInstitutionId;
    private String pan;
    private String bin;
    private String cardAcceptorId;
    private String fromAccountType;
    private String toAccountType;
    private String acquiringInstitutionIdentifier;
    private String terminalId;
    private long minorAmount;
    private int sinkInterchangeId;
    private String transactionType;
    private Type type = Type.ROUTE;
    private String serviceType;
    private String transactionFamily;


    public int getSourceInterchangeId() {
        return sourceInterchangeId;
    }

    public void setSourceInterchangeId(int sourceInterchangeId) {
        this.sourceInterchangeId = sourceInterchangeId;
    }

    public String getReceivingInstitutionId() {
        return receivingInstitutionId;
    }

    public void setReceivingInstitutionId(String receivingInstitutionId) {
        this.receivingInstitutionId = receivingInstitutionId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getCardAcceptorId() {
        return cardAcceptorId;
    }

    public void setCardAcceptorId(String cardAcceptorId) {
        this.cardAcceptorId = cardAcceptorId;
    }

    public String getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getAcquiringInstitutionIdentifier() {
        return acquiringInstitutionIdentifier;
    }

    public void setAcquiringInstitutionIdentifier(String acquiringInstitutionIdentifier) {
        this.acquiringInstitutionIdentifier = acquiringInstitutionIdentifier;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public long getMinorAmount() {
        return minorAmount;
    }

    public void setMinorAmount(long minorAmount) {
        this.minorAmount = minorAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getSinkInterchangeId() {
        return sinkInterchangeId;
    }

    public void setSinkInterchangeId(int sinkInterchangeId) {
        this.sinkInterchangeId = sinkInterchangeId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public enum Type {
        ROUTE,
        SEND
    }

    public String getTransactionFamily() {
        return transactionFamily;
    }

    public void setTransactionFamily(String transactionFamily) {
        this.transactionFamily = transactionFamily;
    }
}
