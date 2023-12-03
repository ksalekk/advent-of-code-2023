package day3;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gear {
    private static final Line prevLine = new Line();
    private static final Line centerLine = new Line();
    private static Line nextLine = new Line();

    private static final Pattern numberPattern = Pattern.compile("\\d+");
    private static final Pattern symbolPatternPart1 = Pattern.compile("[^a-zA-Z\\d.]");
    private static class Number {
        private final int value;
        private final int startPosition;
        private final int endPosition;
        private boolean wasCounted;

        public Number(int value, int startPosition, int endPosition) {
            this.value = value;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.wasCounted = false;
        }

        @Override
        public String toString() {
            return "Number{" +
                    "value=" + value +
                    ", startPosition=" + startPosition +
                    ", endPosition=" + endPosition +
                    '}';
        }
    }

    private static class Symbol {
        private final char value;
        private final int position;
        private int adjacentCnt;

        public Symbol(char value, int position) {
            this.value = value;
            this.position = position;
            this.adjacentCnt = 0;
        }

        @Override
        public String toString() {
            return "Symbol{" +
                    "value=" + value +
                    ", position=" + position +
                    '}';
        }
    }

    private static class Line {
        private ArrayList<Symbol> symbolsInLine = new ArrayList<>();
        private ArrayList<Number> numbersInLine = new ArrayList<>();

        private void replaceLine(Line newContent) {
            numbersInLine = (ArrayList<Number>) newContent.numbersInLine.clone();
            symbolsInLine = (ArrayList<Symbol>) newContent.symbolsInLine.clone();

        }

        private void readLine(String line) {
            numbersInLine.clear();
            symbolsInLine.clear();

            Matcher numberMatcher = numberPattern.matcher(line);
            Matcher symbolMatcher = symbolPatternPart1.matcher(line);

            while(numberMatcher.find()) {
                int start = numberMatcher.start();
                int end = numberMatcher.end();
                int value = Integer.parseInt(numberMatcher.group());
                numbersInLine.add(new Number(value, start, end));
            }

            while(symbolMatcher.find()) {
                int position = symbolMatcher.start();
                assert symbolMatcher.start() == symbolMatcher.end();
                char value = symbolMatcher.group().charAt(0);
                symbolsInLine.add(new Symbol(value, position));
            }
        }
    }

    private static int checkAdjacentPart1() {
        int sum = 0;
        if(centerLine.symbolsInLine.isEmpty()) {
            return sum;
        }

        for(Symbol sym : centerLine.symbolsInLine) {
            for(Number num : prevLine.numbersInLine)
                if(!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                    num.wasCounted = true;
                    sum += num.value;
                }

            for(Number num : centerLine.numbersInLine)
                if(!num.wasCounted && (num.startPosition == sym.position + 1 || num.endPosition == sym.position)) {
                    num.wasCounted = true;
                    sum += num.value;
                }

            if(nextLine != null) {
                for(Number num : nextLine.numbersInLine) {
                    if (!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                        num.wasCounted = true;
                        sum += num.value;
                    }
                }
            }
        }
        return sum;
    }

    private static void clearNumbersCounted() {
        for(Number num : prevLine.numbersInLine) {
            num.wasCounted = false;
        }
        for(Number num : centerLine.numbersInLine) {
            num.wasCounted = false;
        }
        if(nextLine != null) {
            for(Number num : nextLine.numbersInLine) {
                num.wasCounted = false;
            }
        }
    }

    private static long checkAdjacentPart2() {
        clearNumbersCounted();
        long sum = 0;


        if(centerLine.symbolsInLine.isEmpty()) {
            return 0;
        }

        for(Symbol sym : centerLine.symbolsInLine) {
            long multiply = 1;
            if(sym.value != '*') {
                continue;
            }

            for(Number num : prevLine.numbersInLine)
                if(!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                    num.wasCounted = true;
                    multiply *= num.value;
                    sym.adjacentCnt++;
                }

            for(Number num : centerLine.numbersInLine)
                if(!num.wasCounted && (num.startPosition == sym.position + 1 || num.endPosition == sym.position)) {
                    num.wasCounted = true;
                    multiply *= num.value;
                    sym.adjacentCnt++;
                }

            if(nextLine != null) {
                for(Number num : nextLine.numbersInLine) {
                    if (!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                        num.wasCounted = true;
                        multiply *= num.value;
                        sym.adjacentCnt++;
                    }
                }
            }
            sum += sym.adjacentCnt == 2 ? multiply : 0;
        }
        return sum;
    }



    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/day3/input.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String fileLine;
            prevLine.readLine(br.readLine());
            centerLine.readLine(br.readLine());
            nextLine.readLine(br.readLine());
            int sumPart1 = checkAdjacentPart1();
            long sumPart2 = checkAdjacentPart2();

            while((fileLine = br.readLine()) != null) {
                prevLine.replaceLine(centerLine);
                centerLine.replaceLine(nextLine);
                nextLine.readLine(fileLine);
                sumPart1 += checkAdjacentPart1();
                sumPart2 += checkAdjacentPart2();
            }

            prevLine.replaceLine(centerLine);
            centerLine.replaceLine(nextLine);
            nextLine = null;
            sumPart1 += checkAdjacentPart1();
            sumPart2 += checkAdjacentPart2();

            System.out.println("Part 1: " + sumPart1);
            System.out.println("Part 2: " + sumPart2);

            double time = System.currentTimeMillis()-start;
            System.out.println("Czas: " + time/1000 + "s");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
