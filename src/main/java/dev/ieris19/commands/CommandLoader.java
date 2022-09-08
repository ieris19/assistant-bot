package dev.ieris19.commands;

import dev.ieris19.commands.implementations.Command;
import lib.ieris19.util.ClassUtils;
import lib.ieris19.util.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

/**
 * A class that loads all the commands in the {@link dev.ieris19.commands.implementations} package and stores them in a
 * {@link HashMap} for easy access. This way, the commands can be accessed by their name. This class also provides a
 * method to get all the implemented command classes to automatically generate listeners for the commands.
 */
public class CommandLoader {
	/**
	 * An instance of the {@link CommandLoader} class that is used to access the commands.
	 */
	private static CommandLoader instance;

	/**
	 * Creates a new CommandLoader for the given package. If a CommandLoader for the given package already exists, it will
	 * return that CommandLoader instead of creating a new one
	 */
	public static CommandLoader getInstance() {
		if (instance == null) {
			instance = new CommandLoader(Command.class.getPackage());
		}
		return instance;
	}

	/**
	 * A hashmap relating a string to a command object such that the map represents all available commands
	 */
	private final HashMap<String, Command> commandList = new HashMap<>();

	/**
	 * Grants access to the command list from outside classes
	 *
	 * @return a list of all the command classes that are implemented
	 */
	public HashMap<String, Command> getCommandList() {
		if (commandList.isEmpty()) {
			loadCommands();
		}
		return commandList;
	}

	/**
	 * Constructs a new class loader object for the given class's package
	 */
	private CommandLoader(Package pkg) {
		this.commandPackage = pkg;
	}

	/**
	 * Package where the command implementations are stored
	 */
	private final Package commandPackage;

	/**
	 * Adds commands to the {@link #commandList} on the classes in the
	 * {@link dev.ieris19.commands.implementations implementations package} that implement commands
	 */
	public void loadCommands() {
		Set<Class<?>> classes;
		try {
			classes = ClassUtils.getClassesInPackage(commandPackage);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		classes.forEach(commandClass -> {
			if (commandClass.toString().split(" ")[0].equals("interface"))
				return;
			Command command;
			try {
				command = (Command) Class.forName(commandClass.getName()).getDeclaredConstructor().newInstance();
			} catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException |
							 ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			String commandName = commandClass.getSimpleName();
			commandList.put(commandName.replaceAll("Command", "").toLowerCase(), command);
		});
		Log.getInstance().info("Loaded " + commandList.size() + " commands: \n" + commandList.keySet());
	}
}
