package callsite.git

import java.io.File

trait SourceControlManager {
  def isClean(file: File): Boolean

  def lastCommitIdOf(file: File): String

  def repoRootPathFrom(file: File): String
}
