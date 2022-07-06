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
    private AtomicBoolean input = new AtomicBoolean(false);
    private TargetDataLine targetLine;
    private SourceDataLine sourceLine;
    private int bytes;
    private Thread audioRecorder;
    private byte[] datagram;
    private InetAddress address;
    private DatagramSocket socket;
    private int port;

    public AudioThread(TargetDataLine targetLine, SourceDataLine sourceLine, int bytes, InetAddress address,
            DatagramSocket socket, boolean input, int port) {
        this.targetLine = targetLine;
        this.sourceLine = sourceLine;
        this.bytes = bytes;
        this.address = address;
        this.socket = socket;
        this.port = port;
        datagram = new byte[bytes];
        this.input.set(input);
    }

    @Override
    public void run() {
    
        //Client: Sende Packete an Port 4446 des Servers, Empfange Packete an Port 4445
        DatagramPacket packet;

        if (input.get()) {
            System.out.println("Client: Erhalte Packete ...");
            packet = new DatagramPacket(datagram, datagram.length, address, 4446);
            running.set(true);
            while (running.get()) {
                try {
                    socket.receive(packet);
                    System.out.println("Packet erhalten von " + packet.getAddress() + ":" + packet.getPort());
                    sourceLine.write(packet.getData(), 0, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else {
            System.out.println("Client: Sende packete ...");
            packet = new DatagramPacket(datagram, datagram.length, address, port);
            running.set(true);
            while (running.get()) {
                try {
                    // sourceLine.write(stream.readNBytes(bytes), 0, bytes);
                    // System.out.println("loop");
                    targetLine.read(datagram, 0, bytes);// stream.readNBytes(bytes);
                    socket.send(packet);
                    System.out.println("Client: Packet gesendet ...");
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

    }

    public void start() {
        audioRecorder = new Thread(this);
        audioRecorder.start();
    }

    public void stopIt() {
        if(!input.get()){
            String message = "end";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, 4445);
            try {
                socket.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        running.set(false);
    }

}
