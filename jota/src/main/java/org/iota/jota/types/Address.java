package org.iota.jota.types;

public class Address {
    
    Hash address;
    
    long index;
    
    int securityLevel;

    /**
     * 
     * @param address
     * @param index
     * @param securityLevel
     */
    public Address(Hash address, long index, int securityLevel) {
        this.address = address;
        this.index = index;
        this.securityLevel = securityLevel;
    }

    public Hash getAddress() {
        return address;
    }

    public long getIndex() {
        return index;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public String toString() {
        return "Address [address=" + address + ", index=" + index + ", securityLevel=" + securityLevel + "]";
    }
}