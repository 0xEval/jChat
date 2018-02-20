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

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

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

    public String getDest() { return  dest; }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Message { \n\tHeader: " + header + "\n\tSender: "
                    + sender + "\n\tTimestamp: " + timestamp + "\n\tData: " + data + "\n}");
        return str.toString();
    }

    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        byte header   = HeaderList.MSG;
        String sender = "Joe";
        String data   = "This is not a test";
        Message testMessage = new Message(header, sender, data);
        System.out.println(testMessage);

        try {
            byte[] byteMsg = toByteArray(testMessage);
            System.out.println(byteMsg);
            Message msg = (Message)toObject(byteMsg);
            System.out.println(msg);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

    }

}
