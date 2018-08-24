package callsite

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.scalatest.{FunSuiteLike, Matchers}

class GitToolsTest extends FunSuiteLike with Matchers {

  import GitTools._

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

  test("a new file should not be clean") {
    val file: File = root.resolve("newFile").toFile

    file.createNewFile()

    isClean(file) should be(false)
  }

  test("a committed new file should be clean") {
    val file: File = root.resolve("newFile").toFile

    file.createNewFile()
    git.add().addFilepattern("newFile").call()
    git.commit().setMessage("commit message yolo").call()

    isClean(file) should be(true)
  }

  test("a committed then modified new file should not be clean") {
    val file: File = root.resolve("newFile").toFile

    file.createNewFile()
    git.add().addFilepattern("newFile").call()
    git.commit().setMessage("commit message yolo").call()
    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    isClean(file) should be(false)
  }

  test("should get last commit id from a new committed file") {
    val file: File = root.resolve("newFile").toFile
    file.createNewFile()
    git.add().addFilepattern("newFile")
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    lastCommitIdOf(file) should be(initialCommit.name())
  }

  test(
    "should get last commit id from a committed file even after some modifications on this file"
  ) {
    val file: File = root.resolve("newFile").toFile
    file.createNewFile()
    git.add().addFilepattern("newFile")
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    lastCommitIdOf(file) should be(initialCommit.name())
  }

  test("should get last commit id after committed a file twice") {
    val file: File = root.resolve("newFile").toFile
    file.createNewFile()
    git.add().addFilepattern("newFile")
    val initialCommit: RevCommit =
      git.commit().setMessage("commit message yolo 1").call()

    Files.write(file.toPath, "file contents".getBytes(StandardCharsets.UTF_8))

    val secondCommit =
      git.commit().setAll(true).setMessage("commit message yolo 2").call()

    lastCommitIdOf(file) should be(secondCommit.name())
  }

  test("should get a path to root of the repo from a file") {
    val dir = root.resolve("a/b/c/d").toFile
    dir.mkdirs()

    val file = root.resolve("a/b/c/d/e.txt").toFile
    file.createNewFile()

    pathToRepoRoot(file) should be("a/b/c/d/e.txt")
  }

}
