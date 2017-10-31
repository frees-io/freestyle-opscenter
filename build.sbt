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
      %%("frees-core"),
      %%("frees-http4s"),
      %%("frees-config"),
      %%("frees-logging"),
      %%("frees-rpc"),
      %%("cats-effect"),
      %%("http4s-dsl"),
      %%("http4s-blaze-client"),
      %%("http4s-blaze-server"),
      %%("pbdirect"),
      %("joda-time")
    )
  )
