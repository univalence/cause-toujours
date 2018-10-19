package callsite.util

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{FileSystems, Files, Path}
import java.util.UUID

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.scalatest.{FunSuiteLike, Matchers}

class GitToolsTest extends FunSuiteLike with Matchers {

  import callsite.util.GitTools._

  val root: Path = Files.createTempDirectory("testGit")
  val git: Git   = Git.init().setDirectory(root.toFile).call()

  test("should not get git for unmanaged directory") {
    val unmanagedRoot: Path = Files.createTempDirectory("testGit")

    val maybeGit = getGit(unmanagedRoot.toFile)

    maybeGit should not be defined
  }

  test("should get git for managed directory") {
    val maybeGit = getGit(root.toFile)

    maybeGit should be(defined)
  }

  test("a new file should be untracked") {
    val file: File = root.resolve(newFileName).toFile

    file.createNewFile()

    fileStatus(file) should be(Untracked)
  }

  test("a committed new file should be clean") {
    val fileName   = newFileName
    val file: File = root.resolve(fileName).toFile

    file.createNewFile()
    git.add().addFilepattern(fileName).call()
    git.commit().setMessage("commit message yolo").call()

    fileStatus(file) should be(Clean)
  }

  test("a committed new file then modified should be modified") {
    val fileName   = newFileName
    val file: File = root.resolve(fileName).toFile

    file.createNewFile()
    git.add().addFilepattern(fileName).call()
    git.commit().setMessage("commit message yolo").call()
    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    fileStatus(file) should be(Modified)
  }

  test("should get last commit id from a new committed file") {
    val fileName   = newFileName
    val file: File = root.resolve(fileName).toFile
    file.createNewFile()
    git.add().addFilepattern(fileName)
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    lastCommitIdOf(file) should be(initialCommit.name())
  }

  test(
    "should get last commit id from a committed file even after some modifications on this file"
  ) {
    val fileName   = newFileName
    val file: File = root.resolve(fileName).toFile
    file.createNewFile()
    git.add().addFilepattern(fileName)
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    lastCommitIdOf(file) should be(initialCommit.name())
  }

  test("should get last commit id after committed a file twice") {
    val fileName   = newFileName
    val file: File = root.resolve(fileName).toFile
    file.createNewFile()
    git.add().addFilepattern(fileName)
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    val secondCommit =
      git.commit().setAll(true).setMessage("commit message yolo 2").call()

    lastCommitIdOf(file) should be(secondCommit.name())
  }

  test("should get a path to root of the repo from a file") {
    val subPath = FileSystems.getDefault.getPath("a", "b", "c", "d")
    val dir     = root.resolve(subPath)
    dir.toFile.mkdirs()

    val file = dir.resolve("e.txt").toFile
    file.createNewFile()

    pathToRepoRoot(file) should be(FileSystems.getDefault.getPath("a", "b", "c", "d", "e.txt").toString)
  }

  test("should compute hash of a file") {
    val file: File = root.resolve(newFileName).toFile

    file.createNewFile()

    hashObject(file) should be("e69de29bb2d1d6434b8b29ae775ad8c2e48c5391")
  }

  def newFileName: String = "NewFile-" + UUID.randomUUID().toString

}
