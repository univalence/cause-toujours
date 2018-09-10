package callsite

import java.net.{Inet4Address, Inet6Address, NetworkInterface}
import scala.collection.JavaConverters._

case class NodeInfoNetworkInterface(
    hostName: String,
    canonicalHostName: String,
    hostAddress: String,
    hostIpv6Address: Option[String],
    networkInterfaceName: String,
    networkInterfaceDisplayName: String,
    index: Int,
    parent: NetworkInterface,
    hardwareAddress: Option[String]
)

object NodeInfoNetworkInterface {
  def fromNetworkInterface(networkInterface: NetworkInterface): NodeInfoNetworkInterface = {
    val localHost = networkInterface.getInetAddresses.asScala
      .filter(iNetAddress ⇒ iNetAddress.isSiteLocalAddress && iNetAddress.isInstanceOf[Inet4Address])
      .next()
    val ipv6Address = networkInterface.getInetAddresses.asScala
      .filter(iNetAddress ⇒ iNetAddress.isInstanceOf[Inet6Address])
      .toSeq
      .headOption
    val ipv6HostAddress = ipv6Address.flatMap(i ⇒ Option(i.getHostAddress.substring(0, i.getHostAddress.indexOf('%'))))
    val macAddress =
      Option(networkInterface.getHardwareAddress).flatMap(ha ⇒ Option(ha.map("%02x" format _).mkString(":")))

    NodeInfoNetworkInterface(
      localHost.getHostName,
      localHost.getCanonicalHostName,
      localHost.getHostAddress,
      ipv6HostAddress,
      networkInterface.getName,
      networkInterface.getDisplayName,
      networkInterface.getIndex,
      networkInterface.getParent,
      macAddress
    )
  }
}
