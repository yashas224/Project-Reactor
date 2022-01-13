package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImperativeExample {
    public static void main(String[] args) {

        var list = Arrays.asList("xsdt", "apple", "men", "adam", "adam", "likute");
        var newNamesList = namesGreaterThanSize(list, 3);
        System.out.println(newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> list, int size) {
        var namesList = new ArrayList<String>();
        for (var s : list) {
            if (s.length() > size && !namesList.contains(s.toUpperCase(Locale.ROOT))) {
                namesList.add(s.toUpperCase(Locale.ROOT));
            }
        }
        return namesList;
    }


}
