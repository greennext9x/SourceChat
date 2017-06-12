package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.model.Contact;

oneway interface ISubscriptionListener {

/**
     * Called when:
     *  <ul>
     *  <li> the request a contact has sent to client
     *  </ul>
     *
     * @see org.awesomeapp.messenger.engine.SubscriptionRequestListener#onSubScriptionRequest(Contact from)
     */
    void onSubScriptionChanged(in Contact from, long providerId, long accountId, int subType, int subStatus);


    /**
     * Called when:
     *  <ul>
     *  <li> the request a contact has sent to client
     *  </ul>
     *
     * @see org.awesomeapp.messenger.engine.SubscriptionRequestListener#onSubScriptionRequest(Contact from)
     */
    void onSubScriptionRequest(in Contact from, long providerId, long accountId);

    /**
     * Called when the request is approved by user.
     *
     * @see org.awesomeapp.messenger.engine.SubscriptionRequestListener#onSubscriptionApproved(String contact)
     */
    void onSubscriptionApproved(in Contact from, long providerId, long accountId);

    /**
     * Called when a subscription request is declined.
     *
     * @see org.awesomeapp.messenger.engine.ContactListListener#onSubscriptionDeclined(String contact)
     */
    void onSubscriptionDeclined(in Contact from, long providerId, long accountId);
}
