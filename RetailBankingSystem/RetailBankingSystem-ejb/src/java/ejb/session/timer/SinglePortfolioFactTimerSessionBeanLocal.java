/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface SinglePortfolioFactTimerSessionBeanLocal {

    public void initSPFTimer();
}
