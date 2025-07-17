package com.dev.brito.desafioserasa.specification;

import com.dev.brito.desafioserasa.entity.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class PersonSpecification {
    public static Specification<Person> build(String name, Integer age, String zipCode) {
        Specification<Person> spec = (root, query, builder) -> null;
        spec = spec.and(activeTrue());

        if (StringUtils.hasText(name)) {
            spec = spec.and(nameContains(name));
        }

        if (age != null) {
            spec = spec.and(ageEquals(age));
        }

        if (StringUtils.hasText(zipCode)) {
            spec = spec.and(zipCodeEquals(zipCode));
        }

        return spec;
    }

    private static Specification<Person> activeTrue() {
        return (root, query, builder) -> builder.isTrue(root.get("active"));
    }

    private static Specification<Person> nameContains(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<Person> ageEquals(Integer age) {
        return (root, query, builder) ->
                builder.equal(root.get("age"), age);
    }

    private static Specification<Person> zipCodeEquals(String zipCode) {
        return (root, query, builder) ->
                builder.equal(root.get("zipCode"), zipCode);
    }
}
