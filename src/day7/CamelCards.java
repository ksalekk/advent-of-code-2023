package day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CamelCards {

    private enum HandType {
        FIVE, FOUR, FULL_HOUSE, THREE, TWO_PAIR, HIGH_CARD
    }

    public static class Hand {
        String cards;
        int bid;
        HandType type;
        HashMap<Character, Integer> charsAndCounts;

        public Hand(String[] inputLine) {
            this.cards = inputLine[0];
            this.bid = Integer.parseInt(inputLine[1]);
            this.charsAndCounts = createMap();
            this.type = checkType();
        }

        private HashMap<Character, Integer> createMap() {
            HashMap<Character, Integer> mapForHand = new HashMap<>();
            for(char character : cards.toCharArray()) {
                int count = charsAndCounts.containsKey(character) ? charsAndCounts.get(character)+1 : 1;
                charsAndCounts.put(character, count);
            }
            return mapForHand;
        }

        private HandType checkType() {
            ArrayList<Integer> counters = new ArrayList<>(charsAndCounts.values());
            if(counters.size() == 1) {
                return HandType.FIVE;
            } else if(counters.size() == 2 && counters.contains(4)) {
                return HandType.FOUR;
            } else if(counters.size() == 2 && counters.contains(3)) {
                return HandType.THREE;
            }


        }


    }






    public static ArrayList<Hand> hands = new ArrayList<>();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day7/test.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                hands.add(new Hand(line.split(" ")));


            }


            System.out.println("Part 1: " );
            System.out.println("Part 2: " );

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }
}

