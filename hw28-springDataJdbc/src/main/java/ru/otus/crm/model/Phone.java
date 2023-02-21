package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("phone")
public class Phone {
    @Id
    private Long id;
    @Nonnull
    private String number;
    @Nonnull
    private String clientId;

    public Phone() {
    }

    public Phone(@Nonnull String number, String clientId) {
        this(null, number, clientId);
    }

    @PersistenceConstructor
    public Phone(Long id, @Nonnull String number, @Nonnull String clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @Nonnull
    public String getNumber() {
        return number;
    }

    @Nonnull
    public String getClientId() {
        return clientId;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return "Phone{id=" + id + ", number='" + number + "'}";
    }
}
