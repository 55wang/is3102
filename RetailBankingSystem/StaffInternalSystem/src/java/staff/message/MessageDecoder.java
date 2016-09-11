/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import entity.Message;
import org.primefaces.push.Decoder;
import utils.LoggingUtil;

/**
 *
 * @author leiyang
 */
 
/**
 * A Simple {@link org.primefaces.push.Decoder} that decode a String into a {@link MessageDTO} object.
 */
public class MessageDecoder implements Decoder<String,Message> {
 
    @Override
    public Message decode(String s) {
        LoggingUtil.StaffMessageLog(MessageDecoder.class, s);
        String[] userAndMessage = s.split(":");
        System.out.println(userAndMessage);
        if (userAndMessage.length >= 2) {
            return new Message();
        } 
        else {
            return new Message();
        }
    }
}
