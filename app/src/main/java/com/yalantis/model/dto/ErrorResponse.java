package com.yalantis.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Model for Error object parsed from Response
 */
public final class ErrorResponse {
    @SerializedName("errors")
    private final Errors errors;

    public ErrorResponse(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    /**
     * Error body
     */
    public static final class Errors {
        @SerializedName("base")
        private final Base base[];
        @SerializedName("attributes")
        private final Attributes attributes;

        public Errors(Base[] base, Attributes attributes) {
            this.base = base;
            this.attributes = attributes;
        }

        /**
         * Check if error base or more detailed
         *
         * @return true if base empty or null, false in other case
         */
        public boolean isBaseError() {
            return base != null && base.length > 0;
        }

        /**
         * TODO: need to check response in real work and handle it in proper way
         * Getter for actual error message from response
         *
         * @return only first message from base or first name and first title from attributes
         */
        public String getErrorMessage() {
            return isBaseError()
                    ? base[0].getMessage()
                    : (attributes != null && attributes.isNotEmpty()
                    ? attributes.getErrorNames()[0].getMessage() + "/n" + attributes.getErrorTitles()[0].getMessage()
                    : "An error occurred");
        }

        /**
         * Base Error body
         */
        public static final class Base {
            @SerializedName("message")
            private final String message;
            @SerializedName("code")
            private final long code;

            public Base(String message, long code) {
                this.message = message;
                this.code = code;
            }

            public String getMessage() {
                return message;
            }

            public long getCode() {
                return code;
            }
        }

        /**
         * Detailed Error body
         */
        public static final class Attributes {
            @SerializedName("name")
            private final Name name[];
            @SerializedName("title")
            private final Title title[];

            public Attributes(Name[] name, Title[] title) {
                this.name = name;
                this.title = title;
            }

            /**
             * Check if arrays inside attributes are not empty
             *
             * @return true if not empty and false in other case
             */
            public boolean isNotEmpty() {
                return name != null && name.length > 0
                        && title != null && title.length > 0;
            }

            public Name[] getErrorNames() {
                return name;
            }

            public Title[] getErrorTitles() {
                return title;
            }

            public static final class Name {
                @SerializedName("message")
                private final String message;

                public Name(String message) {
                    this.message = message;
                }

                public String getMessage() {
                    return message;
                }
            }

            public static final class Title {
                @SerializedName("message")
                private final String message;

                public Title(String message) {
                    this.message = message;
                }

                public String getMessage() {
                    return message;
                }
            }
        }
    }
}