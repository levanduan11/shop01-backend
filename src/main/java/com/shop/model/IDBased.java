package com.shop.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass

public abstract class IDBased {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDBased)) return false;
        IDBased idBased = (IDBased) o;
        return Objects.equals(id, idBased.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "IDBased{" +
                "id=" + id +
                '}';
    }

}
