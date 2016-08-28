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
		String[] messageParts = message.split(" ");
		switch (messageParts[0])
		{
			case "!time":
				String time = new java.util.Date().toString();
				sendMessage(channel, sender + ": The time is now " + time);
				break;
			case "!javac":
				if (hasIndex(1, messageParts))
				{
					switch (messageParts[1])
					{
						case "-version":
							sendMessage(channel, System.getProperty("java.version"));
							sendMessage(channel, new ScriptEngineManager().getEngineByName("nashorn").toString());
							break;
						default:
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
							break;
					}
				}
				break;
			case "!banword":
				if (hasIndex(1, messageParts))
				{
					sendMessage(channel, "\"" + messageParts[1] + "\" has added to bad words list.");
					badWords.add(messageParts[1]);
				}
				break;
			case "!unbanword":
				if (hasIndex(1, messageParts))
				{
					String badWord = messageParts[1];
					if (badWords.remove(badWord))
						sendMessage(channel, "\"" + badWord + "\" has removed from bad words list.");
					else
						sendMessage(channel, "\"" + badWord + "\" hasn't removed from bad words list.");
				}
				break;
		}

		if (!message.startsWith("!banword ") && !message.startsWith("!unbanword "))
			badWords.stream().filter(message::contains).forEach(badWord -> kick(channel, sender));
	}

	private <T> boolean hasIndex(int index, T[] array)
	{
		return array.length > index;
	}

	private List<String> badWords = new ArrayList<>();
}