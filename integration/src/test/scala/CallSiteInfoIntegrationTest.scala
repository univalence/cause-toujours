import org.scalatest.{FunSuiteLike, Matchers}

class CallSiteInfoIntegrationTest extends FunSuiteLike with Matchers {

  test("should") {
    import callsite.CallSiteInfo
    import callsite.CallSiteMacro.buildAt

    def getBuildAt(implicit callsite: CallSiteInfo): Long = callsite.buildAt

    getBuildAt should be <= buildAt
  }

}
