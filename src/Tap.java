public class Tap extends Entity {

    private float tapTimeMs = 0;
    private CharSequence virtualKey;

    public Tap(int cc, CharSequence virtualKey) {
        super(cc);
        this.virtualKey = virtualKey;
    }

    public float getTapTimeMs() {
        return tapTimeMs;
    }

    public CharSequence getVirtualKey() {
        return virtualKey;
    }
}
