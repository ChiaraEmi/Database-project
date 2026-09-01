package soundwave.data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to convert entities and fields into readable string representations.
 */
public final class Printer {

    private Printer() { }

    /**
     * Creates a new Field instance with the given name and value.
     *
     * @param name the field name.
     * @param value the field value.
     * @return a new Field instance.
     */
    public static Field field(final String name, final Object value) {
        return new Field(name, value);
    }

    /**
     * Helper function to build a formatted string representation of an object's fields.
     *
     * @param name the entity name.
     * @param fields the list of fields to include.
     * @return a string representation of the object.
     */
    public static String stringify(final String name, final List<Field> fields) {
        final var builder = new StringBuilder(name);
        final var fieldsString = fields.stream().map(Field::toString).collect(Collectors.joining(", "));
        builder.append('[').append(fieldsString).append(']');
        return builder.toString();
    }

    /**
     * Represents a single field key-value pair for string formatting.
     */
    public static final class Field {

        private final String name;
        private final Object value;

        /**
         * Constructs a new Field with a name and value.
         *
         * @param name the name of the field.
         * @param value the value of the field.
         */
        public Field(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Gets the field name.
         *
         * @return the name.
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the field value.
         *
         * @return the value.
         */
        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            if (this.value instanceof String) {
                return this.name + "='" + this.value + "'";
            } else {
                return this.name + "=" + (this.value == null ? "null" : this.value.toString());
            }
        }
    }
}
