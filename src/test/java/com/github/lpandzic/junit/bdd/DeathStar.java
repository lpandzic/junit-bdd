package com.github.lpandzic.junit.bdd;

/**
 * @author Lovro Pandzic
 */
class DeathStar {

    Target fireAt(Target target) throws TargetAlreadyDestroyedException {

        if (target.isDestroyed()) {
            throw new TargetAlreadyDestroyedException("Cannot fire at a destroyed " + target);
        }

        target.destroy();

        return target;
    }
}
