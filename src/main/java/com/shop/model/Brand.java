package com.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand extends IDBased implements Serializable {
    private static final long serialVersionUID= 1L;
    @NotNull
    @Column(nullable = false,unique = true)
    private String name;
    private String logo;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = {@JoinColumn(name = "brand_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id",referencedColumnName = "id")}
    )
    private Set<Category>categories=new HashSet<>();

    public Brand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                '}';
    }
}
