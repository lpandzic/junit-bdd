package com.github.lpandzic.junit.bdd;

import org.junit.Rule;
import org.junit.Test;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Lovro Pandzic
 */
public class FestAssertExampleTest {

    @Rule
    public Bdd bdd = Bdd.initialized();

    private Target alderaan = Target.ofName("Alderaan");
    private Target coruscant = Target.ofName("Coruscant");

    @Test
    public void shouldBeAbleToFireAtAlderaan() throws Exception {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan)).then(target -> {
            assertThat(target.isDestroyed()).isTrue();
            assertThat(target).isEqualTo(alderaan);
            assertThat(target).isNotEqualTo(coruscant);
        });
    }

}
