package htw.projektarbeit.udpConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import htw.projektarbeit.audio.AudioThread;

public class UdpServer extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] datagram; //= new byte[256];
    private AudioInputStream stream;

    public UdpServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    @Override
    public void run() {
        running = true;
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(dataInfo)) {
            System.out.println("Is not supported");
        }
        System.out.println("Server: Server gestartet.");

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getSourceDataLine(format);
            // targetLine.open();
            sourceLine.open();

            // JOptionPane.showMessageDialog(null, "Hit Ok to start recording");
            // targetLine.start();
            sourceLine.start();

            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = targetLine.getBufferSize() / 8;
            final int bytes = bufferLengthInFrames * frameSizeInBytes;
            datagram = new byte[bytes];

            System.out.println("Server: ByteArraySize is " + bytes);

            DatagramPacket packet = new DatagramPacket(datagram, datagram.length);            

            while (running) {
               
                try {
                    socket.receive(packet);
                    System.out.println("Server: Packet erhalten!");
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (received.equals("end")) {
                        running = false;
                        break;
                    }
                    System.out.println("Packet: " + packet.getData().length);
                    System.out.println("Bytes: " + bytes);
                    sourceLine.write(packet.getData(), 0, bytes);
                    // System.out.println("loop");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // JOptionPane.showMessageDialog(null, "Hit OK for stopping");
            System.out.println("Stopping source line ...");
            // targetLine.stop();
            sourceLine.stop();
            // targetLine.close();
            sourceLine.close();
            System.out.println("Stopped everything");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        System.out.println("Server aus.");
        socket.close();
    }
}
