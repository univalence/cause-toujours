package callsite

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
      runtime.getInputArguments.asScala.toString(),
      runtime.getUptime,
      runtime.getVmName,
      runtime.getVmVendor,
      runtime.getVmVersion,
      runtime.getSpecName,
      runtime.getSpecVendor,
      runtime.getManagementSpecVersion
    )
  }
}
