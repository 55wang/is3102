/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
public class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate = new Date();
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateDate = new Date();
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "conversation")
    private List<Message> messages = new ArrayList<Message>();
    @ManyToOne(cascade={CascadeType.PERSIST})
    private StaffAccount sender;
    @ManyToOne(cascade={CascadeType.PERSIST})
    private StaffAccount receiver;
    private Boolean readBySender = false;
    private Boolean readByReceiver = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * @return the sender
     */
    public StaffAccount getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(StaffAccount sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public StaffAccount getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(StaffAccount receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the readBySender
     */
    public Boolean getReadBySender() {
        return readBySender;
    }

    /**
     * @param readBySender the readBySender to set
     */
    public void setReadBySender(Boolean readBySender) {
        this.readBySender = readBySender;
    }

    /**
     * @return the readByReceiver
     */
    public Boolean getReadByReceiver() {
        return readByReceiver;
    }

    /**
     * @param readByReceiver the readByReceiver to set
     */
    public void setReadByReceiver(Boolean readByReceiver) {
        this.readByReceiver = readByReceiver;
    }
    
}
