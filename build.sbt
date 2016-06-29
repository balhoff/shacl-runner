enablePlugins(JavaAppPackaging)

organization  := "org.rti.bioinformatics"

name          := "shacl-runner"

version       := "0.1"

scalaVersion  := "2.11.8"

mainClass in Compile := Some("org.rti.bioinformatics.RunSHACL")

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions += "-Xmx4G"

resolvers += Resolver.mavenLocal

libraryDependencies ++= {
  Seq(
    "org.topbraid"           %  "shacl"               % "0.0.1-SNAPSHOT"
  )
}
