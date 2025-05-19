package com.collicode.shared.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Result {

    public Result() {
    }

    public interface ValidationResult {

        static ValidationResult valid() {
            return ValidationSupport.valid();
        }

        static ValidationResult invalid(String reason) {
            return new Invalid(reason);
        }

        boolean isValid();

        void addReason(String reason);

        Optional<String> getReason();

        Optional<List<String>> getReasons();
    }

    private static final class ValidationSupport {

        private static final ValidationResult valid = new ValidationResult() {
            public boolean isValid() {
                return true;
            }

            public Optional<String> getReason() {
                return Optional.of("");
            }

            public Optional<List<String>> getReasons() {
                return Optional.empty();
            }

            public void addReason(String reason) {
            }
        };

        private ValidationSupport() {
        }

        static ValidationResult valid() {
            return valid;
        }
    }

    private static final class Invalid implements ValidationResult {

        String reason;
        List<String> reasons = null;

        Invalid(String reason) {
            this.reasons = new ArrayList();
            if (reason != null && StringUtils.hasText(reason)) {
                this.reason = reason;
                this.reasons.add(reason);
            }
        }

        public boolean isValid() {
            return false;
        }

        public Optional<String> getReason() {

            return Optional.ofNullable(StringUtils.hasText(this.reason) ? reason : "");
        }

        public Optional<List<String>> getReasons() {
            return Optional.ofNullable(this.reasons);
        }

        public void addReason(String reason) {
            this.reasons.add(reason);
        }
    }
}
