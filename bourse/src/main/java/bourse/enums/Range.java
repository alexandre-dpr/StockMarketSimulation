package bourse.enums;

public enum Range {
    ONE_DAY("1d"),
    ONE_YEAR("1y");

    public final String label;

    Range(String label) {
        this.label = label;
    }
}
