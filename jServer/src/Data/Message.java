/* ---------------------------------------------------------------------
 * Message.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 *
 * Description:
 *
 *  Message objects are exchanged between each jChat entities.
 *  A Message is defined by the following fields:
 *      - Header: contains a byte value representing the type of message
 *          (see HeaderList.java)
 *      - Sender: who sent the message
 *      - Timestamp: time at which the message was sent
 *      - Data: content of the message
 * --------------------------------------------------------------------- */

package Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private byte   header;
    private String sender;
    private String timestamp;
    private String data;
    private String dest;

    public Message(byte _header, String _sender, String _data) {
       header = _header;
       sender = _sender;
       data   = _data;
       timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public Message(byte _header, String _sender, String _data, String _dest) {
        this(_header, _sender, _data);
        if (header == HeaderList.PRV) {
            dest = _dest;
        }
    }

    public byte getHeader() {
        return header;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return timestamp.substring(timestamp.indexOf(" ")+1);
    }

    public String getSender() {
        return sender;
    }

    public String getData() {
        return data;
    }

    public String getDest() {
        return dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Message) {
            Message m = (Message) o;
            return  m.getHeader() == getHeader() &&
                    m.getSender().equals(getSender()) &&
                    m.getTimestamp().equals(getTimestamp());
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Message { \n\tHeader: " + header + "\n\tSender: "
                    + sender + "\n\tTimestamp: " + timestamp + "\n\tData: " + data + "\n}");
        return str.toString();
    }

    public void encryptMessage(byte[] key) {
        try {
            data = new String(Crypto.encrypt(key, getData().getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptMessage(byte[] key) {
        try {
            data = new String(Crypto.decrypt(key, getData().getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        Message msg = new Message(HeaderList.MSG,"TestSender","SecretMessage");

        System.out.println("Testing Canonical Form");
        System.out.println(msg.toString());

        byte[] key = Crypto.generateKey();
        byte[] rawData = msg.getData().getBytes();
        byte[] encryptedData = Crypto.encrypt(key, rawData);
        byte[] decryptedData = Crypto.decrypt(key, encryptedData);

        System.out.println("\nTesting Encryption on ByteArray");
        System.out.println(new String(rawData));
        System.out.println(new String(encryptedData));
        System.out.println(new String(decryptedData));

        System.out.println("\nTesting Encryption on Strings");
        System.out.println(msg.getData());
        msg.encryptMessage(key);
        System.out.println(msg.getData());
        msg.decryptMessage(key);
        System.out.println(msg.getData());

    }

}
