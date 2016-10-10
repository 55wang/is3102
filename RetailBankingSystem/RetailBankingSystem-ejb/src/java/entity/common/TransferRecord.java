/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.common;

import javax.persistence.Entity;
import server.utilities.EnumUtils.TransferPurpose;

/**
 *
 * @author leiyang
 */
@Entity
public class TransferRecord extends TransactionRecord {
    
    private TransferPurpose purpose;

    /**
     * @return the purpose
     */
    public TransferPurpose getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(TransferPurpose purpose) {
        this.purpose = purpose;
    }
}
