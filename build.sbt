name := "FlappyRiscv"
version := "0.1"

scalaVersion := "2.11.12"
val spinalVersion = "1.9.0"

fork := true

libraryDependencies ++= Seq(
  "com.hubspot.jinjava" % "jinjava" % "2.7.0",
  "com.github.spinalhdl" % "spinalhdl-core_2.11" % spinalVersion,
  "com.github.spinalhdl" % "spinalhdl-lib_2.11" % spinalVersion,
  compilerPlugin("com.github.spinalhdl" % "spinalhdl-idsl-plugin_2.11" % spinalVersion)
)
