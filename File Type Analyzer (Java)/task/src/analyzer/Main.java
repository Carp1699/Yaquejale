package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <file_path> <patterns_file>");
            return;
        }

        String filePath = args[0];
        String patternsFile = args[1];

        try {
            Map<Float, List<String>> patterns = readPatterns(patternsFile);
            System.out.println(patterns);
            findHighestPriorityPattern(filePath, patterns);
        } catch (IOException e) {
            System.out.println("Error reading patterns file: " + e.getMessage());
        }
    }

    private static Map<Float, List<String>> readPatterns(String patternsFile) throws IOException {
        List<Float> floats = new ArrayList<>();
        Map<Float, List<String>> patterns = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(patternsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Float priority = Float.parseFloat(parts[0]);
                    String pattern = parts[1].replaceAll("\"", "");
                    String response = parts[2].replaceAll("\"", "");
                    if (floats.contains(priority))
                        patterns.put((float) (priority + Math.random()), List.of(pattern, response));
                    else {
                        floats.add(priority);
                        patterns.put(priority, List.of(pattern, response));
                    }

                }
            }
        }

        return patterns;
    }

    private static void findHighestPriorityPattern(String filePath, Map<Float, List<String>> patterns) throws IOException {
        int MAX_THREADS = 8;
        File file = new File(filePath);
        String highestPriorityPattern = null;
        int highestPriority = Integer.MIN_VALUE;
        File[] subFiles = file.listFiles();
        int ThreadPoolSize = Math.min(subFiles.length, MAX_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(ThreadPoolSize);
        List<Future<String>> futureList =
                Arrays.stream(subFiles)
                        .map(file1 -> executorService.submit(() -> {
                            StringBuilder strB = new StringBuilder(file1.getName());
                            strB.append(": ");
                            List<Float> priorityMatchKeyList = new ArrayList<>();
                            for (Map.Entry<Float, List<String>> entry : patterns.entrySet()) {
                                Float key = entry.getKey();
                                List<String> value = entry.getValue();
                                try {
                                    if (Files.readAllLines(Paths.get(file1.getAbsolutePath())).stream().anyMatch(s -> s.contains(value.get(0)))) {
                                        priorityMatchKeyList.add(key);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if (priorityMatchKeyList.isEmpty()) return strB.append("Unknown file type").toString();
                            else {
                                return strB.append(patterns.get(Collections.max(priorityMatchKeyList)).get(1)).toString();
                            }
                        })).toList();
        futureList.forEach(stringFuture -> {
            try {
                System.out.println(stringFuture.get());
            } catch (Exception e) {
            }
        });
    }
}