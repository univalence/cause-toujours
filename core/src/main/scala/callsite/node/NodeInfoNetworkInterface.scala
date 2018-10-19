package callsite.node

import java.net.{Inet4Address, Inet6Address, InetAddress, NetworkInterface}

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
    val interfaces = networkInterface.getInetAddresses.asScala.toList

    val localHost: InetAddress =
      interfaces.find(iNetAddress ⇒ iNetAddress.isSiteLocalAddress && iNetAddress.isInstanceOf[Inet4Address]).get

    val ipv6Address: Option[InetAddress] =
      interfaces.find(iNetAddress ⇒ iNetAddress.isInstanceOf[Inet6Address])

    val ipv6HostAddress =
      ipv6Address.flatMap(i ⇒ Option(i.getHostAddress.substring(0, i.getHostAddress.indexOf('%'))))

    val hardwareAddress =
      Option(networkInterface.getHardwareAddress).flatMap(ha ⇒ Option(ha.map("%02x" format _).mkString(":")))

    NodeInfoNetworkInterface(
      hostName                    = localHost.getHostName,
      canonicalHostName           = localHost.getCanonicalHostName,
      hostAddress                 = localHost.getHostAddress,
      hostIpv6Address             = ipv6HostAddress,
      networkInterfaceName        = networkInterface.getName,
      networkInterfaceDisplayName = networkInterface.getDisplayName,
      index                       = networkInterface.getIndex,
      parent                      = networkInterface.getParent,
      hardwareAddress             = hardwareAddress
    )
  }
}
