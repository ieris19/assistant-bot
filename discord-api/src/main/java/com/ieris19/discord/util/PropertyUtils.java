package com.ieris19.discord.util;

import com.ieris19.lib.util.log.Log;
import com.ieris19.lib.util.log.core.IerisLog;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * A class containing miscellaneous utilities related to the properties file
 */
public class PropertyUtils {
	/**
	 * Pattern used to validate the name to be used in the properties file
	 */
	private final static Pattern VALID = Pattern.compile("^[a-zA-Z0-9-\\.]*$");
	/**
	 * The pattern used to match whitespaces, backslashes and underscores
	 */
	private final static Pattern CONTAINS_WHITESPACE = Pattern.compile("[\s\\_]");
	/**
	 * The pattern used to match any character that is not a letter, a number or a dash
	 */
	private final static Pattern CONTAINS_NON_STANDARD = Pattern.compile("[^a-zA-Z0-9-]");

	/**
	 * Returns the guild name to be saved in the properties file
	 *
	 * @param guild guild to be saved
	 *
	 * @return the guild name to be saved in the properties file
	 */
	public static String guildPropertyName(@NotNull Guild guild) {
		return guildPropertyName(guild.getName());
	}

	/**
	 * Recursively processes the guild name to be saved in the properties file
	 * @param name the name to be processed
	 * @return the next stage of the name to be saved in the properties file
	 */
	public static String guildPropertyName(@NotNull String name) {
		if (VALID.matcher(name).matches()) {
			name = name.toLowerCase();
			IerisLog.getInstance().info("Guild name: " + name + ": valid");
			return name;
		}
		name = CONTAINS_WHITESPACE.matcher(name).replaceAll("-");
		name = CONTAINS_NON_STANDARD.matcher(name).replaceAll("*");
		return guildPropertyName(name);
	}
}
