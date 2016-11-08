/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.AssociationRuleEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface MarketBasketAnalysisSessionBeanLocal {
    
    public Set<String> getListProductName();
    public List<AssociationRuleEntity> getListAssociationRules();
    public AssociationRuleEntity getAssociationRule(Long Id);
    public AssociationRuleEntity updateAssociationRule(AssociationRuleEntity rule);
    public Integer deleteAssociationrules();
    public AssociationRuleEntity createAssociationRule(AssociationRuleEntity rule);
    public Set<String> getListProductNameByCustomerId(String Id);
    public void generateAssociationRules();
    
}
