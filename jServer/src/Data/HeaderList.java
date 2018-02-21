/* ---------------------------------------------------------------------
 * HeaderList.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 *
 * Description:
 *
 *  Global byte values used as headers in Message objects.
 * --------------------------------------------------------------------- */

package Data;

public class HeaderList {

    public static final byte LOG  = 0; // User logging in.
    public static final byte MSG  = 1; // User sending a global message.
    public static final byte PRV  = 2; // User sending a private message.
    public static final byte SHR  = 3; // User sharing a file.
    public static final byte BYE  = 4; // User logging out.
    public static final byte UPD  = 5; // Server updating user list.

}
