/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author wang
 */
public class RedirectUtils {

    public static void redirect(String link) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(link);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String generateParameters(Map<String, String> map) {
        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry: map.entrySet()) {
            String oneEntry = entry.getKey() + "=" + entry.getValue();
            params.add(oneEntry);
        }
        String result = "?";
        int counter = 0;
        for (String param : params) {
            if (counter == 0) {
                result += param;
            } else {
                result += "&" + param;
            }
            counter++;
        }
        
        return result;
    }
}
