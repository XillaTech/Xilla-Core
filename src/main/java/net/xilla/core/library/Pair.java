package net.xilla.core.library;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Pair<X, Y> {

    @Getter
    @Setter
    public X valueOne;

    @Getter
    @Setter
    public Y valueTwo;

    public Pair(X valueOne, Y valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Pair)) {
            return false;
        } else {
            Pair<?, ?> other = (Pair)obj;
            return Objects.equals(this.valueOne, other.valueOne) && Objects.equals(this.valueTwo, other.valueTwo);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.valueOne) ^ Objects.hashCode(this.valueTwo);
    }

    @Override
    public String toString() {
        return "(" + this.valueOne + ',' + this.valueTwo + ')';
    }

}
