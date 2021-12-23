name := "data-collector"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"
libraryDependencies += "software.amazon.awssdk" % "s3" % "2.17.102"

lazy val app = (project in file("./")).settings(assembly / assemblyJarName := "collect-data.jar")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}
