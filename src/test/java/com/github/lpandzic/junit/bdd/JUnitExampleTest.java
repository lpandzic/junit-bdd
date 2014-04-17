package com.github.lpandzic.junit.bdd;

import org.junit.Rule;
import org.junit.Test;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lovro Pandzic
 */
public class JUnitExampleTest {

    @Rule
    public Bdd bdd = Bdd.initialized();

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
}
