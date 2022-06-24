package com.shop.service.dto.category;

import com.shop.model.Category;
import com.shop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.Embedded;
import java.util.*;

@Service
public class CategoryManger {

    @Column(nullable = false)
    @Embedded
    private final CategoryRepository categoryRepository;

    public CategoryManger(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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


    public List<CategoryParentDTO>allTree(){

        return new ArrayList<>(categoryRepository.findAllByParentIdIsNull()
                .stream()
                .map(this::treeCategory)
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                }));
    }

    public List<CategoryParentDTO> treeCategory(Category root) {
        Stack<Category> stack = new Stack<>();
        List<CategoryParentDTO> parentDTOS = new ArrayList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Category visit = stack.pop();
            String name = buidName(visit);
            CategoryParentDTO parentDTO = new CategoryParentDTO(visit, name);
            parentDTOS.add(parentDTO);
            Set<Category> child = visit.getChildren();
            if (child.size() > 0) {
                child.forEach(stack::push);
            }
        }
        return parentDTOS;
    }

    private int highTree(Category from) {
        if (from.getParent() == null) {
            return 0;
        }
        int high = 0;
        while (from.getParent() != null) {
            high++;
            from = from.getParent();
        }
        return high;
    }

    private String buidName(Category from) {
        if (from.getParent() == null) {
            String parentName = from.getName().replace("-", "");
            return "-".concat(parentName);
        }
        StringBuilder sb = new StringBuilder();
        int high = highTree(from);
        String nameChild = from.getName().replace("+", "");
        for (int i = 0; i < high; i++) {
            sb.append("+");
        }
        sb.append(nameChild);
        return sb.toString();
    }

    public static void main(String[] args) {
        UUID.randomUUID().toString();
    }



}
