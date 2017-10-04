pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

lazy val root = project
  .in(file("."))
  .settings(name := "example")
  .settings(moduleName := "root")
  .settings(noPublishSettings: _*)
  .settings(scalaMetaSettings: _*)
  .settings(libraryDependencies ++= commonDeps ++ freestyleCoreDeps())
  .dependsOn(core)
  .aggregate(core)

val http4sVersion = "0.17.2"
val freesVersion = "0.3.1"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val core = project
  .in(file("core"))
  .settings(moduleName := "frees-opscenter")
  .settings(scalaMetaSettings: _*)
  .settings(parallelExecution in Test := false)
  .settings(libraryDependencies ++= commonDeps ++ freestyleCoreDeps() ++ Seq(
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "io.frees" %% "freestyle-http-http4s" % freesVersion,
    "io.frees" %% "freestyle-logging" % freesVersion,
    "io.frees" %% "freestyle-config" % freesVersion
  ))
