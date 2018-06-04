public class Stop extends Entity {

    private CharSequence virtualKey;

    public Stop(int cc, CharSequence virtualKey) {
        super(cc);
        this.virtualKey = virtualKey;
    }

    public CharSequence getVirtualKey() {
        return virtualKey;
    }
}
