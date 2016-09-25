/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

/**
 *
 * @author leiyang
 */
public class CreditCardDTO {
    
    private String creditCardNumber;
    private String amount;
    private String description;
    private String errorMessage;
    private String tCode;
    private String aCode;
    
    public CreditCardDTO() {
        
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tCode
     */
    public String gettCode() {
        return tCode;
    }

    /**
     * @param tCode the tCode to set
     */
    public void settCode(String tCode) {
        this.tCode = tCode;
    }

    /**
     * @return the aCode
     */
    public String getaCode() {
        return aCode;
    }

    /**
     * @param aCode the aCode to set
     */
    public void setaCode(String aCode) {
        this.aCode = aCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
}
