package htw.projektarbeit.udpConnection;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.hibernate.annotations.UpdateTimestamp;

public class Client {
    UdpClient client;

    //@Before
    public void setup(String ip) throws SocketException, UnknownHostException{
        client = new UdpClient(ip);
    }

    //@Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String echo = client.sendEcho("Hello"); 
    }

    public void startSending(){
        client.sendAudio();
    }


    private void start(String ip) throws IOException{
        setup(ip);
        whenCanSendAndReceivePacket_thenCorrect();
        startSending();
    }

    public static void main(String[] args){
        try{
            if(args.length == 0){
                System.out.println("Gib die IP Adresse als Parameter mit!");
                System.exit(1);
            }else{
                new Client().start(args[0]);
            }
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
}
