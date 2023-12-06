package day6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoatRace {
    private static final Pattern numberPattern = Pattern.compile("\\d+");

    public static class Race {
        public long time;
        public long recordDistance;

        public Race(int time) {
            this.time = time;
        }

        public Race(long time, long recordDistance) {
            this.time = time;
            this.recordDistance = recordDistance;
        }
    }

    public  static ArrayList<Race> racesPart1 = new ArrayList<>();

    public static int checkWaysNumber(Race race) {
        int waysCnt = 0;
        long time = race.time;
        long recordDistance = race.recordDistance;

        for(int holdTime=1; holdTime<time; holdTime++) {
            long distance = (time-holdTime) * holdTime;
            if(distance > recordDistance) {
                waysCnt++;
            }
        }
        return waysCnt;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day6/input.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line1 = br.readLine();
            String line2 = br.readLine();

            Matcher numberMatcher = numberPattern.matcher(line1);
            while (numberMatcher.find()) {
                racesPart1.add(new Race(Integer.parseInt(numberMatcher.group())));
            }
            numberMatcher.reset(line2);
            int cnt=0;
            while(numberMatcher.find()) {
                racesPart1.get(cnt++).recordDistance = Integer.parseInt(numberMatcher.group());
            }
            int part1 = 1;
            for(Race race : racesPart1) {
                part1 *= checkWaysNumber(race);
            }
            System.out.println("Part 1: " + part1);

            line1 = line1.replaceAll("\\D", "");
            line2 = line2.replaceAll("\\D", "");
            Race part2Race = new Race(Long.parseLong(line1), Long.parseLong(line2));
            int part2 = checkWaysNumber(part2Race);
            System.out.println("Part 2: " + part2);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }
}
