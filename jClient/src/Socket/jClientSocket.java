/* ---------------------------------------------------------------------
 * jClientSocket.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package Socket;

import Data.HeaderList;
import Data.Message;
import UI.ClientGUI;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class jClientSocket {

    private ClientGUI          clientGUI;
    private Socket             socket;
    private ObjectInputStream  reader;
    private ObjectOutputStream writer;
    private boolean            connected;
    private String             username;

    public jClientSocket(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            writer = new ObjectOutputStream(socket.getOutputStream()); // Reading from the Server
            reader = new ObjectInputStream(socket.getInputStream());   // Writing to the Server
        } catch (IOException io) {
            io.printStackTrace();
        }
        connected = false;
        clientGUI = new ClientGUI(this);
    }

    public boolean isConnected() {
        return connected;
    }

    public void login(String str) {
        Message msg = new Message(HeaderList.LOG, null, str);
        username = str;
        try {
            writer.writeObject(msg);
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void sendMessage(String str) {
        Message msg = new Message(HeaderList.MSG, username, str);
        try {
            writer.writeObject(msg);
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void sendMessage(String str, String dest) {
        Message msg = new Message(HeaderList.PRV, username, str, dest);
        try {
            writer.writeObject(msg);
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void run() {
        Message msg;
        try {

            // Wait for server to acknowledge connection (ie. receiving LOG type message)
            while (!connected) {
                msg = (Message) reader.readObject();
                if (msg.getHeader() == HeaderList.LOG) {
                   connected = true;
                    clientGUI.updateChatWindow(
                            "["+msg.getTime()+"] ["+msg.getSender()+"]: " +
                                    "Connection successful, welcome to the chat room."
                    );
                }
            }

            while ((msg = (Message) reader.readObject()) != null) {
                System.out.println("DEBUG: Message received\n"+msg);
                switch (msg.getHeader()) {
                    default:
                        break;
                    case HeaderList.MSG: // Received a global message
                        clientGUI.updateChatWindow(
                                "["+msg.getTime()+"] ["+msg.getSender()+"]: "+msg.getData()
                        );
                        break;
                    case HeaderList.PRV: // Received a private message
                        if (username.equals(msg.getSender()))
                            clientGUI.updateChatWindow(
                                    "["+msg.getTime()+"] [To "+msg.getDest()+"]: "+msg.getData()
                            );
                        else if (username.equals(msg.getDest()))
                            clientGUI.updateChatWindow(
                                    "["+msg.getTime()+"] [From "+msg.getSender()+"]: "+msg.getData()
                            );
                        break;
                    case HeaderList.UPD: // Received an updated user list as a colon separated string.
                        clientGUI.updateUserList(msg.getData().split(":", -1));
                        System.out.println(new ArrayList<>(Arrays.asList(msg.getData().split(":"))));
                        break;
                }
                System.out.println();
            }

        }
        catch (IOException io) { io.printStackTrace(); }
        catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
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
