package callsite

import java.io.File
import java.net.URL

import callsite.git.SystemGit

import scala.language.postfixOps
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader

object Main {

  def main(args: Array[String]): Unit = {
    val filePaths =
      Seq(
        new File("/Users/fsarradin/src/callsite/src/main/scala/callsite/GitTools.scala"),
        new File("/Users/fsarradin/src/callsite/src/main/scala/callsite/Main.scala"),
        new File("/Users/fsarradin/src/callsite/src/main/scala/callsite/CallSiteMacro.scala"),
        new File("/Users/fsarradin/src/callsite/target/.history"),
        new File("/Users/fsarradin/.viminfo")
      )

    for {
      file <- filePaths
    } {
      println(file)
      SystemGit.find.foreach { git =>
        println(s"- isClean: ${git.isClean(file)}")
      }
    }
  }

  def loadJGitJar(path: String): Unit = {
    println(s"jgit jar path: $path")
    val classLoader = new URLClassLoader(Seq(new URL(s"file://$path")), getClass.getClassLoader)
    val cls = Class.forName("org.eclipse.jgit.storage.file.FileRepositoryBuilder", true, classLoader)
    println(cls)
  }

  def isJGitAvailable: Boolean =
    System
      .getProperty("java.class.path")
      .split(File.pathSeparator)
      .exists(_.toLowerCase.contains("jgit"))

}
