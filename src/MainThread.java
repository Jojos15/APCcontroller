import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.naming.Context;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainThread extends com.confusionists.mjdjApi.morph.AbstractMorph {

    private ArrayList<Playback> playbacks = new ArrayList<>();
    private ArrayList<Stop> stops = new ArrayList<>();
    private ArrayList<Tap> taps = new ArrayList<>();
    private ArrayList<Macro> macros = new ArrayList<>();
    private ArrayList<Fader> faders = new ArrayList<>();
    private ArrayList<Integer> macroToRelease = new ArrayList<>();
    private ArrayList<PlaybackPixel> playbackPixels = new ArrayList<>();
    private int lastFaderCC = 0;
    private boolean switchOn = false;
    private int[] lights = new int[98];
    private long lastFaderMovement = System.currentTimeMillis();
    private boolean entered = false;
    private boolean lastFaderCCb = false;

    public boolean mouseDown = true;
    public boolean sameMouse = false;
    private Timer timer = new Timer("Timer");
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - lastFaderMovement >= 500 && entered) {
                try {
                    Keyboard keyboard = new Keyboard();
                    keyboard.releaseMouse();
                    entered = false;
                    mouseDown = true;
                    sameMouse = true;
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public String getName() {
        return "Main Thread";
    }

    @Override
    public void init() {


        for (int i = 0; i < Variables.playbacks.length; i += 2) {
            playbacks.add(new Playback(Integer.parseInt(Variables.playbacks[i]), Variables.playbacks[i + 1]));
        }
        for (int i = 0; i < Variables.stops.length; i += 2) {
            stops.add(new Stop(Integer.parseInt(Variables.stops[i]), Variables.stops[i + 1]));
        }
        for (int i = 0; i < Variables.taps.length; i += 2) {
            taps.add(new Tap(Integer.parseInt(Variables.taps[i]), Variables.taps[i + 1]));
        }
        for (int i = 0; i < Variables.macros.length; i += 3) {
            boolean flag = false;
            if (Variables.macros[i + 2].equals("TRUE")) {
                flag = true;
            }
            macros.add(new Macro(Integer.parseInt(Variables.macros[i]), Variables.macros[i + 1], flag));
        }
        for (int i = 0; i < Variables.fadersX.length; i += 2) {
            if (i == 16) {
                faders.add(new Fader(Variables.fadersX[i], Variables.fadersX[i + 1], Variables.faders_higher_position));
            } else {
                faders.add(new Fader(Variables.fadersX[i], Variables.fadersX[i + 1], Variables.faders_lower_position));
            }
        }
        for (int i = 0; i < 10; i++) {
            if (i < 8) {
                playbackPixels.add(new PlaybackPixel(Integer.parseInt(Variables.playbacks[i * 2]), Integer.parseInt(Variables.stops[i * 2]), Variables.testPixelsX[i], Variables.testPixelsY));
            } else
                playbackPixels.add(new PlaybackPixel(Integer.parseInt(Variables.playbacks[i * 2]), Variables.testPixelsX[i], Variables.testPixelsY));
        }
        // Starting Animation
        for (int i = 0; i < 98; i++) {
            try {
                lights[i] = 0;
                getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i, Variables.GREEN)));
                if (i > 0) {
                    getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i - 1, Variables.RED)));
                }
                if (i > 1) {
                    getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i - 2, Variables.YELLOW)));
                }
                if (i > 2) {
                    getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, i - 3, 0)));
                }

                Thread.sleep(35);
            } catch (InvalidMidiDataException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            writeLightsToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GLOBAL TAP START
        /*GlobalTap globalTap = new GlobalTap(this);
        Thread thread = new Thread(globalTap);
        thread.start();*/

        timer.schedule(task, 0, 200);
    }

    @Override
    public boolean process(MessageWrapper messageWrapper, String s) throws Throwable {
        ShortMessageWrapper shortMessageWrapper = messageWrapper.getAsShortMessageWrapper();
        //CASE: NOTE ON
        if (shortMessageWrapper.isNoteOn()) {
            //CASE 2 ON KEYS EXCEPTION
            if (shortMessageWrapper.getData1() != 88 && shortMessageWrapper.getData1() != 89 && shortMessageWrapper.getData1() != 98) {
                //FIND IN WHICH CATEGORY IT IS
                if (shortMessageWrapper.getData1() <= 7) { // STOP
                    int position = (shortMessageWrapper.getData1() + Variables.stops_offset) % 10;

                    Keyboard keyboard = new Keyboard();
                    keyboard.type(stops.get(position).getVirtualKey());

                    //SET LEDS
                    //TODO: ONLY FOR MY APC
                    if (shortMessageWrapper.getData1() == 6 || shortMessageWrapper.getData1() == 7) {
                        setLed(stops.get(position).isOn(), shortMessageWrapper, Variables.GREEN, Variables.OFF);
                    }
                    setLed(stops.get(position).isOn(), shortMessageWrapper, Variables.YELLOW, Variables.OFF);


                } else if (shortMessageWrapper.getData1() <= 15) { // TAP
                    Keyboard keyboard = new Keyboard();
                    keyboard.type(taps.get((shortMessageWrapper.getData1() + Variables.taps_offset) % 10).getVirtualKey());
                } else if (shortMessageWrapper.getData1() <= 63 && shortMessageWrapper.getData1() >= 56) { // MACRO
                    int position = (shortMessageWrapper.getData1() + Variables.macros_offset) % 10;

                    Keyboard keyboard = new Keyboard();
                    keyboard.type(macros.get(position).getVirtualKey());

                    //SET LEDS
                    macros.get(position).setOn(!macros.get(position).isOn());
                    setLed(macros.get(position).isOn(), shortMessageWrapper, Variables.RED, Variables.OFF);

                    if (macros.get(position).isHold()) {
                        macroToRelease.add(shortMessageWrapper.getData1());
                    }

                } else if (shortMessageWrapper.getData1() <= 71 && shortMessageWrapper.getData1() >= 64) { // PLAYBACK
                    int position = (shortMessageWrapper.getData1() + Variables.playbacks_offset) % 10;

                    Keyboard keyboard = new Keyboard();
                    keyboard.type(playbacks.get(position).getVirtualKey());

                    //SET LEDS
                    playbacks.get(position).setOn(!playbacks.get(position).isOn());
                    setLed(playbacks.get(position).isOn(), shortMessageWrapper, Variables.RED, Variables.OFF);
                    if (!playbacks.get(position).isOn()) {
                        stops.get(position).setOn(false);
                        ShortMessageWrapper messageWrapper1 = shortMessageWrapper;
                        messageWrapper1.alterData1(stops.get(position).getCc());
                        setLed(stops.get(position).isOn(), messageWrapper1, 0, Variables.OFF);
                    } else stops.get(position).setOn(true);
                } else if (shortMessageWrapper.getData1() == 82) {
                    writeLightsToFile();
                    getService().log("Should be writen to lights txt");
                } else if (shortMessageWrapper.getData1() == 87) {
                    checkPixels();
                }
            } else if (shortMessageWrapper.getData1() == 98) {
                switchOn = !switchOn;
                ShortMessageWrapper messageToSend = shortMessageWrapper;
                messageToSend.alterData1(87);
                if (switchOn) {
                    setLed(true, messageToSend, Variables.GREEN, Variables.OFF);
                } else {
                    setLed(false, shortMessageWrapper, Variables.GREEN, Variables.OFF);
                }
            } else {
                //TODO: ONLY FOR MY APC
                int position = 9;
                if (shortMessageWrapper.getData1() == 88) position = 8;
                Keyboard keyboard = new Keyboard();
                keyboard.type(playbacks.get(position).getVirtualKey());

                //SET LEDS
                playbacks.get(position).setOn(!playbacks.get(position).isOn());
                setLed(playbacks.get(position).isOn(), shortMessageWrapper, Variables.GREEN, Variables.OFF);

            }
        } else if (shortMessageWrapper.isNoteOff()) {
            int i = 0;
            int toDelete = 9999;
            for (int cc : macroToRelease) {
                if (shortMessageWrapper.getData1() == cc) {
                    int position = (shortMessageWrapper.getData1() + Variables.macros_offset) % 10;

                    Keyboard keyboard = new Keyboard();
                    keyboard.type(macros.get(position).getVirtualKey());

                    macros.get(position).setOn(false);
                    ShortMessageWrapper message = shortMessageWrapper;
                    message.alterCommand(0x90);
                    setLed(false, message, 0, Variables.OFF);
                    toDelete = i;
                    checkPixels();
                }
                i++;
            }
            if (toDelete != 9999) {
                macroToRelease.remove(toDelete);
            }
        } else if (shortMessageWrapper.isControlChange()) {
            Keyboard keyboard = new Keyboard();
            lastFaderMovement = System.currentTimeMillis();
            lastFaderCCb = false;

            if(lastFaderCC != shortMessageWrapper.getData1()){
                mouseDown = true;
                lastFaderCCb = true;
            }
            else if(sameMouse){
                sameMouse = false;
                mouseDown = true;
            }
            else mouseDown = false;

            if (switchOn && (shortMessageWrapper.getData1() == 54 || shortMessageWrapper.getData1() == 55)) {
                if (shortMessageWrapper.getData1() == 54) {
                    faders.get(9).setPreY(keyboard.moveFader(faders.get(9).getX(), faders.get(9).getPreY(), shortMessageWrapper, lastFaderCCb,mouseDown));
                } else {
                    faders.get(10).setPreY(keyboard.moveFader(faders.get(10).getX(), faders.get(10).getPreY(), shortMessageWrapper, lastFaderCCb,mouseDown));
                }
            } else {
                int position = (shortMessageWrapper.getData1() + Variables.faders_offset) % 10;
                faders.get(position).setPreY(keyboard.moveFader(faders.get(position).getX(), faders.get(position).getPreY(), shortMessageWrapper, lastFaderCCb, mouseDown));
            }
            entered = true;
            lastFaderCC = shortMessageWrapper.getData1();

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

    public void setLed(boolean status, ShortMessageWrapper messageWrapper, int colorOn, int colorOff) {
        if (status) {
            messageWrapper.alterData2(colorOn);
            getService().send(messageWrapper);
            lights[messageWrapper.getData1()] = colorOn;
        } else {
            messageWrapper.alterData2(colorOff);
            getService().send(messageWrapper);
            lights[messageWrapper.getData1()] = colorOff;
        }
    }

    public void writeLightsToFile() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("lights.txt");
        for (int i = 0; i < 98; i++) {
            fileOutputStream.write(lights[i]);
        }
        fileOutputStream.close();
    }

    public void checkPixels() throws AWTException, InvalidMidiDataException {
        for (PlaybackPixel p : playbackPixels) {

            if (!(p.checkPixelColor(255, 255, 255)) && lights[p.getPlaybackCC()] > 0) {
                getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, p.getPlaybackCC(), 0)));
                for (Playback pl : playbacks) {
                    if (pl.getCc() == p.getPlaybackCC()) {
                        pl.setOn(false);
                        break;
                    }
                }
                lights[p.getPlaybackCC()] = 0;
                if (p.isHasPause()) {
                    getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, p.getPauseCC(), 0)));
                    for (Stop s : stops) {
                        if (s.getCc() == p.getPauseCC()) {
                            s.setOn(false);
                            break;
                        }
                    }
                    lights[p.getPauseCC()] = 0;
                }
            } else if (p.checkPixelColor(255, 255, 255) && lights[p.getPlaybackCC()] == 0) {
                getService().send(MessageWrapper.newInstance(new ShortMessage(ShortMessage.NOTE_ON, 0, p.getPlaybackCC(), 3)));
                for (Playback pl : playbacks) {
                    if (pl.getCc() == p.getPlaybackCC()) {
                        pl.setOn(true);
                        break;
                    }
                }
                if (p.isHasPause()) {
                    for (Stop s : stops) {
                        if (s.getCc() == p.getPauseCC()) {
                            s.setOn(true);
                            break;
                        }
                    }
                }
                lights[p.getPlaybackCC()] = 3;
            }
        }
    }
}
