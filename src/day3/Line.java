package day3;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line {
    private static final Pattern numberPattern = Pattern.compile("\\d+");
    private static final Pattern symbolPatternPart1 = Pattern.compile("[^a-zA-Z\\d.]");

    public ArrayList<Symbol> symbolsInLine = new ArrayList<>();
    public ArrayList<Number> numbersInLine = new ArrayList<>();

    public void replaceLine(Line newContent) {
        numbersInLine = (ArrayList<Number>) newContent.numbersInLine.clone();
        symbolsInLine = (ArrayList<Symbol>) newContent.symbolsInLine.clone();

    }

    public void readLine(String line) {
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