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

    @Schedule(dayOfWeek = "*", month = "*", hour = "9", dayOfMonth = "*", year = "*", minute = "48", second = "1")
    public void handleTimeout(Timer timer) 
    {
        System.out.println("aaaaa");
    }
}