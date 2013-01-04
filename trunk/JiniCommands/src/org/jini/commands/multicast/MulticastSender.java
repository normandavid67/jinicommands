
package org.jini.commands.multicast;

import java.io.*;
import java.net.*;


public class MulticastSender {

    public static void main(String[] args) {

        byte[] outBuf;
        final int PORT = 8080;

        try {
            DatagramSocket socket = new DatagramSocket();
            long counter = 0;
            String msg;

            while (true) {

                //String rs = radomStringGenerator();
                String rs = RandomString.randomstring();
                
                msg = "This is multicast! " + counter + " " + rs;
                counter++;
                outBuf = msg.getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName("224.2.2.3");
                DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

                socket.send(outPacket);

                System.out.println("Server sends : " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

   
}

class RandomString {

    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }
        return new String(b, 0);
    }

    private static int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    public static String randomstring() {
        return randomstring(0, 1500);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(randomstring());

    }
}