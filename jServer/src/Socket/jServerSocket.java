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

import java.io.IOException;

import java.io.StringWriter;
import java.net.Socket;
import java.net.ServerSocket;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// No JSON API is not bundled by default in Java (See: https://javaee.github.io/jsonp/).
import javax.json.*;

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

        // Creates a scheduled executor in charge of logging the chat history into a JSON file every X time units.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                JsonObject model = Json.createObjectBuilder()
                        .add("user", "Duke")
                        .add("date", new SimpleDateFormat().toString())
                        .add("content", "PLACEHOLDER AyyyLMAO")
                        .build();

                StringWriter strWriter = new StringWriter();
                JsonWriter   jsnWriter = Json.createWriter(strWriter);
                jsnWriter.writeObject(model);
                jsnWriter.close();
                System.out.println(strWriter.toString());
            }
        }, 0, 5, TimeUnit.SECONDS);

        // Creates a new listener (ie: executor thread) for each incoming client connection on the server.
        while (true) {
            try {
                Socket client = socket.accept();
                System.out.println("Incoming connection from " + client.getLocalSocketAddress());
                ClientHandler newClient = new ClientHandler(client, clientList);
                pool.execute(newClient);
                clientList.add(newClient);
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
