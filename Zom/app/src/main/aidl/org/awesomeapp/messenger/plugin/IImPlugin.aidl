package org.awesomeapp.messenger.plugin;

interface IImPlugin {
    /**
     * Gets the configuration for the provider. The keys MUST match the values
     * defined in {@link ImConfigNames} and {@link ImpsConfigNames}
     *
     * @return the configuration for the provider.
     */
    Map getProviderConfig();
}
