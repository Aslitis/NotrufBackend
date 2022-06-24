package htw.projektarbeit.audioEcho;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioThread implements Runnable {
    private AtomicBoolean running = new AtomicBoolean(false);
    private TargetDataLine targetLine;
    private SourceDataLine sourceLine;
    private int bytes;
    private Thread audioRecorder;

    public AudioThread(TargetDataLine targetLine, SourceDataLine sourceLine, int bytes){
        this.targetLine = targetLine;
        this.sourceLine = sourceLine;
        this.bytes = bytes;
    }
                
    @Override
    public void run(){
        /*AudioInputStream stream = new AudioInputStream(targetLine);
        File out = new File("output.wav");
        try{
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, out);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Stopped recording");*/
        AudioInputStream stream = new AudioInputStream(targetLine);
        System.out.println("Try writing ...");
        running.set(true);
        while(running.get()){
            try {
                sourceLine.write(stream.readNBytes(bytes), 0, bytes);
                //System.out.println("loop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void start(){
        audioRecorder = new Thread(this);
        audioRecorder.start();
    }

    public void stopIt(){
        running.set(false);
    }
    
}
