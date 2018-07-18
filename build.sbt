name := "raml"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.raml" % "raml-parser-2" % "1.0.22",
  "org.freemarker" % "freemarker" % "2.3.28",
  "com.softwaremill.sttp" %% "core" % "1.2.2",
  "com.typesafe" % "config" % "1.3.2",
  "org.json4s" %% "json4s-native" % "3.2.11"
)