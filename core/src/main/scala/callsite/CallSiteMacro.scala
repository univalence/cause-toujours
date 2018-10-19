package callsite
import java.io.File

import scala.annotation.tailrec
import scala.io.Source
import scala.reflect.macros.blackbox

object CallSiteMacro {

  import GitTools._

  lazy val buildAt: Long = System.currentTimeMillis()

  def fileContent(file: File): String =
    Source.fromFile(file).mkString

  def callSiteImpl(c: blackbox.Context): c.Expr[CallSiteInfo] = {
    import c._
    import universe._

    @tailrec
    def enclosingType(symbol: c.universe.Symbol): c.universe.Symbol =
      if (symbol.isType) symbol
      else enclosingType(symbol.owner)

    val sourceFile = enclosingPosition.source.file.file

    //don't include the source in the compiled code if it's not needed
    val source: c.universe.Tree =
      if (isClean(sourceFile))
        q"None"
      else
        q"""Some(${fileContent(sourceFile)})"""

    val owner: c.universe.Symbol = enclosingType(c.internal.enclosingOwner)

    c.Expr[CallSiteInfo](
      q"""callsite.CallSiteInfo(
  enclosingClass = ${owner.fullName},
  file           = ${pathToRepoRoot(sourceFile)},
  line           = ${enclosingPosition.line},
  commit         = ${lastCommitIdOf(sourceFile)},
  buildAt        = $buildAt,
  status         = ${fileStatus(sourceFile).toString.toLowerCase()},
  fingerprint    = ${hashObject(sourceFile)},
  fileContent    = $source
)"""
    )
  }

}
