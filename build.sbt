
val project_t = project
  .in(file("."))
  .settings(
  libraryDependencies ++= Seq(
    ws,
    guice),
  name := "test_play_proj",
  version := "0.1",
  scalaVersion := "2.12.7")
  .enablePlugins(PlayScala)
  .disablePlugins(PlayFilters)
