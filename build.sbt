name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"
jacoco.settings


libraryDependencies ++= Seq( jdbc , ehcache , ws , guice,  "com.typesafe.play" %% "anorm" % "2.5.3" ,
  "mysql" % "mysql-connector-java" % "5.1.30", "org.xerial" % "sqlite-jdbc" % "3.7.2", "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test")

