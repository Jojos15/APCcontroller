import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;

public class MainThread extends com.confusionists.mjdjApi.morph.AbstractMorph {
    @Override
    public String getName() {
        return "Main Thread";
    }

    @Override
    public void init() throws DeviceNotFoundException {

    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        ShortMessageWrapper shortMessageWrapper = messageWrapper.getAsShortMessageWrapper();
        getService().log(shortMessageWrapper.toString());
        shortMessageWrapper.alterData2(4);
        getService().send(shortMessageWrapper);
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
