public class Entity {

    private int cc;
    private boolean isOn = false;

    public Entity(int cc){
        this.cc = cc;
    }

    public int getCc() {
        return cc;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public boolean isOn() {
        return isOn;
    }
}
