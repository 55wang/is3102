/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

/**
 *
 * @author leiyang
 */
import entity.Message;
import org.primefaces.json.JSONObject;
import org.primefaces.push.Encoder;
import utils.LoggingUtil;
 
/**
 * A Simple {@link org.primefaces.push.Encoder} that decode a {@link MessageDTO} into a simple JSON object.
 */
public final class MessageEncoder implements Encoder<Message, String> {
 
    @Override
    public String encode(Message message) {
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, new JSONObject(message).toString());
        return new JSONObject(message).toString();
    }
}
