package callsite

case class CallSiteInfo(
    enclosingClass: String, //name of the enclosing unit
    file: String, //file for the callsite (relative to the git root)
    line: Int, //line in file
    commit: String, //id of the commit
    buildAt: Long, //time you build at
    status: String, //one of "clean", "modified", "untracked"
    fingerprint: String, //file content hash
    fileContent: Option[String] = None //if the build file is different
)

object CallSiteInfo {
  import language.experimental.macros

  implicit def callSiteInfo: CallSiteInfo = macro CallSiteMacro.callSiteImpl
}
