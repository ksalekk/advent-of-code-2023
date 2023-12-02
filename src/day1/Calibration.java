package day1;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calibration {
    private static final Map<String, String> replacementWords = Map.of(
            "one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9"
    );

    private static final Pattern findGroupsPattern = Pattern.compile("(?=(one|two|three|four|five|six|seven|eight|nine|\\d))");
    private static final Pattern extractGroupsPattern = Pattern.compile("one|two|three|four|five|six|seven|eight|nine|\\d");

    private static int getLineNumberPart2(String line) {
        StringBuilder digitsBuffer = new StringBuilder();
        String lineNumber;

        Matcher findMatcher = findGroupsPattern.matcher(line);
        while (findMatcher.find()) {
            StringBuilder subString = new StringBuilder(line);
            int foundGroupIndex = findMatcher.start();
            subString.delete(0, foundGroupIndex);

            Matcher extractMatcher = extractGroupsPattern.matcher(subString);
            if (extractMatcher.find()) {
                if (extractMatcher.group().length() > 1) {
                    digitsBuffer.append(replacementWords.get(extractMatcher.group()));
                } else {
                    digitsBuffer.append(extractMatcher.group());
                }
            }
        }

        lineNumber = "" + digitsBuffer.charAt(0) + digitsBuffer.charAt(digitsBuffer.length() - 1);
        return Integer.parseInt(lineNumber);
    }

    private static int getLineNumberPart1(String line) {
        String lineDigits = line.replaceAll("\\D", "");
        String lineNumber = "" + lineDigits.charAt(0) + lineDigits.charAt(lineDigits.length() - 1);
        return Integer.parseInt(lineNumber);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day1/input.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int sumPart1 = 0;
            int sumPart2 = 0;

            while((line = br.readLine()) != null) {
                sumPart1 += getLineNumberPart1(line);
                sumPart2 += getLineNumberPart2(line);
            }

            System.out.println("Part 1: " + sumPart1);
            System.out.println("Part 2: " + sumPart2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
