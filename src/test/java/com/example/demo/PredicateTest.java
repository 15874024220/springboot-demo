package com.example.demo;

import java.util.Objects;
import java.util.function.Predicate;

public class PredicateTest implements Predicate {
    @Override
    public boolean test(Object o) {
        return false;
    }

    public <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
//                ? Objects::isNull
                ? Objects::isNull
                : targetRef::equals;
    }
}
