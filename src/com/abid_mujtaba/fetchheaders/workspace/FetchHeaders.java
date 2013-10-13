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
        System.out.println("Hello, World");
    }
}
