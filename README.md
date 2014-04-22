#JUnit-BDD

[![Build Status](https://lpandzic.ci.cloudbees.com/buildStatus/icon?job=junit-bdd)](https://lpandzic.ci.cloudbees.com/job/junit-bdd/)

JUnit-BDD is a JUnit [Rule][1] which provides a simple and fluent API that gives you a way of structuring your test code within when and then blocks used in Behavior-driven development.

This projects depends on Java 8 SE and [JUnit][2] (with transitive dependencies excluded).

For more information on the subject of  Behavior-driven development see the following links: [Introducing BDD][3], [GivenWhenThen article by M. Fowler][4] or [Wikipedia article][5].

##Contents

1. [Features](#Features)
    * [Introduction](#Introduction)
    * [Return value assertion](#ReturnValueAssertion)
    * [Thrown exception assertion](#ThrownExceptionsAssertion)
    * [Checked exceptions assertion](#ThrownCheckedExceptionsAssertion)
    * [Assertion framework flexibility](#AssertionFrameworkFlexibility)
2. [Installation](#Installation)
3. [Changes](#Changes)
4. [License](#License)
5. [Credits](#Credits)

## <a name="Features"></a> Features

### <a name="Introduction"></a>Introduction

Testing with JUnit-BDD starts by defining a JUnit test class that contains following rule definition:

```java
import com.github.lpandzic.junit.bdd.Bdd;
import static com.github.lpandzic.junit.bdd.Bdd.when;

@Rule
public Bdd bdd = Bdd.initialized();
```

Note: in the following examples [Hamcrest][6] is used for assertions but you [are free to use any assertion framework you like](#AssertionFrameworkFlexibility).

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

If we decide to change the `fireAt` method so that it doesn't throw the `TargetAlreadyDestroyedException` the test mentioned in previous sub chapter will fail. Since `TargetAlreadyDestroyedException` is a checked exception we can use Generics to prevent that test from compiling and reduce the time required to detect the error!
We change `then` to `thenChecked` and use `isA` matcher:

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

For example, the first testing example can be translated to:

* [plain JUnit assertions][7]

```java
when(deathStar.fireAt(alderaan)).then(target -> {
    assertTrue(target.isDestroyed());
    assertEquals(target, alderaan);
    assertNotEquals(target, coruscant);
});
```

* [FEST Fluent Assertions][8] 

```java
when(deathStar.fireAt(alderaan)).then(target -> {
    assertThat(target.isDestroyed()).isTrue();
    assertThat(target).isEqualTo(alderaan);
    assertThat(target).isNotEqualTo(coruscant);
});
```

## <a name="Installation"></a> Installation

### Maven

Since the project has not been released to Maven Central repository yet, the following repository definition is needed for the SNAPSHOT dependency:
```
<repositories>
    <repository>
		<id>snapshots-repo</id>
		<url>https://oss.sonatype.org/content/repositories/snapshots</url>
		<releases><enabled>false</enabled></releases>
		<snapshots><enabled>true</enabled></snapshots>
	</repository>
</repositories>
```

Latest dependency:

```
<dependency>
    <groupId>com.github.lpandzic</groupId>
	<artifactId>junit-bdd</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

## <a name="Changes"></a> Changes

See the [Changelog][9]

## <a name="License"></a> License

Licensed under [MIT License][10].

## <a name="Credits"></a> Credits

Continuous Integration provided by:

[![Powered by CloudBees](http://www.cloudbees.com/sites/default/files/Button-Powered-by-CB.png)](http://www.cloudbees.com/sites/default/files/Button-Powered-by-CB.png)

[1]: http://junit-team.github.io/junit/javadoc/4.11/org/junit/Rule.html
[2]: https://github.com/junit-team/junit
[3]: http://dannorth.net/introducing-bdd/
[4]: http://martinfowler.com/bliki/GivenWhenThen.html
[5]: http://en.wikipedia.org/wiki/Behavior-driven_development
[6]: https://github.com/hamcrest/JavaHamcrest
[7]: https://github.com/junit-team/junit/wiki/Assertions
[8]: https://github.com/alexruiz/fest-assert-2.x
[9]: https://github.com/lpandzic/junit-bdd/blob/master/CHANGELOG.md
[10]: https://github.com/lpandzic/junit-bdd/blob/master/LICENSE