package com.github.lpandzic.bdd4j;

import org.junit.Test;

import static com.github.lpandzic.bdd4j.Bdd4j.when;
import static org.junit.Assert.*;

/**
 * @author Lovro Pandzic
 */
public class JUnitExampleTest {

    private Target alderaan = Target.ofName("Alderaan");
    private Target coruscant = Target.ofName("Coruscant");

    @Test
    public void shouldBeAbleToFireAtAlderaan() throws Exception {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan)).then(target -> {
            assertTrue(target.isDestroyed());
            assertEquals(target, alderaan);
            assertNotEquals(target, coruscant);
        });
    }

    @Test
    public void shouldNotBeAbleToFireAtADestroyedTarget() throws TargetAlreadyDestroyedException {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan));
        when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
            assertEquals(TargetAlreadyDestroyedException.class, thrownException.getClass());
            assertEquals("Cannot fire at a destroyed " + alderaan, thrownException.getMessage());
        });
    }
}
