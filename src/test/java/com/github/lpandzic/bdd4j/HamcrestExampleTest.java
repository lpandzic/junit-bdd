package com.github.lpandzic.bdd4j;

import org.junit.Test;

import static com.github.lpandzic.bdd4j.Bdd4j.when;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Lovro Pandzic
 */
public class HamcrestExampleTest {

    private Target alderaan = Target.ofName("Alderaan");
    private Target coruscant = Target.ofName("Coruscant");

    @Test
    public void shouldBeAbleToFireAtAlderaan() throws Exception {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan)).then(target -> {
            assertThat(target.isDestroyed(), is(true));
            assertThat(target, is(alderaan));
            assertThat(target, is(not(coruscant)));
        });
    }

    @Test
    public void shouldNotBeAbleToFireAtADestroyedTarget() throws TargetAlreadyDestroyedException {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan));
        when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
            assertThat(thrownException, is(instanceOf(TargetAlreadyDestroyedException.class)));
            assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
        });
    }

    @Test
    public void checkedShouldNotBeAbleToFireAtADestroyedTarget() throws TargetAlreadyDestroyedException {

        DeathStar deathStar = new DeathStar();

        when(deathStar.fireAt(alderaan));
        when(() -> deathStar.fireAt(alderaan)).thenChecked(thrownException -> {
            assertThat(thrownException, isA(TargetAlreadyDestroyedException.class));
            assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
        });
    }
}
