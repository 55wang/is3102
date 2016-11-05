/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.util.Random;

/**
 *
 * @author leiyang
 */
public class ColorUtils {
    static String[] colors = {
        "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#CDDC39",
        "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A",
        "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548", "#9E9E9E"
    };
    
    static String[] flatUIColors = {
        "F44336", "E91E63", "9C27B0", "673AB7", "3F51B5", "CDDC39",
        "2196F3", "03A9F4", "00BCD4", "009688", "4CAF50", "8BC34A",
        "FFEB3B", "FFC107", "FF9800", "FF5722", "795548", "9E9E9E"
    };
    
    static Random random = new Random();
    
    public static String randomColor() {
        return colors[random.nextInt(colors.length - 1)];
    }
    
    public static String getFlatUIColors(int i){
        return flatUIColors[i];
    }
    
    public static String randomFlatUIColor() {
        return flatUIColors[random.nextInt(colors.length - 1)];
    }
}
