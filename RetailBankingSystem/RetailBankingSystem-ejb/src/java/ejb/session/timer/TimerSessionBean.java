/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

/**
 *
 * @author litong
 */
@Stateless
public class TimerSessionBean implements TimerSessionBeanLocal {
    
    @Resource 
    private SessionContext ctx;

    @Schedule(dayOfWeek = "*", month = "*", hour = "0-8", dayOfMonth = "*", year = "*", minute = "*", second = "*")
    public void EODTimer() {
    
    }
    
    @Timeout
    public void handleTimeout(Timer timer) 
    {
        if(timer.getInfo().toString().equals("Some name"))
        {
            System.err.println("EjbTimerSession.handleTimeout(): some name");
        }
    }
}
