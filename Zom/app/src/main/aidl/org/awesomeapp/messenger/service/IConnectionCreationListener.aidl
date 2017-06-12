package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IImConnection;

oneway interface IConnectionCreationListener {
    /**
     * Called when a connection is created in the service.
     */
    void onConnectionCreated(in IImConnection connection);
}
