package com.shop.service.dto;

import com.shop.model.Category;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryManger {


    public CategoryNode getTreeForUseQueue(Category category) {
        CategoryNode node = new CategoryNode(category);
        Queue<Category> categories = new LinkedList<>();
        Set<Category> categories1 = category.getChildren();
        TreeSet<Category> categories2 = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()));
        categories2.addAll(categories1);

        if (categories2.size() > 0) {
            categories2.forEach(x -> categories.add(x));
        }

        Queue<CategoryNode> categoryNodes = new LinkedList<>();
        Set<CategoryNode> categoryNodes1 = node.getChild();
        TreeSet<CategoryNode> categoryNodes2 = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()));
        categoryNodes2.addAll(categoryNodes1);

        if (categoryNodes2.size() > 0) {
            categoryNodes2.forEach(x -> categoryNodes.add(x));
        }

        while (!categories.isEmpty()) {
            Category ca = categories.poll();
            CategoryNode caNode = categoryNodes.poll();
            Set<Category> child = ca.getChildren();

            TreeSet<Category> child2 = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()));
            child2.addAll(child);

            if (child2.size() > 0) {
                Set<CategoryNode> categoryNodes11 = new TreeSet<>((a, b) -> a.getName().compareTo(b.getName()));
                child2.forEach(c -> {
                    CategoryNode categoryNode = new CategoryNode();
                    categoryNode.setName(c.getName());

                    categoryNodes11.add(categoryNode);
                    categories.add(c);
                    categoryNodes.add(categoryNode);

                });
                caNode.setChild(categoryNodes11);
            }

        }
       return node;
    }



}
