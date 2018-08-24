package callsite

import java.io.File

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{Constants, Repository}
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

  def isClean(file: File): Boolean =
    (for {
      git  ← getGit(file)
      path ← pathInGit(file, git)
    } yield {
      git.status().addPath(path).call().isClean
    }).getOrElse(false)

  def lastCommitIdOf(file: File): String =
    getGit(file)
      .flatMap(g ⇒ Option(g.getRepository.resolve(Constants.HEAD)))
      .map(_.getName)
      .getOrElse("")

  def pathToRepoRoot(file: File): String =
    getGit(file)
      .flatMap(git ⇒ pathInGit(file, git))
      .getOrElse(file.getPath)

}
