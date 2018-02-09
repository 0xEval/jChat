/* ---------------------------------------------------------------------
 * jClientSocket.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package Socket;

import UI.ClientGUI;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class jClientSocket {

    private ClientGUI      clientGUI;
    private Socket         socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public jClientSocket(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException io) {
            io.printStackTrace();
        }
        clientGUI = new ClientGUI(this);
    }

    public void sendMessage(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void run() {
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                clientGUI.updateChatWindow(inputLine);
                System.out.println("DEBUG: " + inputLine);
            }
        } catch(IOException io) { io.printStackTrace(); }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Server client <hostName> <portNumber>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber  = Integer.parseInt(args[1]);

        jClientSocket client = new jClientSocket(hostName, portNumber);
        client.run();
    }

}
