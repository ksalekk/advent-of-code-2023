package day2;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubeConundrum {
    private static final Pattern redPattern = Pattern.compile("(\\d+) (red)");
    private static final Pattern bluePattern = Pattern.compile("(\\d+) (blue)");
    private static final Pattern greenPattern = Pattern.compile("(\\d+) (green)");


    private static boolean isPossiblePart1(String line) {
         return isColorPossible(redPattern.matcher(line), 12)
                 && isColorPossible(greenPattern.matcher(line), 13)
                 && isColorPossible(bluePattern.matcher(line), 14);
    }

    private static boolean isColorPossible(Matcher colorMatcher, int maxValue) {
        while(colorMatcher.find()) {
            int foundNUmber = Integer.parseInt(colorMatcher.group(1));
            if(foundNUmber > maxValue) {
                return false;
            }
        }
        return true;
    }


    private static int getLineSetsPowerPart2(String line) {
        int redMax = getColorMax(redPattern.matcher(line));
        int greenMax = getColorMax(greenPattern.matcher(line));
        int blueMax = getColorMax(bluePattern.matcher(line));

        return redMax * greenMax * blueMax;
    }

    private static int getColorMax(Matcher colorMatcher) {
        int lastMax = 0;
        while(colorMatcher.find()) {
            int foundNumber = Integer.parseInt(colorMatcher.group(1));
            lastMax = Math.max(lastMax, foundNumber);
        }
        return lastMax;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day2/input.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int idSumPart1 = 0;
            int powerSumPart2 = 0;
            int gameId = 1;

            while((line = br.readLine()) != null) {
                if(isPossiblePart1(line)) {
                    idSumPart1 += gameId;
                }
                powerSumPart2 += getLineSetsPowerPart2(line);
                gameId++;
            }

            System.out.println("Part 1: " + idSumPart1);
            System.out.println("Part 2: " + powerSumPart2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }
}
