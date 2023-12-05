package day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seed {
    private static final int SEEDS_NUMBER = 20;
    private static final int CATEGORIES_NUMBER = 8;

    private static final Pattern numberPattern = Pattern.compile("\\d+");
    private static final Pattern headerPattern = Pattern.compile("to");
    private static final Pattern pairPattern = Pattern.compile("(\\d+) (\\d+)");

    public static long[][] seeds = new long[SEEDS_NUMBER][CATEGORIES_NUMBER];
    public static ArrayList<ArrayList<MapRange>> maps = new ArrayList<>();
    public static int currentMapPart1 = 0;
    public static int currentMapPart2 = 0;

   public static class MapRange {
        protected long srcStart;
        protected long srcStop;
        protected long offset;

       public MapRange(long[] data) {
           this.srcStart = data[1];
           this.srcStop = this.srcStart + data[2];
           this.offset = data[0] - this.srcStart;
       }
   }

    public static class NumbersRange {
        protected long start;
        protected long stop;

        public NumbersRange(long start, long stop) {
            this.start = start;
            this.stop = stop;
        }
    }

    public static void loadSeedsPart1(String line) {
        Matcher numberMatcher = numberPattern.matcher(line);
        int seedNumber = 0;
        while(numberMatcher.find()) {
            seeds[seedNumber++][0] = Long.parseLong(numberMatcher.group());
        }
    }

    public static void updateMap(String line) {
        Matcher numberMatcher = numberPattern.matcher(line);
        long[] lineNumbers = new long[3];
        int cnt = 0;
        while(numberMatcher.find()) {
            lineNumbers[cnt++] = Long.parseLong(numberMatcher.group());
        }
        maps.getLast().add(new MapRange(lineNumbers));
    }

    public static void updateCategoryPart1() {
        for(int i = 0; i<seeds.length; i++) {
            seeds[i][currentMapPart1 +1] = seeds[i][currentMapPart1];
            for(MapRange update : maps.getLast()) {
                if(seeds[i][currentMapPart1] >= update.srcStart && seeds[i][currentMapPart1] < update.srcStop) {
                    seeds[i][currentMapPart1 +1] = seeds[i][currentMapPart1] + update.offset;
                    break;
                }
            }
        }
        currentMapPart1++;
    }



    public static ArrayList<ArrayList<NumbersRange>> seedsRanges = new ArrayList<>();

    public static void loadSeedsPart2(String line) {
        Matcher pairMatcher = pairPattern.matcher(line);
        while(pairMatcher.find()) {
            long start = Long.parseLong(pairMatcher.group(1));
            long range = Long.parseLong(pairMatcher.group(2));
            seedsRanges.add(new ArrayList<>());
            seedsRanges.getLast().add(new NumbersRange(start, start+range));
        }
    }

    public static void updateCategoryPart2() {
        for(ArrayList<NumbersRange> singleRange : seedsRanges) {
            // seedsRanges has size 8 (one for each category)
            NumbersRange copy = new NumbersRange(singleRange.get(currentMapPart2).start, singleRange.get(currentMapPart2).stop);
            singleRange.add(copy);
            for(MapRange update : maps.getLast()) {
                if(singleRange.get(currentMapPart2).start >= update.srcStart && singleRange.get(currentMapPart2).stop <= update.srcStop) {
                    singleRange.get(currentMapPart2 + 1).start = singleRange.get(currentMapPart2).start + update.offset;
                    singleRange.get(currentMapPart2 + 1).stop = singleRange.get(currentMapPart2).stop + update.offset;
                    break;
                }
            }
        }
        currentMapPart2++;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day5/test.txt");


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = br.readLine();
            if(line != null) {
                loadSeedsPart1(line);
                loadSeedsPart2(line);
                br.readLine();
            }

            Matcher headerMatcher;
            while((line = br.readLine()) != null) {
                maps.add(new ArrayList<>());
                headerMatcher = headerPattern.matcher(line);
                boolean flag = headerMatcher.find();
                while(flag) {
                    line = br.readLine();
                    if(line == null || line.isEmpty()) {
                        break;
                    }
                    updateMap(line);
                    headerMatcher = headerPattern.matcher(line);
                    flag = !(headerMatcher.find());
                }
                updateCategoryPart1();
                updateCategoryPart2();
            }

            long min = Long.MAX_VALUE;
            for (long[] seed : seeds) {
                if (seed[7] < min) {
                    min = seed[7];
                }
            }
            System.out.println("Part 1: " + min);
            System.out.println("Part 2: " );

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }

}