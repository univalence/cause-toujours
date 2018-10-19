import java.io.File

import callsite.util.GitTools
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.scalatest.{FunSuiteLike, Matchers}

class CallSiteMacroIntegrationTest extends FunSuiteLike with Matchers {

  import callsite.CallSiteMacro.buildAt

  test("should have builtAt lower or equal to the current time") {
    buildAt should be <= System.currentTimeMillis()
  }

  test("should get call site information") {
    val csi      = implicitly[callsite.CallSiteInfo]
    val git: Git = GitTools.getGit(new File(".")).get

    csi should have(
      'enclosingClass (getClass.getCanonicalName),
      'commit (git.getRepository.resolve(Constants.HEAD).getName)
    )

    csi.file should endWith(getClass.getSimpleName + ".scala")
    csi.buildAt should be <= buildAt
  }

  test("should get the filename from implicit callsite") {
    def getFilename(implicit csi: callsite.CallSiteInfo): String = csi.file

    getFilename should endWith(getClass.getSimpleName + ".scala")
  }

}
