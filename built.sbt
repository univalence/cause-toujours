// -- Main project settings
lazy val causeToujours =
  (project in file("."))
    .settings(commonSettings, publishSettings)
    .settings(
      libraryDependencies ++= Seq(
        "org.scala-lang"   % "scala-reflect"    % scalaVersion.value,
        "org.eclipse.jgit" % "org.eclipse.jgit" % "5.0.2.201807311906-r"
      ),
      // -- Test dependencies
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest"   % "3.0.5",
        "org.slf4j"     % "slf4j-simple" % "1.7.25"
      ).map(_ % Test)
    )

lazy val metadataSettings =
  Def.settings(
    // -- Organization
    organization := "io.univalence",
    organizationName := "Univalence",
    organizationHomepage := Some(url("http://univalence.io/")),
    // -- Project
    name := "cause-toujours",
    version := "0.1.1-SNAPSHOT",
    description := "Cause-toujours is a microframework that gathers information about code location",
    startYear := Some(2018),
    licenses += ("Apache-2.0" → new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/UNIVALENCE/cause-toujours")),
    // -- Contributors
    developers := List(
      Developer(
        id = "jwinandy",
        name = "Jonathan Winandy",
        email = "jonathan@univalence.io",
        url = url("https://github.com/ahoy-jon")
      ),
      Developer(
        id = "phong",
        name = "Philippe Hong",
        email = "philippe@univalence.io",
        url = url("https://github.com/hwki77")
      ),
      Developer(
        id = "fsarradin",
        name = "François Sarradin",
        email = "francois@univalence.io",
        url = url("https://github.com/fsarradin")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/UNIVALENCE/Zoom"),
        "scm:git:https://github.com/UNIVALENCE/Zoom.git",
        "scm:git:git@github.com:UNIVALENCE/Zoom.git"
      ))
  )

lazy val scalaSettings =
  Def.settings(
    crossScalaVersions := Seq("2.11.12", "2.12.6"),
    scalaVersion := crossScalaVersions.value.find(_.startsWith("2.12")).get,
    scalacOptions :=
      Opts.compile.encoding("utf-8") // Specify character encoding used by source files (linked to the previous item).
        ++ Seq(
          Opts.compile.deprecation, // Emit warning and location for usages of deprecated APIs.
          Opts.compile.explaintypes, // Explain type errors in more detail.
          Opts.compile.unchecked, // Enable additional warnings where generated code depends on assumptions.
          "-feature", // Emit warning and location for usages of features that should be imported explicitly.
          "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
          "-language:experimental.macros", // Allow macro definition (besides implementation and application)
          "-language:higherKinds", // Allow higher-kinded types
          "-language:implicitConversions", // Allow definition of implicit functions called views
          "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
          "-Xfatal-warnings", // Fail the compilation if there are any warnings.
          "-Xfuture", // Turn on future language features.
          "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
          "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
          "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
          "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
          "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
          "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
          "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
          "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
          "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
          "-Xlint:option-implicit", // Option.apply used implicit view.
          "-Xlint:package-object-classes", // Class or object defined in package object.
          "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
          "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
          "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
          "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
          "-Xlint:unsound-match", // Pattern match may not be typesafe.
          "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
          "-Ypartial-unification", // Enable partial unification in type constructor inference
          "-Ywarn-dead-code", // Warn when dead code is identified.
          "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
          "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
          "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
          "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
          "-Ywarn-numeric-widen" // Warn when numerics are widened.
        )
  )

lazy val publishSettings =
  Def.settings(
    // -- Settings meant for deployment on oss.sonatype.org
    publishTo := sonatypePublishTo.value,
    useGpg := true
  )

lazy val commonSettings =
  Def.settings(metadataSettings,
               scalaSettings,
               parallelExecution := false,
               scalafmtOnCompile in ThisBuild := true,
               scalafmtTestOnCompile in ThisBuild := true)
