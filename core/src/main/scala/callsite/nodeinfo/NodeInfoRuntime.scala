package callsite.nodeinfo

import java.lang.management.ManagementFactory

import scala.collection.JavaConverters._

case class NodeInfoRuntime(
    runtimeName: String,
    runtimeInputArgs: String,
    runtimeUptime: Long,
    runtimeVMName: String,
    runtimeVMVendor: String,
    runtimeVMVersion: String,
    runtimeSpecName: String,
    runtimeSpecVendor: String,
    runtimeSpecVersion: String
)

object NodeInfoRuntime {

  def nodeInfoRuntime: NodeInfoRuntime = {
    val runtime = ManagementFactory.getRuntimeMXBean

    NodeInfoRuntime(
      runtime.getName,
      runtimeInputArgs   = runtime.getInputArguments.asScala.toString(),
      runtimeUptime      = runtime.getUptime,
      runtimeVMName      = runtime.getVmName,
      runtimeVMVendor    = runtime.getVmVendor,
      runtimeVMVersion   = runtime.getVmVersion,
      runtimeSpecName    = runtime.getSpecName,
      runtimeSpecVendor  = runtime.getSpecVendor,
      runtimeSpecVersion = runtime.getManagementSpecVersion
    )
  }

}
