package de.flaflo.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flaflo.language.LanguageManager.Dictionary;
import de.flaflo.main.Main;

/**
 * Zust�ndig f�r den Spawn Befehl
 * 
 * @author Flaflo
 */
public class CommandConsole extends Command {

	public CommandConsole()
	{
		super("c");
	}
	
	@Override
	public boolean execute(final CommandSender arg0, final String arg2, final String[] args) {
		final Player p = (Player) arg0;

		if (args.length == 0) {
			if (!p.isOp() || !p.hasPermission("server.command.dispatch"))
				p.sendMessage("�7[�aConsole�7]�r �cDu besitzt nicht gen�gend Rechte!");
			else
				p.sendMessage("�7[�aConsole�7]�r /c <commad>");
		} else if (args.length > 0)
			if (p.isOp() || p.hasPermission("server.command.dispatch")) {

				final StringBuilder cmdSb = new StringBuilder();

				for (int i = 0; i < args.length; i++) {
					final String cmd = args[i];

					cmdSb.append(cmd);

					if (i < (args.length - 1))
						cmdSb.append(" ");
				}
				
				final String command = cmdSb.toString();

				final PrintStream old = System.out;
				PrintStream cache = null;

				final ByteArrayOutputStream baos = new ByteArrayOutputStream();

				try {
					cache = new PrintStream(baos, true, "utf-8");
				} catch (final UnsupportedEncodingException e) {
				}

				if (cache != null) {
					System.setOut(cache);

					Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), command);

					final String response = baos.toString();
					
					if (!response.isEmpty())
						p.sendMessage("�7[�aConsole�7]�r " + baos.toString());
					else
						p.sendMessage("�7[�aConsole�7]�r Command erfolgreich ausgef�hrt.");
						
					System.setOut(old);
				} else
					p.sendMessage("�7[�aConsole�7]�r �cEs ist ein Fehler aufgetreten!");
			} else
				Main.getInstance().sendMessageLang((Player) arg0, "Clear", Dictionary.ADMIN_RESTRICTED);

		return false;
	}

}
