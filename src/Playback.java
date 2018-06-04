public class Playback extends Entity {

    private CharSequence virtualKey;

    public Playback(int cc, CharSequence virtualKey) {
        super(cc);
        this.virtualKey = virtualKey;
    }

    public CharSequence getVirtualKey() {
        return virtualKey;
    }
}
