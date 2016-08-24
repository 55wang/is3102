/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.batch.interest;

import java.io.Serializable;

/**
 *
 * @author leiyang
 */
public class ItemNumberCheckPoint implements Serializable {
    
    private long itemNumber;
    private long numItems;
    
    public ItemNumberCheckPoint() {
        itemNumber = 0;
    }
    
    public ItemNumberCheckPoint(int numItems) {
        this();
        this.numItems = numItems;
    }
    
    public void nextItem() {
        itemNumber++;
    }

    /**
     * @return the itemNumber
     */
    public long getItemNumber() {
        return itemNumber;
    }

    /**
     * @param itemNumber the itemNumber to set
     */
    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * @return the numItems
     */
    public long getNumItems() {
        return numItems;
    }

    /**
     * @param numItems the numItems to set
     */
    public void setNumItems(long numItems) {
        this.numItems = numItems;
    }
}
