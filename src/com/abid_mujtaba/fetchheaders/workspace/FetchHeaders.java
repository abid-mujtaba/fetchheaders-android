//import com.abid_mujtaba.fetchheaders.Resources;

import com.sun.mail.imap.IMAPFolder;

import java.util.Properties;

import javax.mail.*;


/**
 * We will use this class as a standalone java application to prototype fetching headers from imap accounts.
 *
 * By working with only java we will save the headache of having to work through the emulator for a piece of code that
 * is essentially pure java.
 *
 * It is for this very reason that no package has been defined for this class. This causes IntelliJ to throw a warning
 * but the file compiles fine by itself.
 *
 * A Makefile is used to compile and execute the file in one go in an attempt to speed up development.
 */

public class FetchHeaders
{
    public static void main(String[] args)
    {
        print("Starting Application\n");

        Properties props = new Properties();

        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", "mail.physics.tamu.edu");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");          // Uses SSL to secure communication
        props.setProperty("mail.imaps.socketFactory.fallback", "false");

        Session imapSession = Session.getInstance(props);

        try
        {
            Store store = imapSession.getStore("imaps");

            // Connect to server by sending username and password:
            store.connect("mail.physics.tamu.edu", "abid_naqvi83", "OSP9isA25tzv3dF2");

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);       // Open Inbox as read-only

            Message[] messages = inbox.getMessages();

            FetchProfile fp = new FetchProfile();
            fp.add(IMAPFolder.FetchProfileItem.HEADERS);        // Fetch header data
            fp.add(FetchProfile.Item.FLAGS);            // Fetch flags

            inbox.fetch(messages, fp);

            Message message = messages[0];

            print(message.getSentDate().toString());
            print(message.getFrom()[0].toString());
            print(message.getSubject());
            print(message.isSet(Flags.Flag.SEEN) + "");

            print("\n" + inbox.getUnreadMessageCount());
        }
        catch (NoSuchProviderException e) {} // Resources.Loge("Unable to get store from imapsession", e); }
        catch (MessagingException e) {} // Resources.Loge("Exception while attempting to connect to mail server", e); }
    }

    public static void print(String message)
    {
        System.out.println(message);
    }
}
