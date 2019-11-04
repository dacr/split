# split [![License][licenseImg]][licenseLink] [![Build Status][travisImg]][travisLink] [![Codacy][codacyImg]][codacyLink] [![codecov][codecovImg]][codecovLink]

**split** library is a collection of various text based
splitting algorithms designed for high performances

In your build.sbt, add this :

`libraryDependencies += "fr.janalyse" %% "split" % version`

Latest `version`: [![Maven][mavenImg]][mavenLink] [![Scaladex][scaladexImg]][scaladexLink]

For usage example, take a look to unit test cases.

## CsvSplit
To split csv lines into cells using the specified 
separator (`,` by default). It supports content such as
`truc, blah, "bouh, bah"` which contains 3 cells.

## *StringSplit* 
To convert a string into a collection of sub-strings
while taking into account brackets, quotes, ...
   
## *FlowGroup*
To convert a strings flow into a smaller one.
For example to process multi-lines logs
   
## *KeyValSplit*
To extract key values from a string. This is done
in a smart way which allow the separator to be used
inside values.

[mavenImg]: https://img.shields.io/maven-central/v/fr.janalyse/split_2.12.svg
[mavenImg2]: https://maven-badges.herokuapp.com/maven-central/fr.janalyse/split_2.12/badge.svg
[mavenLink]: https://search.maven.org/#search%7Cga%7C1%7Cfr.janalyse.split

[scaladexImg]: https://index.scala-lang.org/dacr/split/split/latest.svg
[scaladexLink]: https://index.scala-lang.org/dacr/split

[licenseImg]: https://img.shields.io/github/license/dacr/split.svg
[licenseImg2]: https://img.shields.io/:license-apache2-blue.svg
[licenseLink]: LICENSE

[codacyImg]: https://img.shields.io/codacy/3f668cc5639b4f04bd85e5c068350aea.svg
[codacyImg2]: https://api.codacy.com/project/badge/grade/3f668cc5639b4f04bd85e5c068350aea
[codacyLink]: https://www.codacy.com/app/dacr/split/dashboard

[codecovImg]: https://img.shields.io/codecov/c/github/dacr/split/master.svg
[codecovImg2]: https://codecov.io/github/dacr/split/coverage.svg?branch=master
[codecovLink]: http://codecov.io/github/dacr/split?branch=master

[travisImg]: https://img.shields.io/travis/dacr/split.svg
[travisImg2]: https://travis-ci.org/dacr/split.png?branch=master
[travisLink]:https://travis-ci.org/dacr/split
