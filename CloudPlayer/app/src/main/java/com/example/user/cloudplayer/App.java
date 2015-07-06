package com.example.user.cloudplayer;

import android.app.Application;

import com.example.user.cloudplayer.storage.CloudStorage;
import com.example.user.cloudplayer.transport.NetworkEventListener;
import com.parse.Parse;
import com.parse.ParseACL;

import java.util.HashSet;

public class App extends Application implements NetworkEventListener {
    private HashSet<NetworkEventListener> listeners;
    private CloudStorage cloudStorage;

    public void addListener(NetworkEventListener networkEventListener) {
        if (!listeners.contains(networkEventListener))
            listeners.add(networkEventListener);
    }

    public void removeListener(NetworkEventListener listener){
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        listeners = new HashSet<>();
        cloudStorage = new CloudStorage();
        cloudStorage.setListener(this);
        Parse.initialize(this, getResources().getString(R.string.application_id),
                getResources().getString(R.string.client_key));
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(acl,true);
        cloudStorage.setResources(getResources());
    }
}
