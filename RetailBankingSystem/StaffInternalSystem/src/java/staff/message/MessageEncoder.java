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
import utils.LoggingUtils;
 
/**
 * A Simple {@link org.primefaces.push.Encoder} that decode a {@link MessageDTO} into a simple JSON object.
 */
public final class MessageEncoder implements Encoder<MessageDTO, String> {
 
    @Override
    public String encode(MessageDTO m) {
        LoggingUtils.StaffMessageLog(MessageViewManagedBean.class, "MessageDTO Encoding with message: " + new JSONObject(m).toString());
        return new JSONObject(m).toString();
    }
}
