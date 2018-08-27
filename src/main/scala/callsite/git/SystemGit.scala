package callsite.git

import java.io.File

import scala.language.postfixOps
import scala.util.Try

class SystemGit(val gitExePath: File) extends SourceControlManager {

  import scala.sys.process._

  val silentLogger: ProcessLogger = ProcessLogger(_ => (), _ => ())
  private val gitExe: String = gitExePath.toString

  def getRoot(file: File): Option[File] = {
    val fileDirectory =
      if (file.isDirectory)
        file
      else
        file.toPath.getParent.toFile

    Try {
      new File(Process(s"$gitExe rev-parse --show-toplevel", fileDirectory).!!(silentLogger) trim)
    } toOption
  }

  def getFilePathInGit(root: File, file: File): File = {
    root.toPath.relativize(file.toPath).toFile
  }

  override def isClean(file: File): Boolean =
    getRoot(file)
      .forall { root =>
        val fileGitPath = getFilePathInGit(root, file)
        val result = Process(s"$gitExe ls-files --modified --others $fileGitPath", root).!!(silentLogger) trim

        result.isEmpty
      }

  override def lastCommitIdOf(file: File): String = ???

  override def repoRootPathFrom(file: File): String = ???
}

object SystemGit {

  import scala.sys.process._

  lazy val finder: String =
    if (System.getProperty("os.name").toLowerCase contains "windows")
      "where"
    else
      "which"

  def find: Option[SystemGit] =
    Try {
      s"$finder git".lineStream.head
    }
      .map(exe => new SystemGit(new File(exe)))
      .toOption
}
