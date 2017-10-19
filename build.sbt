import sbtorgpolicies.OrgPoliciesPlugin.autoImport.%%

pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val root = project
  .in(file("."))
  .settings(name := "example")
  .settings(moduleName := "root")
  .settings(noPublishSettings: _*)
  .dependsOn(core)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(moduleName := "frees-opscenter")
  .settings(scalaMetaSettings: _*)
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),
    PB.protoSources in Compile := Seq(file("core/src/main/protobuf")),
    libraryDependencies ++= Seq(
      "com.trueaccord.scalapb" %%% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion,
      "com.trueaccord.scalapb" %%% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
    )
  )
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
      %%("http4s-blaze-server")
    )
  )

