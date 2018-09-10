package callsite

import java.net._
import java.lang.management.ManagementFactory

import scala.collection.JavaConverters._

case class NodeInfo(
    /*hostName: String,
                     canonicalHostName: String,
                     hostAddress: String,
                     hostIpv6Address : Option[String],
                     networkInterfaceName: String,
                     networkInterfaceDisplayName: String,
                     index: Int,
                     parent: NetworkInterface,
                     hardwareAddress: String,*/
    /*processors: Int,
                     osArch: String,
                     osName: String,
                     osVersion: String,*/
    /*runtimeName: String,
                     runtimeInputArgs: String,
                     runtimeUptime: Long,
                     runtimeVMName: String,
                     runtimeVMVendor: String,
                     runtimeVMVersion: String,
                     runtimeSpecName: String,
                     runtimeSpecVendor: String,
                     runtimeSpecVersion: String*/
    nodeInfoNetworkInterfaceList: List[NodeInfoNetworkInterface],
    nodeInfoOS: NodeInfoOS,
    nodeInfoRuntime: NodeInfoRuntime
)

object NodeInfo {
  def nodeInfo: NodeInfo = {
    val networkInterfaceList = NetworkInterface.getNetworkInterfaces.asScala.toList
      .flatMap(_.getInetAddresses.asScala.toList
        .filter(a ⇒ !(a.isLinkLocalAddress || a.isLoopbackAddress)))
      .map(NetworkInterface.getByInetAddress)
      .map(NodeInfoNetworkInterface.fromNetworkInterface)

    NodeInfo(
      networkInterfaceList,
      NodeInfoOS.nodeInfoOS,
      NodeInfoRuntime.nodeInfoRuntime
    )
  }

  def main(args: Array[String]): Unit = {

    val inetAddresses = for {
      interfaces ← NetworkInterface.getNetworkInterfaces.asScala
      addresses  ← interfaces.getInetAddresses.asScala
      if addresses.isSiteLocalAddress
    } yield addresses

    val networkInterfaceSet = NetworkInterface.getNetworkInterfaces.asScala.toList
      .flatMap(_.getInetAddresses.asScala.toList
        .filter(a ⇒ !(a.isLinkLocalAddress || a.isLoopbackAddress)))
      .map(NetworkInterface.getByInetAddress)
      .toSet

    //fromNetworkInterface(networkInterfaceSet.head)

    val nodeInfo = NodeInfo.nodeInfo

    println(nodeInfo.nodeInfoNetworkInterfaceList.head.hostAddress)

    for {
      interfaces ← NetworkInterface.getNetworkInterfaces.asScala
      addresses  ← interfaces.getInetAddresses.asScala
      if !addresses.isLoopbackAddress && addresses.isSiteLocalAddress
    } println(addresses.getHostAddress)

    if (inetAddresses.nonEmpty && inetAddresses.hasNext) {
      val localHost = inetAddresses.next()
      println(localHost)
      println("host name: " + localHost.getHostName)
      println("canonical host name: " + localHost.getCanonicalHostName)
      println("host address: " + localHost.getHostAddress)

      println()

      val networkInterface = NetworkInterface.getByInetAddress(localHost)
      println(networkInterface)

      println("name: " + networkInterface.getName)
      println("display name: " + networkInterface.getDisplayName)
      println("index: " + networkInterface.getIndex)
      println("parent: " + networkInterface.getParent)
      println("hardware address: " + networkInterface.getHardwareAddress.map("%02x" format _).mkString(":"))

      println()

      val os = ManagementFactory.getOperatingSystemMXBean
      println("processors: " + os.getAvailableProcessors)
      println("os arch: " + os.getArch)
      println("os name: " + os.getName)
      println("os version: " + os.getVersion)

      val runtime = ManagementFactory.getRuntimeMXBean
      println("runtime name: " + runtime.getName)
      //println("runtime pid: " + runtime.getPid)
      println("runtime input args: " + runtime.getInputArguments.asScala.toString())
      println("runtime uptime: " + runtime.getUptime)
      println("runtime VM name: " + runtime.getVmName)
      println("runtime VM vendor: " + runtime.getVmVendor)
      println("runtime VM version: " + runtime.getVmVersion)
      println("runtime spec name: " + runtime.getSpecName)
      println("runtime spec vendor: " + runtime.getSpecVendor)
      println("runtime spec version: " + runtime.getSpecVersion)
    }

  }

}
