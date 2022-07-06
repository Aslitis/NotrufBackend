package htw.projektarbeit.udpConnection;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.hibernate.annotations.UpdateTimestamp;

public class test {
    UdpClient client;

    //@Before
    public void setup(String ip) throws SocketException, UnknownHostException{
        //new UdpServer().start();
        System.out.println("Server gestartet!");
        client = new UdpClient(ip);
    }

    //@Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String echo = client.sendEcho("Hello"); 
        //assertEquals("Hello server", echo);
        //echo = client.sendEcho("server is working");  
        //assertFalse(echo.equals("Hello server"));
    }


    public void test(){
        client.sendAudio();
    }

    //@After
    public void tearDown() throws IOException{
        client.sendEcho("end");
        client.close();
    }


    private void start(String ip) throws IOException{
        setup(ip);
        whenCanSendAndReceivePacket_thenCorrect();
        test();
        //tearDown();
    }

    public static void main(String[] args){
        try{
            if(args.length == 0){
                System.out.println("Gib die IP Adresse als Parameter mit!");
                System.exit(1);
            }else{
                new test().start(args[0]);
            }
        }catch(Exception e){
            e.printStackTrace();
        } 

        /*
        try {
            new test().start("localhost");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */
    }
}
