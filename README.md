#JUnit-BDD

[![Build Status](https://travis-ci.org/lpandzic/junit-bdd.svg?branch=master)](https://travis-ci.org/lpandzic/junit-bdd)
[![Coverage Status](https://img.shields.io/coveralls/lpandzic/junit-bdd.svg)](https://coveralls.io/r/lpandzic/junit-bdd?branch=coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.lpandzic/junit-bdd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.lpandzic/junit-bdd)

JUnit-BDD provides a simple and fluent API for structuring test code within when and then blocks used in Behavior-driven development.

As of version 2.0, JUnit-BDD depends only on Java 8 SE.

For more information on the subject of  Behavior-driven development see the following links: [Introducing BDD][1], [GivenWhenThen article by M. Fowler][2] or [Wikipedia article][3].

##Contents

1. [News](#News)
2. [Features](#Features)
    * [Introduction](#Introduction)
    * [Return value assertion](#ReturnValueAssertion)
    * [Thrown exception assertion](#ThrownExceptionsAssertion)
    * [Checked exceptions assertion](#ThrownCheckedExceptionsAssertion)
    * [Assertion framework flexibility](#AssertionFrameworkFlexibility)
3. [Installation](#Installation)
4. [Contributing](#Contributing)
5. [License](#License)

## <a name="News"></a> News

### 2.1

New entry point used for static imports is

```java
com.github.lpandzic.bdd4j.Bdd4j
```

All core classes have moved to the new  `com.github.lpandzic.bdd4j` package.
This is a precondition required for [rename of the project](https://github.com/lpandzic/junit-bdd/issues/13).

### 2.0

JUnit dependency has been removed so the following is no longer required nor possible:

```java
 @Rule
 public Bdd bdd = Bdd.initialized();
```
To migrate to 2.0 all you need to do is remove this Rule definition.

For other changes see the [changelog][4]

## <a name="Features"></a> Features

### <a name="Introduction"></a>Introduction

Following static import is useful for simpler syntax when using JUnit-BDD:

```java
 import static com.github.lpandzic.junit.bdd.Bdd.when;
```

Note: in the following examples [Hamcrest][5] is used for assertions but you [are free to use any assertion framework you like](#AssertionFrameworkFlexibility).

### <a name="ReturnValueAssertion"></a>Return value assertion

For a given class `DeathStar` that contains method with signature `Target fireAt(Target target) throws TargetAlreadyDestroyedException` where `TargetAlreadyDestroyedException` is a checked exception, we can do the following value assertion:

```java
when(deathStar.fireAt(alderaan)).then(target -> {
    assertThat(target.isDestroyed(), is(true));
    assertThat(target, is(alderaan));
    assertThat(target, is(not(coruscant)));
});
```

### <a name="ThrownExceptionsAssertion"></a>Thrown exception assertion

In order to catch exception for an assertion we pass a lambda to the when block:

```java
when(deathStar.fireAt(alderaan));
when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
    assertThat(thrownException, is(instanceOf(TargetAlreadyDestroyedException.class)));
    assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
});
```

### <a name="ThrownCheckedExceptionsAssertion"></a>Thrown checked exceptions assertion

If we decide to change the `fireAt` method so that it doesn't throw the `TargetAlreadyDestroyedException` the test mentioned in previous sub chapter will fail, but it will still compile. Since `TargetAlreadyDestroyedException` is a checked exception we can use Generics to prevent that test from compiling and reduce the time required to detect the error!
To use this feature change `then` to `thenChecked` and use `isA` matcher:

```java
when(deathStar.fireAt(alderaan));
when(() -> deathStar.fireAt(alderaan)).thenChecked(thrownException -> {
    assertThat(thrownException, isA(TargetAlreadyDestroyedException.class));
    assertThat(thrownException.getMessage(), is(equalTo("Cannot fire at a destroyed " + alderaan)));
});
```

Now if we decide to change the signature of `fireAt` not to include `TargetAlreadyDestroyedException` we get a compilation error.

### <a name="AssertionFrameworkFlexibility"></a>Assertion framework flexibility

Although Hamcrest was used in previous examples you are free to use any Java assertion framework.

For example, the first two testing examples can be translated to:

* [plain JUnit assertions][6]

    - Return value assertion
    ```java
    when(deathStar.fireAt(alderaan)).then(target -> {
        assertTrue(target.isDestroyed());
        assertEquals(target, alderaan);
        assertNotEquals(target, coruscant);
    });
    ```
    - Thrown exception assertion
    ```java
    when(deathStar.fireAt(alderaan));
    when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
        assertEquals(TargetAlreadyDestroyedException.class, thrownException.getClass());
        assertEquals("Cannot fire at a destroyed " + alderaan, thrownException.getMessage());
    });
    ```

* [AssertJ][7]

    - Return value assertion
    ```java
    when(deathStar.fireAt(alderaan)).then(target -> {
        assertThat(target.isDestroyed()).isTrue();
        assertThat(target).isEqualTo(alderaan);
        assertThat(target).isNotEqualTo(coruscant);
    });
    ```

    - Thrown exception assertion
    ```java
    when(deathStar.fireAt(alderaan));
    when(() -> deathStar.fireAt(alderaan)).then(thrownException -> {
        assertThat(thrownException).isExactlyInstanceOf(TargetAlreadyDestroyedException.class);
        assertThat(thrownException.getMessage()).isEqualTo("Cannot fire at a destroyed " + alderaan);
    });
    ```

## <a name="Installation"></a> Installation

### Maven

```
<dependency>
    <groupId>com.github.lpandzic</groupId>
	<artifactId>junit-bdd</artifactId>
	<version>2.0</version>
	<scope>test</scope>
</dependency>
```

## <a name="Contributing"></a> Contributing

If you have an idea for a new feature or want to report a bug please use the [issue tracker][9].

## <a name="License"></a> License

Licensed under [MIT License][8].

[1]: http://dannorth.net/introducing-bdd/
[2]: http://martinfowler.com/bliki/GivenWhenThen.html
[3]: http://en.wikipedia.org/wiki/Behavior-driven_development
[4]: http://github.com/lpandzic/junit-bdd/blob/master/CHANGELOG.md
[5]: http://github.com/hamcrest/JavaHamcrest
[6]: http://github.com/junit-team/junit/wiki/Assertions
[7]: http://joel-costigliola.github.io/assertj/index.html
[8]: http://github.com/lpandzic/junit-bdd/blob/master/LICENSE
[9]: http://github.com/lpandzic/junit-bdd/issues?state=open