package hhs4a.project2.c42.enums;

public enum SortOption {
    DEFAULT ("Standaard"),
    SUBJECT_ASC("Gespreksonderwerp (a-z)"),
    SUBJECT_DESC("Gespreksonderwerp (z-a)"),
    LAST_ACTIVE_DESC("Laatst actief (aflopend)"),
    LAST_ACTIVE_ASC("Laatst actief (oplopend)");

    private final String label;

    SortOption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}