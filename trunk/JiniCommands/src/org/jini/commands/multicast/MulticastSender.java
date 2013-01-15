package org.jini.commands.multicast;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jinicommands.JiniCmd;
import org.apache.commons.cli.*;
import org.jini.commands.helper.JiniCommandsLogger;

public class MulticastSender extends JiniCmd {

    int interval = 500;
    int port;
    String multicastGroup;
    String message;
    String file;
    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    private boolean running = true;
    boolean logDatagramMessages = false;

    int getInterval() {
        return interval;
    }

    void setInterval(int interval) {
        this.interval = interval;
    }

    String getFile() {
        return file;
    }

    void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    String getMulticastGroup() {
        return multicastGroup;
    }

    void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }

    int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    public MulticastSender() {
    }

    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("interval").withDescription("Interval").hasArg(true).isRequired(false).create("i"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("port").withDescription("Port").hasArg(true).isRequired(false).create("p"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("multicastgroup").withDescription("With Details").hasArg(true).isRequired(false).create("mcg"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to publish").hasArg(true).isRequired(false).create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("log").withDescription("Log Datagram Messages to Temp Directory").hasArg(false).isRequired(false).create("l"));
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

            if (jcCmd.hasOption('l')) {
                logDatagramMessages = true;
            }


            if ((this.done == false) && (this.jcError == false)) {
                if (jcCmd.hasOption("i")) {
                    try {
                        int i = Integer.parseInt(jcCmd.getOptionValue("i").trim());
                        this.setInterval(i);
                    } catch (Exception e) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Option defined for -i is not a number");
                    }
                }

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

                    try {
                        this.startMulticastSender();
                    } catch (NoSuchAlgorithmException ex) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : " + ex);
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

        final int INTERVAL = this.getInterval();
        final int PORT = this.getPort();
        String mcastGroup = this.getMulticastGroup();

        String fl = this.getFile();
        String fileName = this.getFile();

        // initial Data
        String msg = this.getFileContents(fileName);
        String mdfiveSum = this.checkFileMD5(fileName);

        JiniCommandsLogger jcl = new JiniCommandsLogger();



        try {
            DatagramSocket socket = new DatagramSocket();
            long counter = 0;


            while (this.running) {
                // Every 10th broadcast the file is checked if it has changed (MD5). 
                // If it has the new data is read and broadcasted
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


                if (logDatagramMessages == true) {
                    jcl.writeLog(msg);
                }


                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException ie) {
                    this.setJcError(true);
                    this.addErrorMessages("Error : An error occured in the Sleep thread.");
                }




            }
        } catch (IOException ioe) {
            /*
             DatagramPacket is just a wrapper on a UDP based socket, so the usual UDP rules apply.
             64 kilobytes is the theoretical maximum size of a complete IP datagram, but only 576 bytes are guaranteed to be routed. On any given network path, the link with the smallest Maximum Transmit Unit will determine the actual limit. (1500 bytes, less headers is the common maximum, but it is impossible to predict how many headers there will be so its safest to limit messages to around 1400 bytes.)
             If you go over the MTU limit, IPv4 will automatically break the datagram up into fragments and reassemble them at the end, but only up to 64 kilobytes and only if all fragments make it through. If any fragment is lost, or if any device decides it doesn't like fragments, then the entire packet is lost.
             As noted above, it is impossible to know in advance what the MTU of path will be. There are various algorithms for experimenting to find out, but many devices do not properly implement (or deliberately ignore) the necessary standards so it all comes down to trial and error. Or you can just guess 1400 bytes per message.
             As for errors, if you try to send more bytes than the OS is configured to allow, you should get an EMSGSIZE error or its equivalent. If you send less than that but more than the network allows, the packet will just disappear.
             */

            this.setJcError(true);
            this.addErrorMessages("Info : 64 kilobytes is the theoretical maximum size of a complete IP Datagram");
            this.addErrorMessages("Error : " + ioe);
        }
    }

    private String getTimeStamp() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("PST"));
        return df.format(new Date());
    }

    /**
     * Getter method for OS specific temporary file
     *
     * @return
     */
    private String getOsSpecificTempDirectory() {
        return System.getProperty("java.io.tmpdir");
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

    private void printHelp() {
    }

    // multicastsender -p 8888 -f C:\TEMP\test.txt -mcg 224.2.2.3
// multicastsender -p 8888 -f /Users/admin/Documents/TEST/test.txt -mcg 224.2.2.3
// multicastsender -p 8888 -f /Users/norman/TEMP/test.txt -mcg 224.2.2.3
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
}