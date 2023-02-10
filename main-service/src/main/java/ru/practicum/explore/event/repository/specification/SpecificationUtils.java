package ru.practicum.explore.event.repository.specification;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

public class SpecificationUtils {

    public static <T, A> Specification<T> isNotEquals(SingularAttribute<T, A> attribute, A value) {
        return (root, query, builder) ->
                value == null ? builder.conjunction() : builder.notEqual(root.get(attribute), value);
    }

    public static <T, A> Specification<T> isEquals(SingularAttribute<T, A> attribute, A value) {
        return (root, query, builder) ->
                value == null ? builder.conjunction() : builder.equal(root.get(attribute), value);
    }

    public static <T, A, V> Specification<T> isEquals(
            SingularAttribute<T, A> attribute, SingularAttribute<A, V> subAttribute, V value) {
        return (root, query, builder) ->
                value == null
                        ? builder.conjunction()
                        : builder.equal(root.get(attribute).get(subAttribute), value);
    }

    public static <T> Specification<T> contains(
            SingularAttribute<T, String> attribute, String substring) {
        return (root, query, builder) ->
                substring == null
                        ? builder.conjunction()
                        : builder.and(builder.like(root.get(attribute), "%" + substring + "%"));
    }

    public static <T> Specification<T> containsToString(
            SingularAttribute<T, UUID> attribute, String substring) {
        return (root, query, builder) ->
                substring == null
                        ? builder.conjunction()
                        : builder.and(
                        builder.like(root.get(attribute).as(String.class), "%" + substring + "%"));
    }

    public static <T, A extends Comparable<? super A>> Specification<T> lessThan(
            SingularAttribute<T, A> attribute, A value) {
        return (root, query, builder) ->
                value == null ? builder.conjunction() : builder.lessThan(root.get(attribute), value);
    }

    public static <T, A extends Comparable<? super A>> Specification<T> lessThanOrEqualTo(
            SingularAttribute<T, A> attribute, A value) {
        return (root, query, builder) ->
                value == null
                        ? builder.conjunction()
                        : builder.lessThanOrEqualTo(root.get(attribute), value);
    }

    public static <T, A extends Comparable<? super A>> Specification<T> greaterThan(
            SingularAttribute<T, A> attribute, A value) {
        return (Specification<T>)
                (root, query, builder) ->
                        value == null ? builder.conjunction() : builder.greaterThan(root.get(attribute), value);
    }

    public static <T, A extends Comparable<? super A>> Specification<T> greaterThanOrEqualTo(
            SingularAttribute<T, A> attribute, A value) {
        return (Specification<T>)
                (root, query, builder) ->
                        value == null
                                ? builder.conjunction()
                                : builder.greaterThanOrEqualTo(root.get(attribute), value);
    }

    public static <T, A> Specification<T> in(SingularAttribute<T, A> attribute, List<A> value) {
        return (root, query, builder) ->
                value == null ? builder.conjunction() : root.get(attribute).in(value);
    }

    public static <T, A> Specification<T> isNotNull(SingularAttribute<T, A> attribute) {
        return (root, query, builder) -> builder.isNotNull(root.get(attribute));
    }

    public static <T> Specification<T> disjunction() {
        return (root, query, builder) -> builder.disjunction();
    }

    public static <T> Specification<T> conjunction() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static <T> Specification<T> and(Collection<Specification<T>> specifications) {
        return CollectionUtils.isEmpty(specifications)
                ? conjunction()
                : specifications.stream().reduce(Specification::and).orElse(conjunction());
    }

    public static <T> Specification<T> or(Collection<Specification<T>> specifications) {
        return CollectionUtils.isEmpty(specifications)
                ? conjunction()
                : specifications.stream().reduce(Specification::or).orElse(conjunction());
    }
}
