package htw.projektarbeit.audio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    private byte[] datagram;
    private InetAddress address;
    private DatagramSocket socket;

    public AudioThread(TargetDataLine targetLine, SourceDataLine sourceLine, int bytes, InetAddress address,
            DatagramSocket socket) {
        this.targetLine = targetLine;
        this.sourceLine = sourceLine;
        this.bytes = bytes;
        this.address = address;
        this.socket = socket;
        datagram = new byte[bytes];
    }

    @Override
    public void run() {
        /*
         * AudioInputStream stream = new AudioInputStream(targetLine);
         * File out = new File("output.wav");
         * try{
         * AudioSystem.write(stream, AudioFileFormat.Type.WAVE, out);
         * }catch(IOException e){
         * e.printStackTrace();
         * }
         * System.out.println("Stopped recording");
         */

        //AudioInputStream stream = new AudioInputStream(targetLine);
        System.out.println("Client: Sende packete ...");
        running.set(true);
        while (running.get()) {
            try {
                // sourceLine.write(stream.readNBytes(bytes), 0, bytes);
                // System.out.println("loop");
                targetLine.read(datagram, 0, bytes);//stream.readNBytes(bytes);
                DatagramPacket packet = new DatagramPacket(datagram, datagram.length, address, 4445);
                socket.send(packet);
                System.out.println("Packet gesendet ...");
                /*
                 * packet = new DatagramPacket(datagram, datagram.length);
                 * socket.receive(packet);
                 * System.out.println("Client: Server packet, Addresse " +
                 * packet.getAddress().getHostAddress() + ", Port " + packet.getPort());
                 * String received = new String(packet.getData(), 0, packet.getLength());
                 * System.out.println("Client: " + received);
                 */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void start() {
        audioRecorder = new Thread(this);
        audioRecorder.start();
    }

    public void stopIt() {
        running.set(false);
    }

}
