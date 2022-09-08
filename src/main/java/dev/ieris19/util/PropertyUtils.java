package dev.ieris19.util;

import lib.ieris19.util.log.Log;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * A class containing miscellaneous utilities related to the properties file
 */
public class PropertyUtils {
	private final static Pattern VALID = Pattern.compile("^[a-zA-Z0-9-]*$");
	private final static Pattern CONTAINS_WHITESPACE = Pattern.compile("[\s\\_]");
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

	public static String guildPropertyName(@NotNull String name) {
		if (VALID.matcher(name).matches()) {
			name = name.toLowerCase();
			Log.getInstance().info("Guild name: " + name + ": valid");
			return name;
		}
		name = CONTAINS_WHITESPACE.matcher(name).replaceAll("-");
		name = CONTAINS_NON_STANDARD.matcher(name).replaceAll("*");
		return guildPropertyName(name);
	}
}
