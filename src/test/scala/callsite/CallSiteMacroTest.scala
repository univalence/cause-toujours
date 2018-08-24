package callsite

import java.io.File

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.scalatest.{FunSuiteLike, Matchers}

class CallSiteMacroTest extends FunSuiteLike with Matchers {

  import CallSiteMacro._
  import Implicit._

  test("should have builtAt lower or equal to the current time") {
    buildAt should be <= System.currentTimeMillis()
  }

  test("should get call site information") {
    val csi: CallSiteInfo = implicitly[CallSiteInfo]
    val git: Git          = GitTools.getGit(new File(".")).get

    csi should have(
      'enclosingClass (getClass.getCanonicalName),
      'commit (git.getRepository.resolve(Constants.HEAD).getName)
    )

    csi.file should endWith(getClass.getSimpleName + ".scala")
    csi.buildAt should be <= buildAt
  }
}
