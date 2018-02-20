/* ---------------------------------------------------------------------
 * ClientHandler.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package Socket;

import java.io.*;
import java.net.Socket;

import java.util.HashSet;

import Data.HeaderList;
import Data.Message;

public class ClientHandler implements Runnable {

    private final jServerSocket server;
    private final Socket        client;
    private boolean             connected;
    private String              username;
    private ObjectInputStream   reader;
    private ObjectOutputStream  writer;

    private HashSet<ClientHandler> clientList;

    public ClientHandler(jServerSocket ssocket, Socket socket, HashSet<ClientHandler> list) throws IOException {
        server     = ssocket;
        client     = socket;
        connected  = false;
        writer     = new ObjectOutputStream(socket.getOutputStream());
        reader     = new ObjectInputStream(socket.getInputStream()) ;
        clientList = list;
    }

    public void updateClientList(HashSet<ClientHandler> list) {
        clientList = list;
    }

    public void sendClientList() {
        StringBuilder sb = new StringBuilder("");
        for (ClientHandler c : clientList) {
            sb.append(c.username);
            sb.append(":");
        }
        System.out.println("DEBUG: Client List " + sb);
        Message m = new Message(HeaderList.UPD, "SERVER", sb.toString());
        writeMessage(m);
    }

    private void writeMessage(Message msg) {
        try {
            writer.writeObject(msg);
            if (msg.getHeader() == HeaderList.MSG)
                server.logMessage(msg);
        } catch (IOException io) { io.printStackTrace(); }
    }

    private boolean checkUsernameValidity(String username) {
        for (ClientHandler ch : clientList) {
            if (ch.username.equals(username)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {

        System.out.println("\tStarting a new listener on Thread #" + Thread.currentThread().getId());
        Message msg;
        int loginAttempts = 0;

        try {

            while (!connected) {
                msg = (Message) reader.readObject();
                if (msg.getHeader() == HeaderList.LOG) {
                    if (checkUsernameValidity(msg.getData())) {
                        connected = true;
                        username = msg.getData();
                        server.getClientList().add(this);
                        clientList.add(this);
                        sendClientList();
                        this.writeMessage(new Message(HeaderList.LOG, "SERVER", "OK"));
                        for (ClientHandler ch : clientList)
                            ch.writeMessage(new Message(HeaderList.MSG, "SERVER", "User " +
                                    username + " has joined the room."));
                    } else {
                        System.out.println("loginattemps++");
                        loginAttempts++;
                    }
                }
                if (loginAttempts >= 3) {
                    server.getClientList().remove(this);
                    System.err.println("Connection reset on Thread #"+Thread.currentThread().getId());
                    Thread.currentThread().interrupt();
                    System.exit(-1);
                }
            }

            while ((msg = (Message) reader.readObject()) != null) {
                switch (msg.getHeader()) {
                   default:
                        break;
                   case HeaderList.MSG:
                        for (ClientHandler ch : clientList)
                            ch.writeMessage(msg);
                        System.out.println("\tReceived message \"" + msg.getData() + "\" from Thread #" + Thread.currentThread().getId());
                        break;
                   case HeaderList.PRV:
                       for (ClientHandler ch : clientList)
                           // Only the sender and receiver are able to see the private message
                           if (ch.username.equals(msg.getDest()) || ch.username.equals(msg.getSender()))
                               ch.writeMessage(msg);
                       break;
                }
            }

        }
        catch (IOException io) { io.printStackTrace(); }
        catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
    }

}
