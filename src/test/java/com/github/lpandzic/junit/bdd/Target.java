package com.github.lpandzic.junit.bdd;

import static java.util.Objects.requireNonNull;

/**
 * @author Lovro Pandzic
 */
class Target {

    public static Target ofName(String name) {

        return new Target(name);

    }

    private final String name;
    private boolean destroyed;

    public boolean isDestroyed() {

        return destroyed;
    }

    public void destroy() {

        this.destroyed = true;
    }

    @Override
    public int hashCode() {

        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Target target = (Target) o;

        if (!name.equals(target.name)) {
            return false;
        }

        return true;
    }

    private Target(String name) {

        this.name = requireNonNull(name);
    }

    @Override
    public String toString() {

        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }
}
