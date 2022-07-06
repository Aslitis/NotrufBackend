package htw.projektarbeit.udpConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.hibernate.annotations.UpdateTimestamp;

public class Server {
    private boolean twoClients = false;
    private DatagramSocket socket;
    private InetAddress client1;
    private InetAddress client2;

    private void initializeCommunication(){
        System.out.println("Start initializing communication ...");
        byte[] datagram = giveBytesArray();
        if(datagram.length == 0){
            System.out.println("Couldn't initialize Communication!");
            System.exit(0);
        }
        DatagramPacket packet = new DatagramPacket(datagram, datagram.length);
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("www.google.com", 80));
            System.out.println("Server IP: " + socket.getLocalAddress());
            socket.close();
        } catch (IOException e1) {}

        try{
            System.out.println("Waiting for first client ...");
            socket.receive(packet);
            System.out.println("Received message from first client " + packet.getAddress().getHostAddress());
            System.out.println("First Client uses Port 4445");
            client1 = packet.getAddress();
            
            String msg = "4445";
            datagram = msg.getBytes();
            packet = new DatagramPacket(datagram, datagram.length, client1, 4445);
            System.out.println("Client: Sende packet " + msg);
            socket.send(packet);

            while(!twoClients){
                System.out.println("Waiting for second client ...");
                socket.receive(packet);
                if(!packet.getAddress().equals(client1)){
                    System.out.println("Received message from second client " + packet.getAddress().getHostAddress());
                    System.out.println("Second Client uses Port 4447");
                    client2 = packet.getAddress();

                    msg = "4447";
                    datagram = msg.getBytes();
                    packet = new DatagramPacket(datagram, datagram.length, client2, 4445);
                    System.out.println("Client: Sende packet " + msg);
                    socket.send(packet);

                    twoClients = true;
                    socket.close();
                }else{
                    System.out.println("Message received from " + packet.getAddress());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    

    private byte[] giveBytesArray(){
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(dataInfo)) {
            System.out.println("Is not supported");
        }
        int bytes = 0;
        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = targetLine.getBufferSize() / 8;
            bytes = bufferLengthInFrames * frameSizeInBytes;
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new byte[bytes];
    }


    private void startCommunication() throws SocketException, UnknownHostException{
        DatagramSocket socketOut = new DatagramSocket(4446);
        DatagramSocket socketIn1 = new DatagramSocket(4445);
        DatagramSocket socketIn2 = new DatagramSocket(4447);
        UdpServer server1 = new UdpServer(client1, true, socketOut, socketIn2);
        UdpServer server2 = new UdpServer(client2, false, socketOut, socketIn1 );
        server1.start();
        server2.start();
        System.out.println("Server gestartet!");
    }



    private void start() throws IOException{
        socket = new DatagramSocket(4445);
        initializeCommunication();
        startCommunication();
    }

    public static void main(String[] args){
        try {
            new Server().start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
