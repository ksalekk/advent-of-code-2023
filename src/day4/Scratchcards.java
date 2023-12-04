package day4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Scratchcards {
    public static final Pattern numberPattern = Pattern.compile("\\d+");

    public static ArrayList<Integer> lineWinningNumbers = new ArrayList<>();
    public static ArrayList<Integer> lineOwnedNumbers = new ArrayList<>();

    // test => 6 lines  input => 202 lines
    public static final int[] cardsPerLineCard = new int [202];

    private static void loadLineNumbers(String line) {
        String[] splitedStrings = line.split("\\|");
        Matcher numbersMatcher = numberPattern.matcher(splitedStrings[0].replaceFirst("\\d+", ""));
        while(numbersMatcher.find()) {
            lineWinningNumbers.add(Integer.parseInt(numbersMatcher.group()));
        }
        numbersMatcher = numberPattern.matcher(splitedStrings[1]);
        while(numbersMatcher.find()) {
            lineOwnedNumbers.add(Integer.parseInt(numbersMatcher.group()));
        }
    }

    public static int getLinePoints(String line, int currentCard) {
        int power = -1;
        int extraCards = 0;

        if(! (lineWinningNumbers.isEmpty() & lineOwnedNumbers.isEmpty())) {
            lineWinningNumbers.clear();
            lineOwnedNumbers.clear();
        }
        loadLineNumbers(line);

        for (Integer lineOwnedNumber : lineOwnedNumbers) {
            for (Integer lineWinningNumber : lineWinningNumbers) {
                if (Objects.equals(lineOwnedNumber, lineWinningNumber)) {
                    power++;
                    extraCards++;
                }
            }
        }

        int copiesNumber = cardsPerLineCard[currentCard];
        while(extraCards>0) {
            cardsPerLineCard[currentCard + extraCards] += copiesNumber;
            extraCards--;
        }
        return power != -1 ? (int) Math.pow(2, power) : 0;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day4/input.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int pointsSum1 = 0;
            int currentCard = 0;
            Arrays.fill(cardsPerLineCard, 1);

            while((line = br.readLine()) != null) {
                pointsSum1 += getLinePoints(line, currentCard);
                currentCard++;
            }
            int pointsSum2 = IntStream.of(cardsPerLineCard).sum();

            System.out.println("Part 1: " + pointsSum1);
            System.out.println("Part 2: " + pointsSum2);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }
}
