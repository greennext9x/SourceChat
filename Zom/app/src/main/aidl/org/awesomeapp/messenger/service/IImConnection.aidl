package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IConnectionListener;
import org.awesomeapp.messenger.service.IChatSessionManager;
import org.awesomeapp.messenger.service.IContactListManager;
import org.awesomeapp.messenger.service.IInvitationListener;
import org.awesomeapp.messenger.model.Presence;

interface IImConnection {
    void registerConnectionListener(IConnectionListener listener);
    void unregisterConnectionListener(IConnectionListener listener);

//    void setInvitationListener(IInvitationListener listener);

    IContactListManager getContactListManager();
    IChatSessionManager getChatSessionManager();

    /**
     * Login the IM server.
     *
     * @Param one time password not to be saved, for use if password is not persisted
     * @param autoLoadContacts if true, contacts will be loaded from the server
     *          automatically after the user successfully login; otherwise, the
     *          client must load contacts manually.
     */
    void login(String passwordTempt, boolean autoLoadContacts, boolean retry);
    void logout();
    void cancelLogin();

    Presence getUserPresence();
    int updateUserPresence(in Presence newPresence);

    /**
     * Gets an array of presence status which are supported by the IM provider.
     */
    int[] getSupportedPresenceStatus();

    int getState();

    /**
     * Gets the count of active ChatSessions of this connection.
     */
    int getChatSessionCount();

    long getProviderId();
    long getAccountId();

    /**
     * Whether this connection is going over Tor or not.
     * @return boolean
     */
    boolean isUsingTor();

//    void acceptInvitation(long id);
//    void rejectInvitation(long id);
    void sendHeartbeat();
    
    void setProxy(String type, String host, int port);

    void sendTypingStatus (in String to, boolean isTyping);

    List getFingerprints (String address);
}
