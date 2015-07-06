package com.example.user.cloudplayer.storage;


import android.content.res.Resources;

import com.example.user.cloudplayer.transport.NetworkEventListener;

public class CloudStorage {
    private NetworkEventListener listener;
    private Resources resources;

    public void setListener(NetworkEventListener listener) {
        this.listener = listener;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
