/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyang
 */
public class CommonUtils {
    //Call Like This: CommonUtils.getEnumList(EnumUtils.Gender.class)
    public static <E extends Enum<E>> List<String> getEnumList(Class<E> enumData) {
        List<String> results = new ArrayList<>();
        for (Enum<E> enumVal: enumData.getEnumConstants()) {  
            results.add(enumVal.toString());
        }
        return results;
    }
}
