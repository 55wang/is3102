/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author VIN-S
 */
public class TokenAuthenticationUtils {
    public static void checkAuthentication(String targetPage){
        if(!SessionUtils.getTokenAuthentication()){
            RedirectUtils.redirect("../customer_token/token_authentication.xhtml?target="+targetPage);
        }
    }
}
