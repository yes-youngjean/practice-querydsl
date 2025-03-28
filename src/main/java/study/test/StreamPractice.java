package study.test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamPractice {
    public static void main(String[] args) {
        List<String> names = List.of("Jack", "Ken", "Mike");

//        // --- 1개씩
//        names.stream()
//                .forEach(n -> System.out.println("name = " + n));

//        // --- 필터 + 1개씩
//        names.stream()
//                .filter(n -> n.startsWith("J"))
//                .forEach(n -> System.out.println("n = " + n));

//        names.stream()
//                .map(n -> n.toUpperCase() + "님")
//                .filter(n -> n.startsWith("J"))
//                .forEach(n -> System.out.println("n = " + n));


        List<String> collections = names.stream()
                .filter(n -> n.length() == 4)
                .collect(Collectors.toList());

        System.out.println("collections =  " + collections);


    }
}
