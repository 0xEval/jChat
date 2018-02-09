/* ---------------------------------------------------------------------
 * jServerSocket.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package Socket;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class jServerSocket {

    private final ServerSocket     socket;
    private final ExecutorService  pool;
    private final int              poolSize = 16;

    private HashSet<ClientHandler> clientList;

    public jServerSocket(int portNumber) throws IOException {
        socket     = new ServerSocket(portNumber);
        clientList = new HashSet<ClientHandler>();
        pool       = Executors.newFixedThreadPool(poolSize);
    }

    public void run() {
        System.out.println(
                "Server started. Listening on port <" + socket.getLocalPort() + ">"
                + "\nPress <CTRL-C> to close the connection"
                + "\nWaiting for connections"
        );

        // Creating a new listener (ie: executor thread) for each incoming client connection on the server.
        while (true) {
            try {
                Socket client = socket.accept();
                System.out.println("Incoming connection from " + client.getLocalSocketAddress());
                ClientHandler newClient = new ClientHandler(client, clientList);
                pool.execute(newClient);
                clientList.add(newClient);
                for (ClientHandler c : clientList) {
                    c.updateList(newClient);
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
