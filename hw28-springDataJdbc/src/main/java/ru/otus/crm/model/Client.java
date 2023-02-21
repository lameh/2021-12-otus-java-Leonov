package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Set;

@Table("client")
public class Client implements Persistable<String> {
    @Id
    @Nonnull
    private String id;
    @Nonnull
    private String name;
    private Address address;
    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;
    @Transient
    private boolean isNew;

    public Client() {
        this.id = "cl_" + System.currentTimeMillis();
        this.isNew = true;
    }

    public Client(String id, String name, Address address, Set<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = true;
    }

    @PersistenceConstructor
    public Client(String id, String name, Address address, Set<Phone> phones) {
        this(id, name, address, phones, false);
    }

    public String getId() {
        return id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public String toString() {
        return "Client{id='" + id + "', name='" + name + "', address=" + address +
                ", phones=" + phones + ", isNew=" + isNew + "}";
    }
}
