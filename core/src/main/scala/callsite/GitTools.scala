package callsite

import java.io.{File, FileInputStream}

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{Constants, ObjectInserter, Repository}
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

object GitTools {

  def getGit(file: File): Option[Git] = {
    val repositoryBuilder: FileRepositoryBuilder = new FileRepositoryBuilder()

    if (repositoryBuilder.findGitDir(file).getGitDir != null) {
      val r: Repository = repositoryBuilder.build()
      val git: Git      = Git.open(r.getDirectory)

      Some(git)
    } else {
      None
    }
  }

  def pathInGit(file: File, git: Git): Option[String] = {
    val path = git.getRepository.getWorkTree.toPath

    Some(
      path
        .toRealPath()
        .relativize(file.toPath.toRealPath())
        .toString
    )
  }

  def isTracked(file: File): Boolean =
    (for {
      git  ← getGit(file)
      path ← pathInGit(file, git)
    } yield git.status().addPath(path).call().getUntracked.isEmpty).getOrElse(false)

  def isClean(file: File): Boolean =
    (for {
      git  ← getGit(file)
      path ← pathInGit(file, git)
    } yield git.status().addPath(path).call().isClean).getOrElse(false)

  def lastCommitIdOf(file: File): String =
    getGit(file)
      .flatMap(g ⇒ Option(g.getRepository.resolve(Constants.HEAD)))
      .map(_.getName)
      .getOrElse("")

  def pathToRepoRoot(file: File): String =
    getGit(file)
      .flatMap(git ⇒ pathInGit(file, git))
      .getOrElse(file.getPath)

  def hashObject(file: File): String = {
    val in = new FileInputStream(file)

    try {
      val formatter = new ObjectInserter.Formatter
      val objectId  = formatter.idFor(Constants.OBJ_BLOB, file.length, in)

      objectId.name()
    } finally {
      in.close()
    }
  }

  sealed trait GitStatus
  case object Clean extends GitStatus
  case object Modified extends GitStatus
  case object Untracked extends GitStatus

  def fileStatus(sourceFile: File): GitStatus =
    if (isClean(sourceFile)) Clean
    else if (isTracked(sourceFile)) Modified
    else Untracked

}
