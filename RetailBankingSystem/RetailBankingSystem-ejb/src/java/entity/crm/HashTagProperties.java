/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.crm;

/**
 *
 * @author wang
 */
public class HashTagProperties {

    private Long depositRecency = 0L;
    private Long depositFrequency = 0L;
    private Long depositMonetary = 0L;
    private Long cardRecency = 0L;
    private Long cardFrequency = 0L;
    private Long cardMonetary = 0L;
    private Double actualIncome = 0.0;

    public HashTagProperties() {
    }

    public HashTagProperties(Long depositRecency,
            Long depositFrequency,
            Long depositMonetary,
            Long cardRecency,
            Long cardFrequency,
            Long cardMonetary,
            Double actualIncome) {
        this.depositRecency = depositRecency;
        this.depositFrequency = depositFrequency;
        this.depositMonetary = depositMonetary;
        this.cardRecency = cardRecency;
        this.cardFrequency = cardFrequency;
        this.cardMonetary = cardMonetary;
        this.actualIncome = actualIncome;
    }

    public Long getDepositRecency() {
        return depositRecency;
    }

    public void setDepositRecency(Long depositRecency) {
        this.depositRecency = depositRecency;
    }

    public Long getDepositFrequency() {
        return depositFrequency;
    }

    public void setDepositFrequency(Long depositFrequency) {
        this.depositFrequency = depositFrequency;
    }

    public Long getDepositMonetary() {
        return depositMonetary;
    }

    public void setDepositMonetary(Long depositMonetary) {
        this.depositMonetary = depositMonetary;
    }

    public Long getCardRecency() {
        return cardRecency;
    }

    public void setCardRecency(Long cardRecency) {
        this.cardRecency = cardRecency;
    }

    public Long getCardFrequency() {
        return cardFrequency;
    }

    public void setCardFrequency(Long cardFrequency) {
        this.cardFrequency = cardFrequency;
    }

    public Long getCardMonetary() {
        return cardMonetary;
    }

    public void setCardMonetary(Long cardMonetary) {
        this.cardMonetary = cardMonetary;
    }

    public Double getActualIncome() {
        return actualIncome;
    }

    public void setActualIncome(Double actualIncome) {
        this.actualIncome = actualIncome;
    }

}
