public class Fader extends Entity {

    private int x, preY;

    public Fader(int cc, int x, int preY){
        super(cc);
        this.x = x;
        this.preY = preY;
    }

    public int getPreY() {
        return preY;
    }

    public int getX() {
        return x;
    }

    public void setPreY(int preY) {
        this.preY = preY;
    }
}
