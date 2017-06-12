package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IChatSession;
import org.awesomeapp.messenger.model.Contact;
import org.awesomeapp.messenger.model.ImErrorInfo;
import org.awesomeapp.messenger.model.Message;

interface IChatListener {
    /**
     * This method is called when a new message of the ChatSession has arrived.
     *
     * response indicates whether the user is focused on this message stream or not (for notifications)
     */
    boolean onIncomingMessage(IChatSession ses, in org.awesomeapp.messenger.model.Message msg);

    /**
     * This method is called when a new message of the ChatSession has arrived.
     */
    void onIncomingData(IChatSession ses, in byte[] data);

    /**
     * This method is called when an error is found to send a message in the ChatSession.
     */
    void onSendMessageError(IChatSession ses, in org.awesomeapp.messenger.model.Message msg, in ImErrorInfo error);

    /**
     * This method is called when the chat is converted to a group chat.
     */
    void onConvertedToGroupChat(IChatSession ses);

    /**
     * This method is called when a new contact has joined into this ChatSession.
     */
    void onContactJoined(IChatSession ses, in Contact contact);

    /**
     * This method is called when a contact in this ChatSession has left.
     */
    void onContactLeft(IChatSession ses, in Contact contact);

    /**
     * This method is called when an error is found to invite a contact to join
     * this ChatSession.
     */
    void onInviteError(IChatSession ses, in ImErrorInfo error);

    /**
     * This method is called when a new receipt has arrived.
     */
    void onIncomingReceipt(IChatSession ses, in String packetId);

	/** This method is called when OTR status changes */
	void onStatusChanged(IChatSession ses);
	
	/** this is called when there is a incoming file transfer request **/
	void onIncomingFileTransfer (String from, String file);
	
	
	/** this is called when there is a incoming file transfer request **/
	void onIncomingFileTransferProgress (String file, int percent);
	
	/** this is called when there is a incoming file transfer request **/
	void onIncomingFileTransferError (String file, String message);

    /** this is called when the contact is typing on the other end **/
	void onContactTyping (IChatSession ses, in Contact contact, in boolean isActive);
	
}
