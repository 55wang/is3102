/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.DepositProduct;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface DepositProductSessionBeanLocal {
    public DepositProduct createDepositProduct(DepositProduct dp);
    public DepositProduct updateDepositProduct(DepositProduct dp);
    public DepositProduct getDepositProductByName(String name);
    public List<DepositProduct> getAllPresentProducts();
}
