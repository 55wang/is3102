/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Demo {

    //http://codereview.stackexchange.com/questions/125372/mining-association-rules-in-java

    public static void main(String[] args) {
        demo();
    }

    private static void demo() {
        AprioriFrequentItemsetGenerator<String> generator
                = new AprioriFrequentItemsetGenerator<>();

        List<Set<String>> itemsetList = new ArrayList<>();

//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b")));
//        itemsetList.add(new HashSet<>(Arrays.asList("c")));
//        itemsetList.add(new HashSet<>(Arrays.asList("d", "e")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b", "c")));
//        itemsetList.add(new HashSet<>(Arrays.asList("b")));
        
        List<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        itemsetList.add(new HashSet<>(testList));
        
        testList = new ArrayList<>();
        testList.add("c");
        itemsetList.add(new HashSet<>(testList));
        
        testList = new ArrayList<>();
        testList.add("a");
        itemsetList.add(new HashSet<>(testList));
        
        testList = new ArrayList<>();
        testList.add("d");
        testList.add("e");
        itemsetList.add(new HashSet<>(testList));
        
        testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        testList.add("c");
        itemsetList.add(new HashSet<>(testList));
        
        testList = new ArrayList<>();
        testList.add("b");
        itemsetList.add(new HashSet<>(testList));
        
        
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b")));
//        itemsetList.add(new HashSet<>(Arrays.asList("b", "c", "d")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "c", "d", "e")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "d", "e")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b", "c")));
// 
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b", "c", "d")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b", "c")));
//        itemsetList.add(new HashSet<>(Arrays.asList("a", "b", "d")));
//        itemsetList.add(new HashSet<>(Arrays.asList("b", "c", "e")));

        long startTime = System.nanoTime();
        FrequentItemsetData<String> data = generator.generate(itemsetList, 0.0);
        long endTime = System.nanoTime();

        int i = 1;

        System.out.println("--- Frequent itemsets ---");

        for (Set<String> itemset : data.getFrequentItemsetList()) {
            System.out.printf("%2d: %9s, support: %1.1f\n",
                    i++,
                    itemset,
                    data.getSupport(itemset));
        }

        System.out.printf("Mined frequent itemset in %d milliseconds.\n",
                (endTime - startTime) / 1_000_000);

        startTime = System.nanoTime();
        List<AssociationRule<String>> associationRuleList
                = new AssociationRuleGenerator<String>()
                .mineAssociationRules(data, 0.0);
        endTime = System.nanoTime();

        i = 1;

        System.out.println();
        System.out.println("--- Association rules ---");

        for (AssociationRule<String> rule : associationRuleList) {
            System.out.printf("%2d: %s\n", i++, rule);
        }

        System.out.printf("Mined association rules in %d milliseconds.\n",
                (endTime - startTime) / 1_000_000);
    }
}
