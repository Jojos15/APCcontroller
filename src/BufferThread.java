import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;

import java.util.ArrayList;

public class BufferThread extends com.confusionists.mjdjApi.morph.AbstractMorph {

    private ArrayList<MessageWrapper> bufferedMessages = new ArrayList<>();

    @Override
    public String getName() {
        return "Buffer Thread";
    }

    @Override
    public void init() throws DeviceNotFoundException {

    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        return false;
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
}
