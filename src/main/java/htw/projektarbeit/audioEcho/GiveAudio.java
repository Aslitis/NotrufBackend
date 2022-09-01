package htw.projektarbeit.audioEcho;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;
import javax.xml.transform.Source;

public class GiveAudio {

    public static void main(String[] args){
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, format);
        if(!AudioSystem.isLineSupported(dataInfo)){
            System.out.println("Is not supported");
        }

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getSourceDataLine(format);
            targetLine.open();
            sourceLine.open();

            JOptionPane.showMessageDialog(null, "Hit Ok to start recording");
            targetLine.start();
            sourceLine.start();

            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = targetLine.getBufferSize() / 8;
            final int bytes = bufferLengthInFrames * frameSizeInBytes;

            AudioThread audioRecord = new AudioThread(targetLine, sourceLine, bytes);
            audioRecord.start();
            JOptionPane.showMessageDialog(null, "Hit OK for stopping");
            System.out.println("Stopping target and source line ...");
            targetLine.stop();
            sourceLine.stop();
            targetLine.close();
            sourceLine.close();
            audioRecord.stopIt();
            System.out.println("Stopped everything");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        System.exit(0);
        
    }
}
