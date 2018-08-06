import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.io.*;
import java.util.ArrayList;

public class BufferThread extends com.confusionists.mjdjApi.morph.AbstractMorph {

    private boolean bufferModeOn = false;
    private ArrayList<Integer> bufferedCC = new ArrayList<>();
    private boolean[] ledsState = new boolean[98];

    private ArrayList<MessageWrapper> bufferedMessages = new ArrayList<>();

    @Override
    public String getName() {
        return "Buffer Thread";
    }

    @Override
    public void init() throws DeviceNotFoundException {
        for (int i = 0; i < 98; i++) {
            ledsState[i] = false;
        }
    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        ShortMessageWrapper shortMessageWrapper = messageWrapper.getAsShortMessageWrapper();
        if (shortMessageWrapper.isNoteOn()) {
            if (shortMessageWrapper.getData1() == 82) {
                if (!bufferModeOn) {
                    resetLeds(true);
                    bufferLeds();
                    bufferModeOn = true;
                    return false;
                } else {
                    resetLeds(false);
                    bufferModeOn = false;
                }
            } else if (shortMessageWrapper.getData1() == 83 && bufferedCC.size() != 0) {
                bufferModeOn = false;
                flushBuffer();
                for (int i = 0; i < 98; i++) {
                    ledsState[i] = false;
                }
                return true;
            } else if (bufferModeOn) {
                ledsState[shortMessageWrapper.getData1()] = !ledsState[shortMessageWrapper.getData1()];
                if(ledsState[shortMessageWrapper.getData1()]) {
                    bufferedCC.add(shortMessageWrapper.getData1());
                }
                else{
                    if(bufferedCC.size()!=0) {
                        int position = 0;
                        for (int i = 0; i < bufferedCC.size(); i++) {
                            if (bufferedCC.get(i) == shortMessageWrapper.getData1()) {
                                position = i;
                            }
                        }
                        bufferedCC.remove(position);
                    }
                }
                ShortMessageWrapper messageToSent = shortMessageWrapper;
                if(ledsState[shortMessageWrapper.getData1()]){
                    messageToSent.alterData2(Variables.YELLOW_FLASH);
                }
                else messageToSent.alterData2(Variables.OFF);
                getService().send(shortMessageWrapper);
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

    private void resetLeds(boolean buffer) throws IOException, InvalidMidiDataException {
        FileInputStream fileInputStream = new FileInputStream("lights.txt");
        for (int i = 0; i < 98; i++) {
            int s = 0;
            if (!buffer) {
                s = fileInputStream.read();
                getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i, 0)));
            }
            getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i, s)));
        }
        fileInputStream.close();
    }

    private void bufferLeds() throws InvalidMidiDataException {
        for(int i=0;i<98;i++){
            MessageWrapper message;
            int data2 = Variables.OFF;
            if (ledsState[i]) data2 = Variables.YELLOW_FLASH;
            message = MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i, data2));
            getService().send(message);
        }
    }

    private void flushBuffer() throws Throwable {
        resetLeds(false);
        for (Integer o : bufferedCC) {
            MessageWrapper wrapper = null;
            wrapper = MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, o, 127));
            Thread.sleep(10);
            getService().morph(wrapper, "");
        }
        bufferedCC.clear();
    }
}
