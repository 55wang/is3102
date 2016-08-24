/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.batch.interest;

import ejb.session.dams.CurrentAccountSessionBeanLocal;
import java.util.Properties;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Dependent
@Named("InterestPartitionMapper")
public class InterestPartitionMapper implements PartitionMapper {

//    @PersistenceContext
//    EntityManager em;
    
    @EJB
    private CurrentAccountSessionBeanLocal accountSessionBean;
    
    @Override
    public PartitionPlan mapPartitions() throws Exception {
        // Create new partition plan
        return new PartitionPlanImpl() {
            
            // Auxiliary method -- get the number of accounts
            public long getAccountCount() {
                return accountSessionBean.showNumberOfAccounts();
            }
            
            // The number of partitions could be dynamically calculated based 
            // on many parameters. In this particular example, we are setting 
            // it to a fixed value for simlicity
            @Override
            public int getPartitions() {
                return 2;
            }
            
            // Obtain the parameters for each partition. In this case, the 
            // parameters represent the range of items each partition of the 
            // step should work on.
            @Override
            public Properties[] getPartitionProperties() {
                // Assign an (approximately) equal number of 
                // elements to each partition
                long totalItems = getAccountCount();
                long partItems = (long) totalItems / getPartitions();
                long remItems = totalItems % getPartitions();
                // Populate a Properties Array. Each Properties element
                // in the array corresponds to each partition
                Properties[] props = new Properties[getPartitions()];
                
                for (int i = 0; i < getPartitions(); i++) {
                    props[i] = new Properties();
                    props[i].put("firstItem", i*partItems);
                    // Last partition gets the remainder elements
                    if (i == getPartitions() - 1) {
                        props[i].put("numItems", partItems + remItems);
                    } else {
                        props[i].put("numItems", partItems);
                    }
                }
                
                return props;
            }
        };
    }
    
}
