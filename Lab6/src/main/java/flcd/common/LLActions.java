package flcd.common;

public enum LLActions {
    POP("pop"),
    ERR("err"),
    ACC("acc");

    public final String label;

    private LLActions(String label) {
        this.label = label;
    }
}
