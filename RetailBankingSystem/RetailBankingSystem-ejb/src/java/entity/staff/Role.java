/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.staff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author wang
 */
@Entity
public class Role implements Serializable {
    
    @Id
    private String roleName;
    // info
    @Column(length = 4000)
    private String description;
    
    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(name="Staffaccount_Role_Bdi")
    private List<StaffAccount> staffAccounts = new ArrayList<>();
    
    public Role() {
        
    }
    
    public Role(String name) {
        this.roleName = name;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    @Override
    public String toString() {
        return this.roleName;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        if (!Objects.equals(this.roleName, other.roleName)) {
            return false;
        }
        return true;
    }  

    /**
     * @return the staffAccounts
     */
    public List<StaffAccount> getStaffAccounts() {
        return staffAccounts;
    }

    /**
     * @param staffAccounts the staffAccounts to set
     */
    public void setStaffAccounts(List<StaffAccount> staffAccounts) {
        this.staffAccounts = staffAccounts;
    }
}
