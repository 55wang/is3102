/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.batch.interest;

import java.io.Serializable;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author leiyang
 */
@Dependent
@Named("InterestWriter")
public class InterestWriter extends AbstractItemWriter {

    @Override
    public void open(Serializable checkpoint) throws Exception {}

    @Override
    public void close() throws Exception {}

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object accountObject : items) {
            // persist here
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return new ItemNumberCheckPoint();
    }
    
}
