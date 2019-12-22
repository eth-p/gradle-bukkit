package dev.ethp.bukkit.gradle.extension;

import org.gradle.api.InvalidUserDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitApiExtension {

	// -------------------------------------------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------------------------------------------

	static private Pattern VERSION_PATTERN = Pattern.compile("^([0-9]+)\\.([0-9]+)(?:\\.([0-9]+))?(-.*)?$");


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: libraryVersion
	// Required.
	//
	// The API library version.
	// This is the version used to lookup Maven artifacts.
	//
	// api {
	//     version '1.15.1'
	//     libraryVersion = '1.15.1-R0.1-SNAPSHOT'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String libraryVersion;

	public String getLibraryVersion() {
		return this.libraryVersion;
	}

	public void setLibraryVersion(String version) {
		this.libraryVersion = version;
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: manifestVersion
	// Required.
	//
	// The manifest API version.
	// This is used to generate the plugin manifest.
	//
	// api {
	//     version '1.15.1'
	//     manifestVersion = '1.15'
	// }
	// -------------------------------------------------------------------------------------------------------------

	private String manifestVersion;

	public String getManifestVersion() {
		return this.manifestVersion;
	}

	public void setManifestVersion(String version) {
		this.manifestVersion = version;
	}


	// -------------------------------------------------------------------------------------------------------------
	// PROPERTY: databaseSupport
	// Optional.
	//
	// Whether or not the plugin requires access to the Bukkit database API.
	//
	// api {
	//     databaseSupport()
	//     databaseSupport = true
	// }
	// -------------------------------------------------------------------------------------------------------------

	private Boolean databaseSupport;

	public Boolean getDatabaseSupport() {
		return this.databaseSupport;
	}

	public void setDatabaseSupport(Boolean enabled) {
		this.databaseSupport = enabled;
	}

	public void databaseSupport() {
		this.setDatabaseSupport(true);
	}


	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Sets both the manifest and library versions.
	 *
	 * A version string in one of the following formats is expected:
	 *
	 * <ul>
	 *     <li>{@code "1.14"}</li>
	 *     <li>{@code "1.14.4"}</li>
	 *     <li>{@code "1.15.1-pre1"}</li>
	 *     <li>{@code "1.15.1-R0.1-SNAPSHOT"}</li>
	 * </ul>
	 *
	 * @param version The API version string.
	 */
	public void version(String version) {
		Matcher matcher = VERSION_PATTERN.matcher(version);
		if (!matcher.find()) throw new InvalidUserDataException("Invalid Bukkit API version. Expected: '1.X' or '1.X.Y-pre1");

		String major     = matcher.group(1);
		String minor     = matcher.group(2);
		String rev       = matcher.group(3);
		String qualifier = matcher.group(4);

		if (rev == null)       rev       = "";
		if (qualifier == null) qualifier = "";

		if (qualifier.endsWith("-SNAPSHOT")) qualifier = qualifier.substring(0, qualifier.length() - 9);
		if (qualifier.isEmpty()) qualifier = "-R0.1";

		this.libraryVersion = String.format("%s.%s%s%s%s-SNAPSHOT", major, minor, (rev.isEmpty() ? "" : "."), rev, qualifier);
		this.manifestVersion = String.format("%s.%s", major, minor);
	}


}
