package day3;

import java.io.*;

public class GearRatios {
    private static final Line prevLine = new Line();
    private static final Line centerLine = new Line();
    private static Line nextLine = new Line();

    private static int sumPart1 = 0;
    private static int sumPart2 = 0;

    private static void updateSum() {
        int sum1 = 0;
        int sum2 = 0;

        if(centerLine.symbolsInLine.isEmpty()) {
            return;
        }

        for(Symbol sym : centerLine.symbolsInLine) {
            int multiply = 1;

            for(Number num : prevLine.numbersInLine)
                if(!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                    num.wasCounted = true;
                    sum1 += num.value;
                    if(sym.value == '*') {
                        multiply *= num.value;
                        sym.cntPart2++;
                    }
                }

            for(Number num : centerLine.numbersInLine)
                if(!num.wasCounted && (num.startPosition == sym.position + 1 || num.endPosition == sym.position)) {
                    num.wasCounted = true;
                    sum1 += num.value;
                    if(sym.value == '*') {
                        multiply *= num.value;
                        sym.cntPart2++;
                    }
                }

            if(nextLine != null) {
                for(Number num : nextLine.numbersInLine) {
                    if (!num.wasCounted && (sym.position >= num.startPosition - 1 && sym.position <= num.endPosition)) {
                        num.wasCounted = true;
                        sum1 += num.value;
                        if(sym.value == '*') {
                            multiply *= num.value;
                            sym.cntPart2++;
                        }
                    }
                }
            }
            sum2 += sym.cntPart2 == 2 ? multiply : 0;
        }
        sumPart1 += sum1;
        sumPart2 += sum2;
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
            updateSum();

            while((fileLine = br.readLine()) != null) {
                prevLine.replaceLine(centerLine);
                centerLine.replaceLine(nextLine);
                nextLine.readLine(fileLine);
                updateSum();
            }

            prevLine.replaceLine(centerLine);
            centerLine.replaceLine(nextLine);
            nextLine = null;
            updateSum();

            System.out.println("Part 1: " + sumPart1);
            System.out.println("Part 2: " + sumPart2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double time = System.currentTimeMillis()-start;
        System.out.println("Time: " + time/1000 + "s");
    }
}
