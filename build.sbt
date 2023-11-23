name := "FlappyRiscv"
version := "0.1"

scalaVersion := "2.12.18"
val spinalVersion = "1.9.3"

fork := true

libraryDependencies ++= Seq(
  "com.hubspot.jinjava" % "jinjava" % "2.7.0",
  "com.github.spinalhdl" % "spinalhdl-core_2.12" % spinalVersion,
  "com.github.spinalhdl" % "spinalhdl-lib_2.12" % spinalVersion,
  compilerPlugin("com.github.spinalhdl" % "spinalhdl-idsl-plugin_2.12" % spinalVersion)
)
