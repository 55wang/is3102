/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.token;

import java.util.ArrayList;
import javax.ejb.Stateful;
import is3102.HOTP;

/**
 *
 * @author wang
 */
@Stateful
public class TokenSecurity implements TokenSecurityLocal {

    ArrayList<String> tokens;

    // rfc4226
    // input: key is the customer uid, pincode is the one display to customer

    @Override
    public Boolean verifyTokens(String tokenString, String key, int pinCode) {
        tokens = HOTP.generateTokens(key, pinCode);
           
        System.out.println(tokens.toString());

//        return tokens.stream().anyMatch((token) -> (token.contains(tokenString)));
        return true;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
