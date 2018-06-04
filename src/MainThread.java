import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;

import java.util.ArrayList;

public class MainThread extends com.confusionists.mjdjApi.morph.AbstractMorph {

    private ArrayList<Playback> playbacks = new ArrayList<>();
    private ArrayList<Stop> stops = new ArrayList<>();
    private ArrayList<Tap> taps = new ArrayList<>();
    private ArrayList<Macro> macros = new ArrayList<>();
    private ArrayList<Fader> faders = new ArrayList<>();
    private int lastFaderCC = 0;


    @Override
    public String getName() {
        return "Main Thread";
    }

    @Override
    public void init() throws DeviceNotFoundException {
        for(int i=0;i<Variables.playbacks.length;i+=2){
            playbacks.add(new Playback(Integer.parseInt(Variables.playbacks[i]), Variables.playbacks[i+1]));
        }
        for(int i=0;i<Variables.stops.length;i+=2){
            stops.add(new Stop(Integer.parseInt(Variables.stops[i]), Variables.stops[i+1]));
        }
        for(int i=0;i<Variables.taps.length;i+=2){
            taps.add(new Tap(Integer.parseInt(Variables.taps[i]), Variables.taps[i+1]));
        }
        for(int i=0;i<Variables.macros.length;i+=3){
            boolean flag = false;
            if(Variables.macros[i+2].equals("TRUE")){
                flag = true;
                getService().log(flag + "");
            }
            macros.add(new Macro(Integer.parseInt(Variables.macros[i]), Variables.macros[i+1], flag));
        }
    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        ShortMessageWrapper shortMessageWrapper = messageWrapper.getAsShortMessageWrapper();
        //CASE: NOTE ON
        if(shortMessageWrapper.isNoteOn()) {
            //CASE 2 ON KEYS EXCEPTION
            if (shortMessageWrapper.getData1() != 88 && shortMessageWrapper.getData1() != 89) {
                //FIND IN WHICH CATEGORY IT IS
                if (shortMessageWrapper.getData1()<=7){ // STOP
                    Keyboard keyboard = new Keyboard();
                    keyboard.type(stops.get((shortMessageWrapper.getData1()+Variables.stops_offset)%10).getVirtualKey());
                }
                else if(shortMessageWrapper.getData1()<=15){ // TAP
                    Keyboard keyboard = new Keyboard();
                    keyboard.type(taps.get((shortMessageWrapper.getData1()+Variables.taps_offset)%10).getVirtualKey());
                }
                else if(shortMessageWrapper.getData1()<=63&&shortMessageWrapper.getData1()>=56){ // MACRO
                    Keyboard keyboard = new Keyboard();
                    keyboard.type(macros.get((shortMessageWrapper.getData1()+Variables.macros_offset)%10).getVirtualKey());
                }
                else if(shortMessageWrapper.getData1()<=71&&shortMessageWrapper.getData1()>=64) { // PLAYBACK
                    Playback p = playbacks.get((shortMessageWrapper.getData1()+Variables.playbacks_offset)%10);// NOT WORKING

                    Keyboard keyboard = new Keyboard();
                    keyboard.type(p.getVirtualKey());

                    //SET LEDS
                    p.setOn(!p.isOn());
                    setLed(p.isOn(), shortMessageWrapper, Variables.RED, Variables.OFF);
                }
            }
            else{
                //DOESN'T WORK CHANGE TO FOR
               for(Playback p : playbacks){
                    if(p.getCc() == shortMessageWrapper.getData1()){
                        Keyboard keyboard = new Keyboard();
                        keyboard.type(p.getVirtualKey());

                        p.setOn(!p.isOn());
                        setLed(p.isOn(), shortMessageWrapper, Variables.GREEN, Variables.OFF);
                        break;
                    }
                }
            }
        }
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

    public void setLed(boolean status, ShortMessageWrapper messageWrapper, int colorOn, int colorOff){
        if(status){
            messageWrapper.alterData2(colorOn);
            getService().send(messageWrapper);
        }
        else{
            messageWrapper.alterData2(colorOff);
            getService().send(messageWrapper);
        }
    }
}
