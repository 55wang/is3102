/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class CustomizedDepositAccount extends DepositAccount {
    // Bank is able to create new deposit account product
    // Interest is applied accross accounts, only certain account has interest memory
}
