package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IImConnection;
import org.awesomeapp.messenger.service.IConnectionCreationListener;
import org.awesomeapp.messenger.crypto.IOtrKeyManager;

interface IRemoteImService {

    /**
     * Gets a list of all installed plug-ins. Each item is an ImPluginInfo.
     */
    List getAllPlugins();

    /**
     * Register a listener on the service so that the client can be notified when
     * there is a connection be created.
     */
    void addConnectionCreatedListener(IConnectionCreationListener listener);

    /**
     * Unregister the listener on the service so that the client doesn't ware of
     * the connection creation anymore.
     */
    void removeConnectionCreatedListener(IConnectionCreationListener listener);

    /**
     * Create a connection for the given provider.
     */
    IImConnection createConnection(long providerId, long accountId);

    /**
     * Create a connection for the given provider.
     */
    IImConnection getConnection(long providerId);

    /**
     * Get all the active connections.
     */
    List getActiveConnections();

    /**
     * Dismiss all notifications for an IM provider.
     */
    void dismissNotifications(long providerId);

    /**
     * Dismiss notification for the specified chat.
     */
    void dismissChatNotification(long providerId, String username);
    
    
    /**
    * do it
    */
    boolean unlockOtrStore (String password);
    
    /**
    * cleaning up rpocess
    **/
    void setKillProcessOnStop (boolean killProcess);
    
    /**
    * get interface to keymanager/store singleton
    **/
    IOtrKeyManager getOtrKeyManager  ();
    
    /**
    * use debug log to logcat out
    **/
    void enableDebugLogging (boolean debugOn);
    
    /**
    * update settings from OTR
    **/
    void updateStateFromSettings ();

    /**
    * do as the name says!
    **/
    void shutdownAndLock ();
}
