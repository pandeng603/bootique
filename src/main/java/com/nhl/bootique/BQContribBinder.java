package com.nhl.bootique;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.nhl.bootique.command.Command;
import com.nhl.bootique.env.EnvironmentProperties;

/**
 * A utility class that helps Bootique itself, integration modules, or custom
 * modules to contribute commands and properties to Bootique runtime.
 * 
 * @since 0.8
 */
public class BQContribBinder {

	public static BQContribBinder binder(Binder binder) {
		return new BQContribBinder(binder);
	}

	private Binder binder;

	BQContribBinder(Binder binder) {
		this.binder = binder;
	}

	MapBinder<String, String> propsBinder() {
		return MapBinder.newMapBinder(binder, String.class, String.class, EnvironmentProperties.class);
	}

	/**
	 * Utility method to contribute environment properties to DI.
	 */
	public void bindProperty(String key, String value) {
		propsBinder().addBinding(key).toInstance(value);
	}

	/**
	 * Utility method to contribute custom commands.
	 */
	@SafeVarargs
	public final void bindCommandTypes(Class<? extends Command>... commands) {
		bindCommandTypes(Arrays.asList(Preconditions.checkNotNull(commands)));
	}

	/**
	 * Utility method to contribute custom commands.
	 */
	public void bindCommandTypes(Collection<Class<? extends Command>> commands) {
		Multibinder<Command> commandBinder = Multibinder.newSetBinder(binder, Command.class);
		Preconditions.checkNotNull(commands).forEach(ct -> commandBinder.addBinding().to(ct));
	}

	/**
	 * Utility method to contribute custom commands.
	 */
	public void bindCommands(Collection<? extends Command> commands) {
		Multibinder<Command> commandBinder = Multibinder.newSetBinder(binder, Command.class);
		Preconditions.checkNotNull(commands).forEach(c -> commandBinder.addBinding().toInstance(c));
	}
}