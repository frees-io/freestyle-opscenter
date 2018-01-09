pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val root = project
  .in(file("."))
  .settings(name := "microservice-opscenter")
  .settings(moduleName := "root")
  .settings(scalaVersion := "2.12.3")
  .settings(noPublishSettings: _*)
  .settings(scalaMetaSettings: _*)
  .dependsOn(core)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(moduleName := "frees-opscenter")
  .settings(scalaMetaSettings: _*)
  .settings(parallelExecution in Test := false)
  .settings(scalaVersion := "2.12.3")
  .settings(libraryDependencies ++= commonDeps ++ freestyleCoreDeps() ++
    Seq(
      %%("frees-core", "0.5.1"),
      %%("frees-http4s", "0.5.1"),
      %%("frees-config", "0.5.1"),
      %%("frees-logging", "0.5.1"),
      %%("frees-rpc", "0.6.1"),
      %%("cats-effect"),
      %%("http4s-dsl"),
      %%("http4s-blaze-client"),
      %%("http4s-blaze-server"),
      %("joda-time")
    )
  )
  .dependsOn(metrics)
  .aggregate(metrics)

lazy val metrics = project
  .in(file("metrics"))
  .settings(moduleName := "frees-metrics")
  .settings(noPublishSettings: _*)
  .settings(scalaMetaSettings: _*)
  .settings(libraryDependencies ++= commonDeps ++ freestyleCoreDeps() ++
    Seq(
      %%("frees-core", "0.5.1"),
      %%("frees-rpc", "0.6.1"),
      %("joda-time")
    )
  )
