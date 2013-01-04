package org.jini.commands.multicast;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jinicommands.JiniCmd;
import org.apache.commons.cli.*;

public class MulticastSender extends JiniCmd {

    int port;
    String multicastGroup;
    String message;
    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMulticastGroup() {
        return multicastGroup;
    }

    public void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MulticastSender() {
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("port").withDescription("Port").hasArg(true).isRequired(false).create("p"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("multicastgroup").withDescription("With Details").hasArg(true).isRequired(false).create("mcg"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to publish").hasArg(true).isRequired(false).create("f"));
    }

    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {

        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            if (jcCmd.hasOption('h')) {

                try {
                   System.out.println(this.checkFileMD5("C:\\TEMP\\test.txt"));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
                }

                printHelp();

            }

            if ((this.done == false) && (this.jcError == false)) {
                if (jcCmd.hasOption("p")) {
                    try {
                        int p = Integer.parseInt(jcCmd.getOptionValue("p").trim());
                        this.setPort(p);
                    } catch (Exception e) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Option defined for -p is not a number");
                    }
                }

                if (jcCmd.hasOption("mcg")) {
                    if (jcCmd.getOptionValue("p").length() > 0) {
                        this.setMulticastGroup(jcCmd.getOptionValue("mcg").trim());
                    } else {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Please define a Option defined for -mcg");
                    }
                }

                if (jcCmd.hasOption("f")) {
                    this.setMessage(this.readMessageFile(jcCmd.getOptionValue("f").trim()));
                }
                System.out.println("Port : " + this.getPort());
                System.out.println("getMulticastGroup : " + this.getMulticastGroup());
                System.out.println("Message : " + this.getMessage());

                if (this.jcError == false) {
                    // this.startMulticastSender();
                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error : " + ex.getMessage());
        }
    }

    private void startMulticastSender() {
        byte[] outBuf;
        final int PORT = this.getPort();
        String msg = this.getMessage();
        String mcastGroup = this.getMulticastGroup();

        try {
            DatagramSocket socket = new DatagramSocket();
            long counter = 0;
            while (true) {
                counter++;
                outBuf = msg.getBytes();

                //Send to multicast IP address and port
                InetAddress address = InetAddress.getByName(mcastGroup);
                DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

                socket.send(outPacket);

                System.out.println("Server sends : (" + counter + ") " + msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }
// multicastsender -p 8888 -f C:\TEMP\test.txt -mcg 224.2.2.3

    public static void main(String[] args) {

        byte[] outBuf;
        final int PORT = 8888;
        String msg;
        try {
            DatagramSocket socket = new DatagramSocket();
            long counter = 0;


            while (true) {
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

    private String readMessageFile(String file) {
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error : Could not find File " + file);
        } catch (IOException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error : Could not read File " + file);
        }
        return "";
    }

    public  String checkFileMD5(String file) throws NoSuchAlgorithmException {


        String pass = this.readMessageFile(file);
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = pass.getBytes();
        m.update(data, 0, data.length);
        BigInteger i = new BigInteger(1, m.digest());
        return String.format("%1$032X", i);
    }

    private void checkFileMD5() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        System.out.println("In ...checkFileMD5");
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream("C:\\TEMP\\test.txt");
        try {
            is = new DigestInputStream(is, md);
            // read stream to EOF as normal...

        } finally {
            is.close();
        }
        byte[] digest = md.digest();
        System.out.println(digest.toString());
    }

    private void printHelp() {
    }
}
