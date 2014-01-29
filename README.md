#JUnit-BDD

[![Build Status](https://lpandzic.ci.cloudbees.com/buildStatus/icon?job=junit-bdd)](https://lpandzic.ci.cloudbees.com/job/junit-bdd/)

JUnit-BDD provides a JUnit test rule which enables Behavior-driven development (BDD) style testing. 

For more information on the subject see the following: [Introducing BDD](http://dannorth.net/introducing-bdd/), [GivenWhenThen article by M. Fowler](http://martinfowler.com/bliki/GivenWhenThen.html), [Wikipedia article](http://en.wikipedia.org/wiki/Behavior-driven_development).

##Features

- Bdd style testing of thrown exception:

```java
public class TestClassExample {

   @UnderTest
   private ClassUnderTest classUnderTest = new ClassUnderTest(...);

   @Rule
   public Bdd bdd = Bdd.initialized(this);

   @Test
   public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

       when(classUnderTest).methodThatThrowsException();

       then(classUnderTest).shouldThrow(Exception.class);
   }
}
```

- Bdd style testing of return value:

```java 
public class TestClassExample {

   @UnderTest
   private ClassUnderTest classUnderTest = new ClassUnderTest(...);

   @Rule
   public Bdd bdd = Bdd.initialized(this);

   @Test
   public void shouldPassWhenExpectedExceptionIsThrown() throws Exception {

       when(classUnderTest).returnsSomeValue();

	   then(classUnderTest).shouldReturn(expectedValue);
   }
}
```
To enable more readable bdd syntax with `when` and `then` syntax use static import 
`import static com.github.lpandzic.junit.bdd.BddJunit.*;`.

For more information about usage see the javadoc in the `Bdd` and `BddJunit` classes. 

## Installation

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
	<version>0.1-SNAPSHOT</version>
</dependency>
```

## Changes

See the [Changelog](https://github.com/lpandzic/junit-bdd/blob/master/CHANGELOG.md)

## Credits

Continuous Integration provided by:

[![Powered by CloudBees](http://www.cloudbees.com/sites/default/files/Button-Powered-by-CB.png)](http://www.cloudbees.com/sites/default/files/Button-Powered-by-CB.png)