package callsite

import java.nio.file.{Path ⇒ JPath}

import scala.io.Source.fromFile
import scala.reflect.io.Path

case class ContainerInfo(containerId: String)

object ContainerInfo {
  def fromPath(path: JPath): ContainerInfo = {
    val fileContent = fromFile(path.toString).mkString
    val containerId = Path(fileContent).name

    ContainerInfo(containerId)
  }
}
