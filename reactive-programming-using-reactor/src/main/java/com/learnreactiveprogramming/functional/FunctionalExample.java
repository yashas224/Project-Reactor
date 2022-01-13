package com.learnreactiveprogramming.functional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {
    public static void main(String[] args) {

        var list = Arrays.asList("xsdt", "apple", "men", "adam", "adam", "likute");
        var newNamesList = namesGreaterThanSize(list, 3);
        System.out.println(newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> list, int size) {
        return list
                .parallelStream()
                .distinct()
                .filter(s -> s.length() > size)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }
}
