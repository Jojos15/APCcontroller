import javax.naming.Context;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class GlobalTap implements Runnable {

    MainThread mainThread;

    public GlobalTap(MainThread mainThread) {
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        ArrayList<Integer> tempList2 = new ArrayList<Integer>();
        ArrayList<Integer> tempList3 = new ArrayList<Integer>();
        int white = new Color(255, 255, 255).getRGB();
        try {
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(1366, 768));
            for (int j = 407; j <= 414; j++) {
                for (int i = 934; i <= 937; i++) {
                    int color = image.getRGB(i, j);
                    if (color == white) {
                        tempList.add(i - 934);
                        tempList.add(j - 407);
                    }
                }
                for (int i = 940; i <= 943; i++) {
                    int color = image.getRGB(i, j);
                    if (color == white) {
                        tempList2.add(i - 940);
                        tempList2.add(j - 407);
                    }
                }
                for (int i = 946; i <= 949; i++) {
                    int color = image.getRGB(i, j);
                    if (color == white) {
                        tempList3.add(i - 946);
                        tempList3.add(j - 407);
                    }
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Integer[] toCheck = new Integer[tempList.size()];
        toCheck = tempList.toArray(toCheck);
        Integer[] toCheck2 = new Integer[tempList2.size()];
        toCheck2 = tempList2.toArray(toCheck2);
        Integer[] toCheck3 = new Integer[tempList3.size()];
        toCheck3 = tempList3.toArray(toCheck3);
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            if (Arrays.equals(toCheck, Variables.digitPixelsWhite[i])) {
                sum += i * 100;
            }
            if (Arrays.equals(toCheck2, Variables.digitPixelsWhite[i])) {
                sum += i * 10;
            }
            if (Arrays.equals(toCheck3, Variables.digitPixelsWhite[i])) {
                sum += i;
            }
        }
        mainThread.getService().log(Integer.toString(sum));
    }
}
