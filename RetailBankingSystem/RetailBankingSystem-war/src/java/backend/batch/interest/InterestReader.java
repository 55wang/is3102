/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.batch.interest;

import java.io.Serializable;
import javax.batch.api.chunk.ItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import backend.batch.interest.ItemNumberCheckPoint;
import java.util.Iterator;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Dependent
@Named("InterestReader")
public class InterestReader implements ItemReader {
    
    private ItemNumberCheckPoint checkpoint;
    @Inject
    JobContext jobCtx;
//    @PersistenceContext
//    private EntityManager em;
    private Iterator iterator;
    private Properties partParams;
    
    public InterestReader() {}

    @Override
    public void open(Serializable ckpt) throws Exception {
        // Get the parameters for this partition
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long execID = jobCtx.getExecutionId();
        partParams = jobOperator.getParameters(execID);
        
        // Get the range of items to work on in this partition
        long firstItem0 = ((Long) partParams.get("firstItem)")).longValue();
        long numItems0 = ((Long) partParams.get("numItems)")).longValue();
        
        if (ckpt == null) {
            checkpoint = new ItemNumberCheckPoint();
            checkpoint.setItemNumber(firstItem0);
            checkpoint.setNumItems(numItems0);
        } else {
            checkpoint = (ItemNumberCheckPoint) ckpt;
        }
        
        // Adjust range from this partition from the checkpoint
        long firstItem = checkpoint.getItemNumber();
        long numItems = numItems0 - (firstItem - firstItem0);
        
        // Obtain an iterator for the account in this partition
//        String query = "SELECT a FROM CurrentAccount a";
//        Query q = em.createQuery(query)
//                .setFirstResult((int) firstItem).setMaxResults((int) numItems);
//        iterator = q.getResultList().iterator();
    }

    @Override
    public void close() throws Exception {
        
    }

    @Override
    public Object readItem() throws Exception {
        if (iterator.hasNext()) {
            checkpoint.nextItem();
            checkpoint.setNumItems(checkpoint.getNumItems() - 1);
            return iterator.next();
        } else {
            return null;
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return checkpoint;
    }
}
