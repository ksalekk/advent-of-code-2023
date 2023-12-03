package day3;


public class Number {
    public final int value;
    public final int startPosition;
    public final int endPosition;
    public boolean wasCounted;

    public Number(int value, int startPosition, int endPosition) {
        this.value = value;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.wasCounted = false;
    }
}
