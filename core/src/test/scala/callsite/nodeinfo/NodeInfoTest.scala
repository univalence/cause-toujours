package callsite.nodeinfo

import org.scalatest.{FunSuiteLike, Matchers}

class NodeInfoTest extends FunSuiteLike with Matchers {

  test("all host address (ipv4) should be xxx.xxx.xxx.xxx where xxx between [0;255]") {
    val nodeInfo = NodeInfo.nodeInfo

    // IPv4 regex is based on defintion in https://www.ietf.org/rfc/rfc3986.txt
    val result = nodeInfo.nodeInfoNetworkInterfaces
      .map(x ⇒ x.hostAddress.matches("^(\\d+\\.){3}\\d+$"))

    result should contain only true
  }

  test("validate the returned hardware addresses") {
    val nodeInfo = NodeInfo.nodeInfo
    val result = nodeInfo.nodeInfoNetworkInterfaces
      .map(x ⇒ x.hardwareAddress.isDefined)

    result should contain only true
  }

}
