public class Macro extends Entity {

    private boolean hold = false;
    private CharSequence virtualKey;

    public Macro(int cc, CharSequence virtualKey, boolean hold) {
        super(cc);
        this.virtualKey = virtualKey;
        this.hold = hold;
    }

    public boolean isHold() {
        return hold;
    }

    public CharSequence getVirtualKey() {
        return virtualKey;
    }
}
