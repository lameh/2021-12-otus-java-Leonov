package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("address")
public class Address {
    @Id
    private String client;
    @Nonnull
    private String street;

    public Address() {
    }

    public Address(String street) {
        this(null, street);
    }

    @PersistenceConstructor
    public Address(String client, @Nonnull String street) {
        this.client = client;
        this.street = street;
    }

    public String getClient() {
        return client;
    }

    @Nonnull
    public String getStreet() {
        return street;
    }

    public String toString() {
        return "Address{client=" + client + ", street='" + street + "'}";
    }
}
