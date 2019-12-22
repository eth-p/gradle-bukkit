package dev.ethp.bukkit.gradle.extension;

import java.util.regex.Pattern;

class Patterns {

	static final Pattern VALID_COMMAND_NAME = Pattern.compile("^\\S+$");
	static final Pattern VALID_PERMISSION_NAME = Pattern.compile("^\\S+$");
	static final Pattern VALID_PLUGIN_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_\\-]+$");


}
