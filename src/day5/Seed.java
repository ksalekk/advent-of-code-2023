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
        for(int i=0; i<seedsRanges.size(); i++) {
            ArrayList<NumbersRange> singleRange = seedsRanges.get(i);
            long inputStart = singleRange.get(currentMapPart2).start;
            long inputStop = singleRange.get(currentMapPart2).stop;

            // copy
            singleRange.add(new NumbersRange(inputStart, inputStop));

            for(MapRange update : maps.getLast()) {

                if(inputStart >= update.srcStart &&  inputStop <= update.srcStop) {
                    translateRanges(singleRange, inputStart, inputStop, update.offset);
                    break;

                } else if(inputStart >= update.srcStart && inputStart < update.srcStop) {
                    createNewBranch(update.srcStop, inputStop, singleRange);
                    translateRanges(singleRange, inputStart, update.srcStop, update.offset);
                    break;

                } else if(inputStop > update.srcStart && inputStop <= update.srcStop) {
                    createNewBranch(inputStart, update.srcStart, singleRange);
                    translateRanges(singleRange, update.srcStart, inputStop, update.offset);
                    break;

                }
            }
        }
        currentMapPart2++;
    }


    private static void createNewBranch(long rangeStart, long rangeStop, ArrayList<NumbersRange> rootSeedsRange) {
        ArrayList<NumbersRange> newBranch = new ArrayList<>();
        seedsRanges.add(newBranch);
        for(int j=0; j<currentMapPart2; j++) {
            newBranch.add(rootSeedsRange.get(j));
        }
        newBranch.add(new NumbersRange(rangeStart, rangeStop));
    }

    private static void translateRanges(ArrayList<NumbersRange> seedsRange, long start, long stop, long offset) {
        seedsRange.get(currentMapPart2 + 1).start = start + offset;
        seedsRange.get(currentMapPart2 + 1).stop = stop + offset;
    }



    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day5/input.txt");


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


            long min2 = Long.MAX_VALUE;
            for(ArrayList<NumbersRange> seedsRange : seedsRanges) {
                if(seedsRange.getLast().start < min2) {
                    min2 = seedsRange.getLast().start;
                }
            }
            System.out.println("Part 2: " + min2);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }

}