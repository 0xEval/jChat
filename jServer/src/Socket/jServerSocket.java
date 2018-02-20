/* ---------------------------------------------------------------------
 * jServerSocket.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 *
 * Description:
 *  Initializes the server socket on a given port and hostname.
 *  Each incoming connection is handled by a separate executor ClientHandler.
 *  All handlers are kept up-to-date in a hash set server-side.
 *
 * --------------------------------------------------------------------- */

package Socket;

import Data.History;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Data.Message;

public class jServerSocket {

    private final ServerSocket     socket;
    private final ExecutorService  pool;
    private final int              poolSize = 16;

    private HashSet<ClientHandler> clientList;
    protected ArrayList<Message> msgBuffer;

    public jServerSocket(int portNumber) throws IOException {
        socket     = new ServerSocket(portNumber);
        clientList = new HashSet<ClientHandler>();
        pool       = Executors.newFixedThreadPool(poolSize);
        msgBuffer  = new ArrayList<Message>();
    }

    protected HashSet<ClientHandler> getClientList() {
        return clientList;
    }

    public synchronized void logMessage(Message msg) {
       msgBuffer.add(msg);
    }

    public void run() {
        System.out.println(
                "Server started. Listening on port <" + socket.getLocalPort() + ">"
                + "\nPress <CTRL-C> to close the connection"
                + "\nWaiting for connections"
        );

        // Creates a scheduled executor in charge of logging the chat history into a JSON file every X time units.
        History.createHistory();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("Logging chat history...");
                System.out.println(msgBuffer);
                if (!msgBuffer.isEmpty())
                    for (Message m : msgBuffer)
                        History.logMessage(m);
                    msgBuffer.clear();
            }
        }, 60, 60, TimeUnit.SECONDS);

        // Creates a new listener (ie: executor thread) for each incoming client connection on the server.
        while (true) {
            try {
                Socket client = socket.accept();
                System.out.println("Incoming connection from " + client.getLocalSocketAddress());
                ClientHandler newClient = new ClientHandler(this, client, clientList);
                pool.execute(newClient);
                // Send the updated list to each executor
                for (ClientHandler c : clientList) {
                    c.updateClientList(clientList);
                    c.sendClientList();
                }
            } catch (IOException io) {
                io.printStackTrace();
                if (pool != null) pool.shutdown();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Server <portNumber>");
            System.exit(-1);
        }
        final int portNumber = Integer.parseInt(args[0]);
        jServerSocket jChat = new jServerSocket(portNumber);
        jChat.run();
    }

}
