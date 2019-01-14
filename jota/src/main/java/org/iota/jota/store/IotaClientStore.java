package org.iota.jota.store;

import java.io.Serializable;

public abstract class IotaClientStore implements Store {

    protected Store store;
    
    public IotaClientStore(Store store) {
        this.store = store;
        try {
            store.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean canWrite() {
        return store.canWrite();
    }
    
    @Override
    public String toString() {
        return store.toString();
    }

    @Override
    public void load() throws Exception {
        store.load();
    }

    @Override
    public void save() throws Exception {
        store.save();
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return store.get(key);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        return store.get(key, def);
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        return store.set(key, value);
    }
}