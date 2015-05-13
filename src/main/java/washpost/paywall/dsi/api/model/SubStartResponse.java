/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package washpost.paywall.dsi.api.model;

import java.util.Date;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jayaramank
 */
public class SubStartResponse {
    
    private boolean isReturnValid;
    private String returnMessage;
    private String account;
    private Float taxAmount;
    private int queueId;
    private Float subscriptionPrice;
    private Date pubStartDate;
    private HttpServletResponse response;

    public boolean isIsReturnValid() {
        return isReturnValid;
    }

    public void setIsReturnValid(boolean isReturnValid) {
        this.isReturnValid = isReturnValid;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Float getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Float taxAmount) {
        this.taxAmount = taxAmount;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public Float getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(Float subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public Date getPubStartDate() {
        return pubStartDate;
    }

    public void setPubStartDate(Date pubStartDate) {
        this.pubStartDate = pubStartDate;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }


    

}
