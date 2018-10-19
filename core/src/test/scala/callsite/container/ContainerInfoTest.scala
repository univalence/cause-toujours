package callsite.container

import java.io.FileWriter
import java.nio.file.{Files, Path}

import org.scalatest.{FunSuiteLike, Matchers}

class ContainerInfoTest extends FunSuiteLike with Matchers {

  test("should get container info from the cpuset of the init process") {
    val data         = "/docker/584295459285092095"
    val cpuset: Path = Files.createTempFile("cpuset", "")

    val fw = new FileWriter(cpuset.toFile)
    try {
      fw.write(data)
    } finally fw.close()

    val containerInfo: ContainerInfo = ContainerInfo.fromPath(cpuset)

    containerInfo.containerId should be("584295459285092095")
  }

}
