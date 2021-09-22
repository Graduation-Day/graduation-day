package com.graduation.utils;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;


public class TextToSpeech {

    public static boolean isMuted = false;

    public static void speak(String text) {
        if (!isMuted) {
            try {
                //setting properties as Kevin Dictionary
                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
                //registering speech engine
                Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
                //create a Synthesizer that generates voice
                Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
                //allocates a synthesizer
                synthesizer.allocate();
                //resume a Synthesizer
                synthesizer.resume();
                //speak the specified text until the QUEUE become empty
                synthesizer.speakPlainText(text, null);
                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}