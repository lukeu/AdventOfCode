package util;

import java.util.Arrays;
import java.util.Objects;

/**
 * A mutable tuple supporting equals even when the fields are arrays.
 */
public class Pair<A,B> {
    public A a;
    public B b;
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pair<?,?>)) {
            return false;
        }
        var that = (Pair<?,?>) obj;
        return Objects.deepEquals(a, that.a)
                && Objects.deepEquals(b, that.b);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {a, b});
    }
}
