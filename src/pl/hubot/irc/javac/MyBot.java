package pl.hubot.irc.javac;

import org.jibble.pircbot.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

class MyBot extends PircBot
{
	MyBot()
	{
		this.setName("javac");
	}

	public void onMessage(String channel, String sender,
	                      String login, String hostname, String message)
	{
		if (message.equalsIgnoreCase("!time"))
		{
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
		else if (message.equalsIgnoreCase("!javac -version"))
		{
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("nashorn");
			sendMessage(channel, System.getProperty("java.version"));
			sendMessage(channel, engine.toString());
		}
		else if (message.startsWith("!javac ".toLowerCase()))
		{
			try
			{
				String code = message.substring(6, message.length());
				ScriptEngineManager manager = new ScriptEngineManager();
				ScriptEngine engine = manager.getEngineByName("nashorn");
				sendMessage(channel, engine.eval(code).toString());
			}
			catch (ScriptException ex)
			{
				sendMessage(channel, ex.getMessage());
			}
		}
		else if (message.startsWith("!banword "))
		{
			String badWord = message.substring(9, message.length());
			sendMessage(channel, "\"" + badWord + "\" has added to bad words list.");
			badWords.add(badWord);
		}
		else if (message.startsWith("!unbanword "))
		{
			String badWord = message.substring(11, message.length());
			if (badWords.remove(badWord)) sendMessage(channel, "\"" + badWord + "\" has removed from bad words list.");
			else sendMessage(channel, "\"" + badWord + "\" hasn't removed from bad words list.");
		}

		if (!message.startsWith("!banword ") && !message.startsWith("!unbanword ")) badWords.stream().filter(message::contains).forEach(badWord -> kick(channel, sender));
	}

	private List<String> badWords = new ArrayList<>();
}