package org.jini.commands.multicast;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    String file;
    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

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
                    String fle = jcCmd.getOptionValue("f").trim();

                    if (fle.length() > 0) {
                        if (this.isFileCanRead(fle)) {
                            this.setFile(fle.trim());
                        } else {
                            this.setJcError(true);
                            this.addErrorMessages("Error : File does not exsist or is unreadable. " + fle);
                        }
                    }
                }

                if (this.jcError == false) {

                    System.out.println("Port : " + this.getPort());
                    System.out.println("getMulticastGroup : " + this.getMulticastGroup());
                    System.out.println("Message : " + this.getMessage());
                    System.out.println("File : " + this.getFile());
                    try {
                        this.startMulticastSender();
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        System.out.println(this.checkFileMD5(this.getFile()));
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
                    }


                }
            }

        } catch (org.apache.commons.cli.ParseException ex) {
            this.setJcError(true);
            this.addErrorMessages("Error : " + ex.getMessage());
        }
    }

    private void startMulticastSender() throws NoSuchAlgorithmException {
        byte[] outBuf;

        final int PORT = this.getPort();
        String mcastGroup = this.getMulticastGroup();

        String fl = this.getFile();
        String fileName = this.getFile();

        // initial Data
        String msg = this.getFileContents(fileName);
        String mdfiveSum = this.checkFileMD5(fileName);

        try {
            DatagramSocket socket = new DatagramSocket();
            long counter = 0;
            while (true) {

                counter++;
                if (counter == 10) {
                    if (mdfiveSum.equals(this.checkFileMD5(fileName)) == false) {
                        msg = this.getFileContents(fileName);
                    }
                    counter = 0;
                }


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
// multicastsender -p 8888 -f /Users/admin/Documents/TEST/test.txt -mcg 224.2.2.3

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

    private String getFileContents(String FileName) {
        StringBuilder resStr = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(FileName));
            String str;
            while ((str = in.readLine()) != null) {
                resStr.append(str);
            }
            in.close();
        } catch (IOException e) {
            this.setJcError(true);
            this.addErrorMessages("Error :" + e.toString());
        }
        return resStr.toString();
    }

    private boolean isFileCanRead(String path) {
        File f = new File(path);
        if ((f.isFile()) && (f.canRead())) {
            return true;
        }
        return false;
    }

    public String checkFileMD5(String file) throws NoSuchAlgorithmException {
        String pass = this.getFileContents(file);
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = pass.getBytes();
        m.update(data, 0, data.length);
        BigInteger i = new BigInteger(1, m.digest());
        return String.format("%1$032X", i);
    }

//    private void checkFileMD5() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
//        System.out.println("In ...checkFileMD5");
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        InputStream is = new FileInputStream("/Users/norman/TEMP/test.txt");
//        try {
//            is = new DigestInputStream(is, md);
//            // read stream to EOF as normal...
//
//        } finally {
//            is.close();
//        }
//        byte[] digest = md.digest();
//        System.out.println(digest.toString());
//    }
    private void printHelp() {
    }
}
