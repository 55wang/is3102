/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;


/**
 *
 * @author litong
 */
//@Singleton
//@LocalBean
//@Startup
//public class TimerServiceDemo {
//
//    // Add business logic below. (Right-click in editor and choose
//    // "Insert Code > Add Business Method")
//    @Resource
//    private TimerService timerService;
//    
//    @PostConstruct
//    private void init(){
//        timerService.createTimer(1000, 2000 , "IntervalTimerDemo_Info");
//    }
//    
//    @Timeout
//    public void excute(Timer timer){
//        System.out.println("Timer Service : " + timer.getInfo());
//        System.out.println("Current Time : " + new Date());
//        System.out.println("Next Timeout : " + timer.getNextTimeout());
//        System.out.println("Time Remaining : " + timer.getTimeRemaining());
//        System.out.println("____________________________________________");
//    }
//}

