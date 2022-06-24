package htw.projektarbeit.ssh;

import com.jcraft.jsch.*;
import java.awt.*;

public class Tunnel {
    public static void main(String[] arg){

        int lport;
        String rhost;
        int rport;
    
        try{
          JSch jsch=new JSch();
    
          String host=null;
          if(arg.length>0){
            host=arg[0];
          }
          else{
            //Do something 
            System.exit(1);
          }
          String user=host.substring(0, host.indexOf('@'));
          host=host.substring(host.indexOf('@')+1);
    
          Session session=jsch.getSession(user, host, 22);
    
          //String foo=JOptionPane.showInputDialog("Enter -L port:host:hostport","port:host:hostport");
          //clientPort:serverIP:PortWoEsWirklichHinSoll
          String foo = "4445:134.96.217.36:4445";
          lport=Integer.parseInt(foo.substring(0, foo.indexOf(':')));
          foo=foo.substring(foo.indexOf(':')+1);
          rhost=foo.substring(0, foo.indexOf(':'));
          rport=Integer.parseInt(foo.substring(foo.indexOf(':')+1));
    
          // username and password will be given via UserInfo interface.
          //UserInfo ui=new MyUserInfo();
          session.setUserInfo(null);
    
          session.connect();
    
          //Channel channel=session.openChannel("shell");
          //channel.connect();
    
          int assinged_port=session.setPortForwardingL(lport, rhost, rport);
          System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
        }
        catch(Exception e){
          System.out.println(e);
        }
      }
}
