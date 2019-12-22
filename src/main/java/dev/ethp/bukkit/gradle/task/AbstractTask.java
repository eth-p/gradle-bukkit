package dev.ethp.bukkit.gradle.task;

import java.util.Optional;

import groovy.lang.Closure;

import dev.ethp.bukkit.gradle.extension.BukkitExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.logging.text.StyledTextOutput;
import org.gradle.internal.logging.text.StyledTextOutputFactory;

/**
 * An abstract class that automatically fetches the {@link BukkitExtension} object from the project.
 * This also has a couple of output convenience methods.
 */
public abstract class AbstractTask extends DefaultTask {

	// -------------------------------------------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------------------------------------------

	protected BukkitExtension extension;

	// -------------------------------------------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------------------------------------------

	public AbstractTask() {
		getProject().afterEvaluate(new Closure(null) {
			public void doCall() {
				AbstractTask.this.extension = AbstractTask.this.getProject().getExtensions().getByType(BukkitExtension.class);
			}
		});
	}

	// -------------------------------------------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------------------------------------------

	/**
	 * Prints a header.
	 *
	 * @param header The header text.
	 */
	protected void printHeader(String header) {
		StringBuilder underline = new StringBuilder();
		for (int i = header.length(); i > 0; i--) underline.append('-');
		StyledTextOutputFactory styles = this.getServices().get(StyledTextOutputFactory.class);
		styles.create("")
				.style(StyledTextOutput.Style.Header).text(header).text("\n")
				.style(StyledTextOutput.Style.Header).text(underline).println();
	}

	/**
	 * Prints a property.
	 *
	 * @param key   The property key.
	 * @param value The property value.
	 */
	protected void printProperty(String key, Optional<String> value) {
		StyledTextOutputFactory styles = this.getServices().get(StyledTextOutputFactory.class);
		StyledTextOutput output = styles.create("");

		if (key != null) {
			output = output.style(StyledTextOutput.Style.Identifier).text(key).text(": ");
		}

		if (value.isPresent()) {
			output = output.style(StyledTextOutput.Style.Description).text(value.get());
		} else {
			output = output.style(StyledTextOutput.Style.Normal).text("[unset]");
		}

		output.println();
	}

	/**
	 * Prints a list item.
	 *
	 * @param value The item value.
	 */
	protected void printItem(String value) {
		StyledTextOutputFactory styles = this.getServices().get(StyledTextOutputFactory.class);
		StyledTextOutput output = styles.create("")
				.style(StyledTextOutput.Style.Identifier).text(" - ")
				.style(StyledTextOutput.Style.Description).text(value)
				.println();
	}

	/**
	 * Prints a spacer.
	 */
	protected void printSpacer() {
		System.out.println();
	}

	/**
	 * Prints an error message.
	 * @param error The error message.
	 */
	protected void printError(String error) {
		StyledTextOutputFactory styles = this.getServices().get(StyledTextOutputFactory.class);
		styles.create("error").style(StyledTextOutput.Style.Failure).text(error).println();
	}

	/**
	 * Prints an info message.
	 * @param info The info message.
	 */
	protected void printInfo(String info) {
		StyledTextOutputFactory styles = this.getServices().get(StyledTextOutputFactory.class);
		styles.create("error").style(StyledTextOutput.Style.Info).text(info).println();
	}

	// -------------------------------------------------------------------------------------------------------------
	// Abstract
	// -------------------------------------------------------------------------------------------------------------

	@TaskAction
	abstract void exec();

}