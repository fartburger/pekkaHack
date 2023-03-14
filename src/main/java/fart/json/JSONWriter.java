package fart.json;

import fart.json.JSONArray;
import fart.json.JSONException;
import fart.json.JSONObject;
import fart.json.JSONString;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class JSONWriter {
    private static final int maxdepth = 200;

    /**
     * The comma flag determines if a comma should be output before the next
     * value.
     */
    private boolean comma;

    /**
     * The current mode. Values:
     * 'a' (array),
     * 'd' (done),
     * 'i' (initial),
     * 'k' (key),
     * 'o' (object).
     */
    protected char mode;

    /**
     * The object/array stack.
     */
    private final fart.json.JSONObject stack[];

    /**
     * The stack top index. A value of 0 indicates that the stack is empty.
     */
    private int top;

    /**
     * The writer that will receive the output.
     */
    protected Appendable writer;

    /**
     * Make a fresh JSONWriter. It can be used to build one JSON text.
     * @param w an appendable object
     */
    public JSONWriter(Appendable w) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new fart.json.JSONObject[maxdepth];
        this.top = 0;
        this.writer = w;
    }

    /**
     * Append a value.
     * @param string A string value.
     * @return this
     * @throws fart.json.JSONException If the value is out of sequence.
     */
    private fart.json.JSONWriter append(String string) throws fart.json.JSONException {
        if (string == null) {
            throw new fart.json.JSONException("Null pointer");
        }
        if (this.mode == 'o' || this.mode == 'a') {
            try {
                if (this.comma && this.mode == 'a') {
                    this.writer.append(',');
                }
                this.writer.append(string);
            } catch (IOException e) {
                // Android as of API 25 does not support this exception constructor
                // however we won't worry about it. If an exception is happening here
                // it will just throw a "Method not found" exception instead.
                throw new fart.json.JSONException(e);
            }
            if (this.mode == 'o') {
                this.mode = 'k';
            }
            this.comma = true;
            return this;
        }
        throw new fart.json.JSONException("Value out of sequence.");
    }

    /**
     * Begin appending a new array. All values until the balancing
     * <code>endArray</code> will be appended to this array. The
     * <code>endArray</code> method must be called to mark the array's end.
     * @return this
     * @throws fart.json.JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public fart.json.JSONWriter array() throws fart.json.JSONException {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            this.push(null);
            this.append("[");
            this.comma = false;
            return this;
        }
        throw new fart.json.JSONException("Misplaced array.");
    }

    /**
     * End something.
     * @param m Mode
     * @param c Closing character
     * @return this
     * @throws fart.json.JSONException If unbalanced.
     */
    private fart.json.JSONWriter end(char m, char c) throws fart.json.JSONException {
        if (this.mode != m) {
            throw new fart.json.JSONException(m == 'a'
                    ? "Misplaced endArray."
                    : "Misplaced endObject.");
        }
        this.pop(m);
        try {
            this.writer.append(c);
        } catch (IOException e) {
            // Android as of API 25 does not support this exception constructor
            // however we won't worry about it. If an exception is happening here
            // it will just throw a "Method not found" exception instead.
            throw new fart.json.JSONException(e);
        }
        this.comma = true;
        return this;
    }

    /**
     * End an array. This method most be called to balance calls to
     * <code>array</code>.
     * @return this
     * @throws fart.json.JSONException If incorrectly nested.
     */
    public fart.json.JSONWriter endArray() throws fart.json.JSONException {
        return this.end('a', ']');
    }

    /**
     * End an object. This method most be called to balance calls to
     * <code>object</code>.
     * @return this
     * @throws fart.json.JSONException If incorrectly nested.
     */
    public fart.json.JSONWriter endObject() throws fart.json.JSONException {
        return this.end('k', '}');
    }

    /**
     * Append a key. The key will be associated with the next value. In an
     * object, every value must be preceded by a key.
     * @param string A key string.
     * @return this
     * @throws fart.json.JSONException If the key is out of place. For example, keys
     *  do not belong in arrays or if the key is null.
     */
    public fart.json.JSONWriter key(String string) throws fart.json.JSONException {
        if (string == null) {
            throw new fart.json.JSONException("Null key.");
        }
        if (this.mode == 'k') {
            try {
                fart.json.JSONObject topObject = this.stack[this.top - 1];
                // don't use the built in putOnce method to maintain Android support
                if(topObject.has(string)) {
                    throw new fart.json.JSONException("Duplicate key \"" + string + "\"");
                }
                topObject.put(string, true);
                if (this.comma) {
                    this.writer.append(',');
                }
                this.writer.append(fart.json.JSONObject.quote(string));
                this.writer.append(':');
                this.comma = false;
                this.mode = 'o';
                return this;
            } catch (IOException e) {
                // Android as of API 25 does not support this exception constructor
                // however we won't worry about it. If an exception is happening here
                // it will just throw a "Method not found" exception instead.
                throw new fart.json.JSONException(e);
            }
        }
        throw new fart.json.JSONException("Misplaced key.");
    }


    /**
     * Begin appending a new object. All keys and values until the balancing
     * <code>endObject</code> will be appended to this object. The
     * <code>endObject</code> method must be called to mark the object's end.
     * @return this
     * @throws fart.json.JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public fart.json.JSONWriter object() throws fart.json.JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        if (this.mode == 'o' || this.mode == 'a') {
            this.append("{");
            this.push(new fart.json.JSONObject());
            this.comma = false;
            return this;
        }
        throw new fart.json.JSONException("Misplaced object.");

    }


    /**
     * Pop an array or object scope.
     * @param c The scope to close.
     * @throws fart.json.JSONException If nesting is wrong.
     */
    private void pop(char c) throws fart.json.JSONException {
        if (this.top <= 0) {
            throw new fart.json.JSONException("Nesting error.");
        }
        char m = this.stack[this.top - 1] == null ? 'a' : 'k';
        if (m != c) {
            throw new fart.json.JSONException("Nesting error.");
        }
        this.top -= 1;
        this.mode = this.top == 0
                ? 'd'
                : this.stack[this.top - 1] == null
                ? 'a'
                : 'k';
    }

    /**
     * Push an array or object scope.
     * @param jo The scope to open.
     * @throws fart.json.JSONException If nesting is too deep.
     */
    private void push(fart.json.JSONObject jo) throws fart.json.JSONException {
        if (this.top >= maxdepth) {
            throw new fart.json.JSONException("Nesting too deep.");
        }
        this.stack[this.top] = jo;
        this.mode = jo == null ? 'a' : 'k';
        this.top += 1;
    }

    /**
     * Make a JSON text of an Object value. If the object has an
     * value.toJSONString() method, then that method will be used to produce the
     * JSON text. The method is required to produce a strictly conforming text.
     * If the object does not contain a toJSONString method (which is the most
     * common case), then a text will be produced by other means. If the value
     * is an array or Collection, then a JSONArray will be made from it and its
     * toJSONString method will be called. If the value is a MAP, then a
     * JSONObject will be made from it and its toJSONString method will be
     * called. Otherwise, the value's toString method will be called, and the
     * result will be quoted.
     *
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value
     *            The value to be serialized.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     * @throws fart.json.JSONException
     *             If the value is or contains an invalid number.
     */
    public static String valueToString(Object value) throws fart.json.JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof JSONString) {
            String object;
            try {
                object = ((JSONString) value).toJSONString();
            } catch (Exception e) {
                throw new fart.json.JSONException(e);
            }
            if (object != null) {
                return object;
            }
            throw new fart.json.JSONException("Bad value from toJSONString: " + object);
        }
        if (value instanceof Number) {
            // not all Numbers may match actual JSON Numbers. i.e. Fractions or Complex
            final String numberAsString = fart.json.JSONObject.numberToString((Number) value);
            if(fart.json.JSONObject.NUMBER_PATTERN.matcher(numberAsString).matches()) {
                // Close enough to a JSON number that we will return it unquoted
                return numberAsString;
            }
            // The Number value is not a valid JSON number.
            // Instead we will quote it as a string
            return fart.json.JSONObject.quote(numberAsString);
        }
        if (value instanceof Boolean || value instanceof fart.json.JSONObject
                || value instanceof fart.json.JSONArray) {
            return value.toString();
        }
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return new fart.json.JSONObject(map).toString();
        }
        if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            return new fart.json.JSONArray(coll).toString();
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString();
        }
        if(value instanceof Enum<?>){
            return fart.json.JSONObject.quote(((Enum<?>)value).name());
        }
        return JSONObject.quote(value.toString());
    }

    /**
     * Append either the value <code>true</code> or the value
     * <code>false</code>.
     * @param b A boolean.
     * @return this
     * @throws fart.json.JSONException if a called function has an error
     */
    public fart.json.JSONWriter value(boolean b) throws fart.json.JSONException {
        return this.append(b ? "true" : "false");
    }

    /**
     * Append a double value.
     * @param d A double.
     * @return this
     * @throws fart.json.JSONException If the number is not finite.
     */
    public fart.json.JSONWriter value(double d) throws fart.json.JSONException {
        return this.value(Double.valueOf(d));
    }

    /**
     * Append a long value.
     * @param l A long.
     * @return this
     * @throws fart.json.JSONException if a called function has an error
     */
    public fart.json.JSONWriter value(long l) throws fart.json.JSONException {
        return this.append(Long.toString(l));
    }


    /**
     * Append an object value.
     * @param object The object to append. It can be null, or a Boolean, Number,
     *   String, JSONObject, or JSONArray, or an object that implements JSONString.
     * @return this
     * @throws fart.json.JSONException If the value is out of sequence.
     */
    public fart.json.JSONWriter value(Object object) throws JSONException {
        return this.append(valueToString(object));
    }
}
