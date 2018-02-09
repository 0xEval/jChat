/* ---------------------------------------------------------------------
 * ClientHandler.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 * --------------------------------------------------------------------- */

package Socket;

import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.HashSet;

public class ClientHandler implements Runnable {

    private final Socket         client;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private HashSet<ClientHandler> clientList;

    public ClientHandler(Socket socket, HashSet<ClientHandler> list) throws IOException {
        client     = socket;
        reader     = new BufferedReader(new InputStreamReader(client.getInputStream()));
        writer     = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        clientList = list;
    }

    public void updateList(ClientHandler ch) {
        if (!ch.equals(this)) {
            clientList.add(ch);
            System.out.println("\t\tDEBUG: Updating client list [#" + Thread.currentThread().getId() + "]");
        }
    }

    private void writeMessage(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException io) { io.printStackTrace(); }
    }

    @Override
    public void run() {
        System.out.println("\tStarting a new listener on Thread #" + Thread.currentThread().getId());
        try {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                for (ClientHandler ch : clientList) ch.writeMessage(inputLine);
                System.out.println(
                        "\tReceived message \"" + inputLine + "\" from Thread #" + Thread.currentThread().getId());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
