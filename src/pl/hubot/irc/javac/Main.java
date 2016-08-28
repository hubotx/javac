package pl.hubot.irc.javac;

import org.jibble.pircbot.IrcException;

import java.io.IOException;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			// Now start our bot up.
			MyBot bot = new MyBot();

			// Enable debugging output.
			bot.setVerbose(true);

			// Connect to the IRC server.
			bot.connect("irc.freenode.net");

			// Join the #pircbot channel.
			bot.joinChannel("#4programmers");
		}
		catch (IrcException | IOException ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}
