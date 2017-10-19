pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val root = project
  .in(file("."))
  .settings(scalaVersion := "2.12.3")
  .settings(name := "example")
  .settings(moduleName := "root")
  .settings(noPublishSettings: _*)
  .settings(scalaMetaSettings: _*)
  .dependsOn(core)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(scalaVersion := "2.12.3")
  .settings(moduleName := "frees-opscenter")
  .settings(scalaMetaSettings: _*)
  .settings(parallelExecution in Test := false)
  .settings(libraryDependencies ++= commonDeps ++ freestyleCoreDeps() ++
    Seq(
      %%("frees-core"),
      %%("frees-http-http4s"),
      %%("frees-config"),
      %%("frees-logging"),
      %%("cats-effect"),
      %%("http4s-dsl"),
      %%("http4s-blaze-client"),
      %%("http4s-blaze-server"),
      %%("pbdirect")
    )
  )