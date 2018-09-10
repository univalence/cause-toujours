package callsite.nodeinfo

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
      processors = os.getAvailableProcessors,
      osArch     = os.getArch,
      osName     = os.getName,
      osVersion  = os.getVersion
    )
  }

}
