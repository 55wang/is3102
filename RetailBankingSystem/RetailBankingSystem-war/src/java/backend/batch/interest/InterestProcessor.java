/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.batch.interest;

import entity.SavingAccount;
import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author leiyang
 */
@Dependent
@Named("InterestProcessor")
public class InterestProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object obj) throws Exception {
        SavingAccount da = (SavingAccount) obj;
        //TODO: Process interest here, da.calculate()
        return da;
    }
    
}
