name := "mf-dabi-aco-ant-colony-optimization-algorithm"

version := "0.1"

scalaVersion := "2.13.5"
//scalaVersion := "2.12.11"
libraryDependencies ++= Seq(
  "org.scalanlp" %% "breeze" % "1.2" withSources(),
  "org.scalanlp" %% "breeze-viz" % "1.2" withSources(),
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"

)