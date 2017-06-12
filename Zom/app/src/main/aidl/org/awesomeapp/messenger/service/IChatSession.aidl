package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IChatListener;
import org.awesomeapp.messenger.service.IDataListener;
import org.awesomeapp.messenger.model.Message;
import org.awesomeapp.messenger.model.Contact;
import org.awesomeapp.messenger.crypto.IOtrChatSession;



interface IChatSession {
    /**
     * Registers a ChatListener with this ChatSession to listen to incoming
     * message and participant change events.
     */
    void registerChatListener(IChatListener listener);

    /**
     * Unregisters the ChatListener so that it won't be notified again.
     */
    void unregisterChatListener(IChatListener listener);

    /**
     * Tells if this ChatSession is a group session.
     */
    boolean isGroupChatSession();

    /**
     * Gets the name of ChatSession.
     */
    String getName();

    /**
     * Gets the id of the ChatSession in content provider.
     */
    long getId();

    /**
     * Gets the participants of this ChatSession.
     */
    String[] getParticipants();

    /**
     * Convert a single chat to a group chat. If the chat session is already a
     * group chat or it's converting to group chat.
     */
    void convertToGroupChat(String nickname);

    /**
     * Invites a contact to join this ChatSession. The user can only invite
     * contacts to join this ChatSession if it's a group session. Nothing will
     * happen if this is a simple one-to-one ChatSession.
     */
    void inviteContact(String contact);

    /**
     * Leaves this ChatSession.
     */
    void leave();

    /**
     * Leaves this ChatSession if there isn't any message sent or received in it.
     */
    void leaveIfInactive();

    /**
     * Sends a message to all participants in this ChatSession.
     */
    void sendMessage(String text, boolean isResend);

    /**
     * Sends data to all participants in this ChatSession.
     */
    boolean offerData(String offerId, String localUri, String type);

/**
*  Send knock to wake up remote user
**/
//    boolean sendKnock ();

    /**
     * Mark this chat session as read.
     */
    void markAsRead();   

    /**
        * Get OTR Session Manager
        */
    IOtrChatSession getDefaultOtrChatSession();

    /**
    *  notify presence updated
    * */
    void presenceChanged (int newPresence);

     /**
    * set class for handling incoming data transfers
    */
    void setDataListener (IDataListener dataListener);
    
    /**
    * respond to incoming data request
    */
    void setIncomingFileResponse (String transferForm, boolean acceptThis, boolean acceptAll);
    
    /**
    * reinit chatsession if we are starting a new chat
    */
    void reInit();

    /**
         * Sends a ChatSecure-Push Whitelist token to all participants in this ChatSession.
     */
//     boolean sendPushWhitelistToken(String token);

     /**
     * set typing active indicator for contact
     **/
     void setContactTyping (in Contact contact, boolean isTyping);

   /**
     * set typing active indicator for contact
     **/
     void sendTypingStatus (boolean isTyping);

    /**
     * is ready to send and receive encyprted messages
     **/
     boolean isEncrypted ();
}

