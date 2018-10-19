package callsite.node

import java.net._

import scala.collection.JavaConverters._

case class NodeInfo(
    networkInterfaces: List[NodeInfoNetworkInterface],
    infoOS: NodeInfoOS,
    infoRuntime: NodeInfoRuntime
)

object NodeInfo {

  def nodeInfo: NodeInfo = {
    val networkInterfaceList =
      NetworkInterface.getNetworkInterfaces.asScala.toList
        .flatMap(_.getInetAddresses.asScala.toList
          .filter(a ⇒ !(a.isLinkLocalAddress || a.isLoopbackAddress)))
        .map(NetworkInterface.getByInetAddress)
        .map(NodeInfoNetworkInterface.fromNetworkInterface)

    NodeInfo(
      networkInterfaces = networkInterfaceList,
      infoOS            = NodeInfoOS.nodeInfoOS,
      infoRuntime       = NodeInfoRuntime.nodeInfoRuntime
    )
  }

}
