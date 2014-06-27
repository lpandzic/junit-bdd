## 2.0

  - removed JUnit TestRule from Bdd - it is no longer required (nor possible) to define Bdd as a JUnit TestRule
  - changed JUnit dependency scope to test - JUnit Bdd is now dependency free for users!

## 1.3

  - replaced FEST assert examples with AssertJ in javadoc [#3](https://github.com/lpandzic/junit-bdd/issues/3)

## 1.2

  - improved javadoc on Bdd [#4](https://github.com/lpandzic/junit-bdd/issues/4)

## 1.1

  - fixed a ClassCastException bug in `thenChecked` [#1](https://github.com/lpandzic/junit-bdd/issues/1)

## 1.0

  - project now depends on Java 8 SE
  - added support for catching exceptions by using lambdas in when
  - added support for checked exception assertions
  - removed all dependencies except plain JUnit dependency excluding transitive dependencies
  - removed use of dynamic proxies
  - changed license to the MIT License

## 0.1

  - initial commit