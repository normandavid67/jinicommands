/*
 New BSD License
 Copyright (c) 2012, Norman David <normandavid67@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Norman David BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
  
 For a copy of the License type 'license'
 */
package org.jini.commands.multicast;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import jinicommands.JiniCmd;
import org.apache.commons.cli.*;
import org.jini.commands.helper.JiniCommandsLogger;
import org.jini.commands.helper.TablePrinter;
import org.jini.commands.utils.PrintChar;

public class MulticastSender extends JiniCmd {

    /**
     * MultiCast Sender. Reads data from a file and Sends DatagramPackets via
     * Multicast at regular intervals. DatagramPacket cannot be more than 64
     * Kilobytes large. If the Data Source File changes. The new data will be
     * broadcasted. Default Intervals = 500 MiliSeconds
     *
     * @author Norman David <normandavid67@gmail.com>
     * @since JiniCommands version 0.2
     * @version 1.0
     */
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

    /**
     * Getter method for the Time interval between each Multicast. Defined in
     * MiliSeconds
     *
     * @return
     */
    int getInterval() {
        return interval;
    }

    /**
     * Setter method for the Time interval between each Multicast. Defined in
     * MiliSeconds
     *
     * @param interval
     */
    void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Getter for the Data File name
     *
     * @return
     */
    String getFile() {
        return file;
    }

    /**
     * Setter for the Data File name
     *
     * @param file
     */
    void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Setter for the Message which will be MultiCasted.
     *
     * @param message
     */
    void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for MultiCast Group
     *
     * @return
     */
    String getMulticastGroup() {
        return multicastGroup;
    }

    /**
     * Setter for MultiCast Group
     *
     * @param multicastGroup
     */
    void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }

    /**
     * Getter for Port Number
     *
     * @return
     */
    int getPort() {
        return port;
    }

    /**
     * Setter for Port Number
     *
     * @param port
     */
    void setPort(int port) {
        this.port = port;
    }

    public MulticastSender() {
    }

    /**
     * In this method all the Command specific Command Line options are defined.
     */
    @Override
    @SuppressWarnings("static-access")
    public void setJCLIOptions() {
        Option Help = new Option("h", "help", false, "Show Help.");
        this.jcOptions.addOption(Help);

        this.jcOptions.addOption(OptionBuilder.withLongOpt("interval").withDescription("Interval").hasArg(true).isRequired(false).create("i"));
        /*Needed*/ this.jcOptions.addOption(OptionBuilder.withLongOpt("port").withDescription("Port").hasArg(true).isRequired(false).create("p"));
        /*Needed*/ this.jcOptions.addOption(OptionBuilder.withLongOpt("multicastgroup").withDescription("With Details").hasArg(true).isRequired(false).create("mcg"));
        /*Needed*/ this.jcOptions.addOption(OptionBuilder.withLongOpt("file").withDescription("File to publish").hasArg(true).isRequired(false).create("f"));
        this.jcOptions.addOption(OptionBuilder.withLongOpt("log").withDescription("Log Datagram Messages to Temp Directory").hasArg(false).isRequired(false).create("l"));
    }

    /**
     * In this method all the execution of the specific Command takes place
     */
    @Override
    @SuppressWarnings("static-access")
    public void executeCommand() {

        this.setJCLIOptions();
        String args[] = this.convertToArray();
        try {
            CommandLine jcCmd = this.jcParser.parse(this.jcOptions, args);
            // Read the -h option
            if (jcCmd.hasOption('h')) {
                // Print help 
                printHelp();
            }

            // Reaf the -l option 
            if (jcCmd.hasOption('l')) {
                // Set the flag
                logDatagramMessages = true;
            }


            if ((this.done == false) && (this.jcError == false)) {
                // Read the -i option. Defines the interval between 2 Multicasts
                if (jcCmd.hasOption("i")) {
                    try {
                        int i = Integer.parseInt(jcCmd.getOptionValue("i").trim());
                        //Set the interval
                        this.setInterval(i);
                    } catch (Exception e) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Option defined for -i is not a number");
                    }
                }

                // Read the -p option. Defines the port
                if (jcCmd.hasOption("p")) {
                    try {
                        int p = Integer.parseInt(jcCmd.getOptionValue("p").trim());
                        // Set port
                        this.setPort(p);
                    } catch (Exception e) {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Option defined for -p is not a number");
                    }
                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a Option defined for -p");

                }

                // Read -mcg option. MultiCast Group
                if (jcCmd.hasOption("mcg")) {
                    if (jcCmd.getOptionValue("p").length() > 0) {
                        // Set the MultiCast Group
                        this.setMulticastGroup(jcCmd.getOptionValue("mcg").trim());
                    } else {
                        this.setJcError(true);
                        this.addErrorMessages("Error : Please define a Option defined for -mcg");
                    }
                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a Option defined for -mcg");
                }

                // Read the -f option. Absolute Path to File
                if (jcCmd.hasOption("f")) {
                    String fle = jcCmd.getOptionValue("f").trim();

                    if (fle.length() > 0) {
                        if (this.isFileCanRead(fle)) {
                            // Set the File 
                            this.setFile(fle.trim());
                        } else {
                            this.setJcError(true);
                            this.addErrorMessages("Error : File does not exsist or is unreadable. " + fle);
                        }
                    }
                } else {
                    this.setJcError(true);
                    this.addErrorMessages("Error : Please define a Option defined for -f");
                }

                if (this.jcError == false) {
                    try {
                        // Start Multicast Sender
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
        // Interval in MiliSeconds
        final int INTERVAL = this.getInterval();
        // Port 
        final int PORT = this.getPort();
        // Multicast Group
        String mcastGroup = this.getMulticastGroup();


        String fl = this.getFile();
        String fileName = this.getFile();

        // Read Data from File
        String msg = this.getFileContents(fileName);
        // MD5 CheckSum
        String mdfiveSum = this.checkFileMD5(fileName);

        // Jini Logging
        JiniCommandsLogger jcl = new JiniCommandsLogger();

        System.out.println("Started Multicast Sender. Sending Datagram every : " + INTERVAL + " Miliseconds");
        if (logDatagramMessages == true) {
            System.out.println("Logging to Directory : " + jcl.getJiniLogDir());
        } else {
            System.out.println("Logging not turned on, add -l to start logging. ");

        }
        System.out.println("'c + ENTER' or 'x + ENTER' to stop Multicast Sender.");

        PrintChar printChar = new PrintChar();
        printChar.setOutPutChar(">");


        try {
            Scanner in = new Scanner(System.in);

            printChar.start();

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

                if ((in.nextLine().equalsIgnoreCase("c")) || (in.nextLine().equalsIgnoreCase("x"))) {
                    this.running = false;
                }


                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException ie) {
                    printChar.setStopPrinting(true);
                    this.setJcError(true);
                    this.addErrorMessages("Error : An error occured in the Sleep thread.");
                }
            }
        } catch (IOException ioe) {
            printChar.setStopPrinting(true);
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

        printChar.setStopPrinting(true);
        this.done = true;
    }

    /**
     * Read File Contents
     *
     * @param FileName
     * @return
     */
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

    /**
     * Check if File is Readable
     *
     * @param path
     * @return
     */
    private boolean isFileCanRead(String path) {
        File f = new File(path);
        if ((f.isFile()) && (f.canRead())) {
            return true;
        }
        return false;
    }

    /**
     * Get the MD5 CheckSum
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String checkFileMD5(String file) throws NoSuchAlgorithmException {
        String pass = this.getFileContents(file);
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = pass.getBytes();
        m.update(data, 0, data.length);
        BigInteger i = new BigInteger(1, m.digest());
        return String.format("%1$032X", i);
    }

    private String getOsSpecificTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    private void printHelp() {
        TablePrinter helpTableHead = new TablePrinter("Command Name : ", "multicastsender");
        helpTableHead.addRow("SYNOPSIS : ", "multicastsender [OPTION]...");
        helpTableHead.addRow("DESCRIPTION : ", "Start a MultiCast Sender. Data is read from a File and send Multicast DatagramPacket.");
        helpTableHead.addRow(" ", "");
        helpTableHead.addRow(" ", "Important : 64 kilobytes is the theoretical maximum size of a complete IP datagram.");
        helpTableHead.addRow(" ", "");
        helpTableHead.addRow(" ", "Note : There must be enough space in the temporary directory of your OS. ");
        helpTableHead.addRow(" ", "The Logs are written in the temporary Directory.");
        helpTableHead.addRow(" ", "Temp Directory of your OS : " + this.getOsSpecificTempDirectory());
        helpTableHead.print();
        TablePrinter helpTable = new TablePrinter("Short Opt", "Long Opt", "Argument", "Desc", "Short Option Example", "Long Option Example");
        helpTable.addRow("-h", "--help", "not required", "Show this help.", "multicastsender -h", "multicastsender --help");
        helpTable.addRow("-i", "--interval", " required", "Interval in Miliseconds. Default = 500 Miliseconds", "multicastsender -i 1000 ", "multicastsender --interval 1000");
        helpTable.addRow("-p", "--port", " required", "Port to which the Multicast traffic is published", "multicastsender -p 8080 ", "multicastsender --port 8080");
        helpTable.addRow("-f", "--file", " required", "File which contains the data (Must be Readable)", "multicastsender -f /PATH/TO/FILE ", "multicastsender --file /PATH/TO/FILE");
        helpTable.addRow("", "", " ", "'multicastsender' checks every 11th multicast if contents of file have varied.", "", "");
        helpTable.addRow("-mcg", "--multicastgroup", " required", "Multicast Group to join", "multicastsender -mcg 224.2.2.3", "multicastsender --multicastgroup 224.2.2.3");
        helpTable.print();


        this.done = true;
    }
    // Win
    // multicastsender -p 8888 -f C:\TEMP\test.txt -mcg 224.2.2.3 -l

    // Air
    // multicastsender -p 8888 -f /Users/admin/Documents/TEST/test.txt -mcg 224.2.2.3
    // Mac Desktop
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