/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        for (Enum<E> enumVal : enumData.getEnumConstants()) {
            results.add(enumVal.toString());
        }
        return results;
    }

    public static String getPrependFolderName() {
        String systemUser = System.getProperty("user.name");
        String prependingPath = "";
        if (systemUser.equals("wang")) {
            prependingPath = "/Users/wang/NEW_IS3102/is3102/RCode/";
        } else if (systemUser.equals("litong")) {
            //#prependingPath = "/Users/litong/Documents/is3102/RCode/";
        } else if (systemUser.equals("leiyang")) {
            prependingPath = "/Users/leiyang/Desktop/IS3102/workspace/is3102/RCode/";
        } else if (systemUser.equals("VIN-S")) {
            System.out.println("VIN-S");
            prependingPath = "/Users/VIN-S/Documents/is3102/RCode/";

        } else if (systemUser.equals("qiuxiaqing")) {
            System.out.println("qiuxiaqing");
            prependingPath = "/Users/qiuxiaqing/Documents/is3102/RCode/";
        } else if (systemUser.equals("yifan")) {

        }
        return prependingPath;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
