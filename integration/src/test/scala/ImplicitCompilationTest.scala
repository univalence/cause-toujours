//NO IMPORT

object ImplicitCompilationTest {

  def testCallSite(implicit cs: callsite.CallSiteInfo): Unit = {}

  testCallSite

  def testPosition(implicit pos: org.scalactic.source.Position): Unit = {}

  testPosition
}

object ValueAssignmentCompilationTest {

  val x = callsite.CallSiteInfo.callSiteInfo

}
