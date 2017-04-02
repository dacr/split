# split 

**split** library is a collection of various text based splitting algorithms designed for high performances

In your build.sbt, add this :

`libraryDependencies += "fr.janalyse" %% "split" % version`

Latest `version`: [![Maven][mavenImg]][mavenLink] [![Scaladex][scaladexImg]][scaladexLink]

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