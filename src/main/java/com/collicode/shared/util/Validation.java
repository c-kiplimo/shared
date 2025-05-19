package com.collicode.shared.util;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.collicode.shared.util.Result.ValidationResult.invalid;
import static com.collicode.shared.util.Result.ValidationResult.valid;


public interface Validation<E> extends Function<E, Result.ValidationResult> {


    static <E> Validation<E> hold(Predicate<E> p, String message) {
        return user -> p.test(user) ? valid() : invalid(message);
    }

    static <E> Validation<E> check(Predicate<E> p, String message) {
        return (record) -> {
            try {
                return p.test(record) ? valid() : invalid(message);
            } catch (Exception exception) {
                return invalid(exception.getMessage());
            }
        };
    }

    static <E> Validation<E> isIsTrue(Predicate<E> p, String message) {
        return (record) -> {
            try {
                return p.test(record) ? invalid(message) : valid();
            } catch (Exception exception) {
                return invalid(exception.getMessage());
            }
        };
    }

    static <E> Validation<E> isIsFalse(Predicate<E> p, String message) {
        return (record) -> {
            try {
                return p.test(record) ? valid() : invalid(message);
            } catch (Exception exception) {
                return invalid(exception.getMessage());
            }
        };
    }

    static <E> Validation<E> get() {
        return (record) -> {
            return valid();
        };
    }

    static <E> Result.ValidationResult get(Predicate<E> p, String message, E record) {
        return p.test(record) ? valid() : invalid(message);
    }

    static <E> Result.ValidationResult get(Predicate<E> p, Supplier<String> message, E record) {
        return p.test(record) ? valid() : invalid(message.get());
    }

    default Validation<E> and(Validation<E> other) {
        return (record) -> {
            Result.ValidationResult result = this.apply(record);
            return result.isValid() ? other.apply(record) : result;
        };
    }

    default Validation<E> or(Validation<E> validationRecord) {
        return (record) -> {
            Result.ValidationResult result = this.apply(record);
            return result.isValid() ? result : validationRecord.apply(record);
        };
    }

    default <E> Validation<E> all(Validation<E>... other) {

        return (record) -> {
            List<String> reasons = Arrays.stream(other)
                    .map((validationRecord) -> {
                        try {
                            return validationRecord.apply(record);
                        } catch (Exception var3) {

                            return invalid(var3.getMessage());
                        }
                    })
                    .filter((valid) -> !valid.isValid() && valid.getReason().isPresent())
                    .map((invalid) -> {
                        return invalid.getReason().get();
                    })
                    .toList();
            if (reasons.isEmpty()) {
                return valid();
            } else {
                Result.ValidationResult result = null;
                Iterator<String> iterator = reasons.iterator();

                while (iterator.hasNext()) {
                    String reason = iterator.next();
                    if (result == null) {
                        result = invalid(reason);
                    } else {
                        result.addReason(reason);
                    }
                }
                return result;
            }
        };
    }
}
