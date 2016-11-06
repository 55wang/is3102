/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.primefaces.context.RequestContext;

/**
 *
 * @author leiyang
 */
public class JSUtils {
    public static void callJSMethod(String s) {
        RequestContext.getCurrentInstance().execute(s);
    }
}
