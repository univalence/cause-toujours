package callsite.implicittest

//NO IMPORT

object ImplicitTest {

  def testCallSite(implicit cs: callsite.CallSiteInfo): Unit = {}

  testCallSite

  def testPosition(implicit pos: org.scalactic.source.Position): Unit = {}

  testPosition
}

object OtherTest {

  val x = callsite.CallSiteInfo.callSiteInfo

}
