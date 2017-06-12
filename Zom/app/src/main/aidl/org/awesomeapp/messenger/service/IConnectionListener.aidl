package org.awesomeapp.messenger.service;

import org.awesomeapp.messenger.service.IImConnection;
import org.awesomeapp.messenger.model.ImErrorInfo;
import org.awesomeapp.messenger.model.Presence;

oneway interface IConnectionListener {
    void onStateChanged(in IImConnection connection, int state,
            in ImErrorInfo error);

    void onUserPresenceUpdated(in IImConnection connection);

    void onUpdatePresenceError(in IImConnection connection,
            in ImErrorInfo error);
}
