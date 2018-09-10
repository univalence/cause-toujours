package callsite

import java.lang.management.ManagementFactory

case class NodeInfoOS(
    processors: Int,
    osArch: String,
    osName: String,
    osVersion: String
)

object NodeInfoOS {
  def nodeInfoOS: NodeInfoOS = {
    val os = ManagementFactory.getOperatingSystemMXBean
    NodeInfoOS(
      os.getAvailableProcessors,
      os.getArch,
      os.getName,
      os.getVersion
    )
  }
}
