package co.thefabulous.search.util;


import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * @param <T>
 * @param <S>
 */
public class ImmutablePair<T, S> implements Serializable {
    private static final long serialVersionUID = 40;

    public final T first;
    public final S second;

    public ImmutablePair() {
        first = null;
        second = null;
    }

    public ImmutablePair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof ImmutablePair == false) {
            return false;
        }

        Object object1 = ((ImmutablePair<?, ?>) object).first;
        Object object2 = ((ImmutablePair<?, ?>) object).second;

        return first.equals(object1) && second.equals(object2);
    }

    @Override
    public int hashCode() {
        return first.hashCode() << 16 + second.hashCode();
    }

    public static <T, S> ImmutablePair<T, S> create(T first, S second) {
        return new ImmutablePair<T, S>(first, second);
    }
}

