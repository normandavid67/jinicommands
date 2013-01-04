package org.jini.commands.multicast;

import java.io.*;
import java.net.*;

public class MulticastReceiver {
    public static void main(String[] args) {
        int size = 1024;
        
        byte[] inBuf = new byte[size];
        try {
            //Prepare to join multicast group
            MulticastSocket socket = new MulticastSocket(8080);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);

            while (true) {
                //DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
                DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
                
                socket.receive(inPacket);
                String msg = new String(inBuf, 0, inPacket.getLength());
                System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
