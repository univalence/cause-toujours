package callsite

import java.io.File

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.scalatest.{FunSuiteLike, Matchers}

class CallSiteMacroTest extends FunSuiteLike with Matchers {

  test("should have builtAt lower or equal to the current time") {
    import CallSiteMacro.buildAt

    buildAt should be <= System.currentTimeMillis()
  }

  test("should get call site information") {
    import CallSiteMacro.buildAt
    import CallSiteInfo.callSite

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
