package com.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NamedEntityGraphs(
        {
                @NamedEntityGraph(name = "com.shop.category", attributeNodes = {@NamedAttributeNode(value = "children"), @NamedAttributeNode(value = "parent")})
        }
)
public class Category extends IDBased implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(unique = true)
    private String alias;
    private String image;
    private boolean enabled = true;
    private String allParentId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Category> children = new HashSet<>();

    public Category() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAllParentId() {
        return allParentId;
    }

    public void setAllParentId(String allParentId) {
        this.allParentId = allParentId;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return name;
    }

}
