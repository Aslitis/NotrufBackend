package htw.projektarbeit.udpConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

import htw.projektarbeit.audio.AudioThread;

public class UdpClient {
    private DatagramSocket socketIn;
    private DatagramSocket socketOut;
    //private DatagramSocket socket;
    private InetAddress address;
    private int port = 4445;
    private byte[] datagram;

    public UdpClient(String ip) throws SocketException, UnknownHostException{
        //Client: Versende Packete über Port 4446, Empfange Packete an Port 4445
        socketOut = new DatagramSocket(4446); //4446
        socketIn = new DatagramSocket(4445); //4445
        //socket = new DatagramSocket(4445);
        address = InetAddress.getByName(ip); //134.96.217.36
    }

    public String sendEcho(String msg) throws IOException{
        datagram = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(datagram, datagram.length, address, 4445);
        System.out.println("Client: Sende packet " + msg);
        socketOut.send(packet);
        packet = new DatagramPacket(datagram, datagram.length);
        socketIn.receive(packet);
        System.out.println("Client: Server packet erhalten, Addresse " + packet.getAddress().getHostAddress() + ", Port " + packet.getPort());
        String receivedString = new String(packet.getData(), 0, packet.getLength());
        int received = Integer.parseInt(receivedString);
        System.out.println("Client: Nutze Port " + received);
        port = received;
        return "";//received;
    }

    public void sendAudio(){
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

            System.out.println("Client: ByteArray Size is " + bytes);

            AudioThread audioRecord = new AudioThread(targetLine, null, bytes, address, socketOut, false, port);
            AudioThread audioInput = new AudioThread(null, sourceLine, bytes, address, socketIn, true, port);
            audioRecord.start();
            audioInput.start();
            JOptionPane.showMessageDialog(null, "Hit OK for stopping");
            System.out.println("Stopping target and source line ...");
            targetLine.stop();
            sourceLine.stop();
            targetLine.close();
            sourceLine.close();
            audioRecord.stopIt();
            audioInput.stopIt();
            System.out.println("Stopped everything");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public void close(){
        System.out.println("Client: Schließe Socket ...");
        socketIn.close();
        socketOut.close();
    }
}
