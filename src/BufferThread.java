import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;

import java.io.*;
import java.util.ArrayList;

public class BufferThread extends com.confusionists.mjdjApi.morph.AbstractMorph {

    private boolean bufferModeOn = false;
    private ArrayList<Integer> bufferedCC = new ArrayList<>();

    private ArrayList<MessageWrapper> bufferedMessages = new ArrayList<>();

    @Override
    public String getName() {
        return "Buffer Thread";
    }

    @Override
    public void init() throws DeviceNotFoundException {
        try {
            readLightsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        ShortMessageWrapper shortMessageWrapper = messageWrapper.getAsShortMessageWrapper();
        if (shortMessageWrapper.isNoteOn()) {
            if(bufferModeOn){
                bufferedCC.add(shortMessageWrapper.getData1());
            }
            if (shortMessageWrapper.getData1() == 87) {
                if(!bufferModeOn) {
                    bufferModeOn = true;
                    return false;
                }
                else{

                }
            }
        }
        return bufferModeOn;
    }

    @Override
    public String diagnose() {
        return null;
    }

    @Override
    public Object getSerializable() {
        return null;
    }

    @Override
    public void setSerializable(Object o) {

    }

    private void readLightsFromFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("lights.txt");
        for (int i = 0; i < 98; i++) {
            int s = fileInputStream.read();
            getService().log(s + "");
        }
    }
}
