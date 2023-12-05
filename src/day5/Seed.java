package day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seed {
    private static final int SEEDS_NUMBER = 20;
    private static final int CATEGORIES_NUMBER = 8;

    private static final Pattern numberPattern = Pattern.compile("\\d+");
    private static final Pattern headerPattern = Pattern.compile("to");
    private static final Pattern pairPattern = Pattern.compile("(\\d+) (\\d+)");

    public static long[][] seeds = new long[SEEDS_NUMBER][CATEGORIES_NUMBER];
    public static ArrayList<ArrayList<MapUpdate>> maps = new ArrayList<>();
    public static int currentMap = 0;

    public static void loadSeedsPart1(String line) {
        Matcher numberMatcher = numberPattern.matcher(line);
        int seedNumber = 0;
        while(numberMatcher.find()) {
            seeds[seedNumber++][0] = Long.parseLong(numberMatcher.group());
        }
    }

   public static class MapUpdate {
        protected long srcStart;
        protected long srcStop;
        protected long destStart;
        protected long offset;

       public MapUpdate(long[] data) {
           this.srcStart = data[1];
           this.srcStop = this.srcStart + data[2];
           this.destStart = data[0];
           this.offset = this.destStart - this.srcStart;
       }
   }

    public static void updateMap(String line) {
        Matcher numberMatcher = numberPattern.matcher(line);
        long[] lineNumbers = new long[3];
        int cnt = 0;
        while(numberMatcher.find()) {
            lineNumbers[cnt++] = Long.parseLong(numberMatcher.group());
        }
        maps.getLast().add(new MapUpdate(lineNumbers));
    }

    public static void updateCategory() {
        for(int i = 0; i<seeds.length; i++) {
            seeds[i][currentMap+1] = seeds[i][currentMap];

            for(MapUpdate update : maps.getLast()) {
                if(seeds[i][currentMap] >= update.srcStart && seeds[i][currentMap] < update.srcStop) {
                    seeds[i][currentMap+1] = seeds[i][currentMap] + update.offset;
                    break;
                }
            }
        }
        currentMap++;




        ListIterator<ResultsRange> prevResults = prevResultsRanges.listIterator();
        while(prevResults.hasNext()) {
            ResultsRange resultRange = prevResults.next();
            for(MapUpdate update : maps.getLast()) {
                long start = Math.max(resultRange.start, update.srcStart);
                long stop = Math.min(resultRange.stop, update.srcStop);
                if(start < stop) {
                    curResultsRanges.add(new ResultsRange(start+update.offset, stop+update.offset));
                    if(start == resultRange.start && stop == resultRange.stop) {
                        break;
                    } else if(start >= resultRange.start && start < resultRange.stop) {
                        prevResults.add(new ResultsRange(resultRange.start, stop));
                    } else if(stop >= resultRange.start && stop < resultRange.stop) {
                        prevResults.add(new ResultsRange(resultRange.start, stop));
                    } else {
                        prevResults.add(new ResultsRange(resultRange.start, start));
                        prevResults.add(new ResultsRange(stop, resultRange.stop));
                    }
                }
            }
        }
        ArrayList<ResultsRange> temp = prevResultsRanges;
        prevResultsRanges = curResultsRanges;
        curResultsRanges = temp;
        curResultsRanges.clear();
    }


    public static class ResultsRange {
        protected long start;
        protected long stop;

        public ResultsRange(long start, long stop) {
            this.start = start;
            this.stop = stop;
        }
    }

    public static ArrayList<ResultsRange> prevResultsRanges = new ArrayList<>();
    public static ArrayList<ResultsRange> curResultsRanges = new ArrayList<>();

    public static void loadSeedsPart2(String line) {
        Matcher pairMatcher = pairPattern.matcher(line);

        while(pairMatcher.find()) {
            long start = Long.parseLong(pairMatcher.group(1));
            long range = Long.parseLong(pairMatcher.group(2));
            prevResultsRanges.add(new ResultsRange(start, start+range));
        }
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
                updateCategory();
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