package com.github.lpandzic.junit.bdd;

import org.junit.Test;

import static com.github.lpandzic.junit.bdd.Bdd.when;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lovro Pandzic
 */
public class AssertJExampleTest {

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

    @Test
    public void shouldNotBeAbleToFireAtADestroyedTarget() throws TargetAlreadyDestroyedException {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan));
        when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
            assertThat(thrownException).isExactlyInstanceOf(TargetAlreadyDestroyedException.class);
            assertThat(thrownException.getMessage()).isEqualTo("Cannot fire at a destroyed " + alderaan);
        });
    }

}
