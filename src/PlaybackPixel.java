import java.awt.*;

public class PlaybackPixel {

    private int playbackCC;
    private int pauseCC;
    private int x,y;
    private boolean hasPause = true;
    public PlaybackPixel(int playbackCC, int pauseCC, int x, int y){
        this.playbackCC = playbackCC;
        this.pauseCC = pauseCC;
        this.x = x;
        this.y = y;
    }

    public PlaybackPixel(int playbackCC, int x, int y){
        this.playbackCC = playbackCC;
        this.x = x;
        this.y = y;
        this.hasPause = false;
    }

    public boolean checkPixelColor(int r, int g, int b) throws AWTException {
        Robot robot = new Robot();
        Color color = robot.getPixelColor(x,y);
        if(color.getBlue()==b&&color.getRed()==r&&color.getGreen()==g){
            return true;
        }
        return false;
    }

    public int getPlaybackCC() {
        return playbackCC;
    }

    public int getPauseCC() {
        return pauseCC;
    }

    public boolean isHasPause(){
        return  hasPause;
    }
}
