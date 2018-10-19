package callsite.nodeinfo

import java.net._

import scala.collection.JavaConverters._

case class NodeInfo(
    nodeInfoNetworkInterfaces: List[NodeInfoNetworkInterface],
    nodeInfoOS: NodeInfoOS,
    nodeInfoRuntime: NodeInfoRuntime
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
      nodeInfoNetworkInterfaces = networkInterfaceList,
      nodeInfoOS                = NodeInfoOS.nodeInfoOS,
      nodeInfoRuntime           = NodeInfoRuntime.nodeInfoRuntime
    )
  }

}
