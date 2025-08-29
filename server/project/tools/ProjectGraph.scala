package tools

import sbt._
import sbt.Keys._

object ProjectGraph extends AutoPlugin {
  object autoImport {
    /** Writes a Mermaid graph of inter-project dependencies (dependsOn and aggregates). */
    val projectDepsMermaid = taskKey[File]("Generate Mermaid graph for internal modules")
  }
  import autoImport._

  override def trigger = allRequirements

  override def buildSettings: Seq[Setting[_]] = Seq(
    projectDepsMermaid := {
      val lb         = loadedBuild.value
      val outDir     = (ThisBuild / baseDirectory).value / "docs"
      val outFile    = outDir / "modules.mmd.md"

      // Collect edges: A --> B if A.dependsOn(B); and dashed A -.-> B if A aggregates B
      case class Edge(from: String, to: String, style: String) // style: "-->" or "-.->"
      val edges: Seq[Edge] = lb.allProjectRefs.flatMap { ref =>
        Project.getProject(ref, lb).toSeq.flatMap { rp =>
          val name = ref.project
          val depEdges = rp.dependencies.map(_.project.project).distinct.map(dep => Edge(name, dep, "-->"))
          val aggEdges = rp.aggregate.map(_.project).distinct.map(agg => Edge(name, agg, "-.->"))
          depEdges ++ aggEdges
        }
      }.distinct

      val nodes: Seq[String] =
        (edges.flatMap(e => Seq(e.from, e.to)).distinct).sorted

      val nodeLines = nodes.map { n =>
        val label = n
        s"""    ${quote(n)}["$label"]"""
      }.mkString("\n")

      val edgeLines = edges.map { e =>
        s"""    ${quote(e.from)} ${e.style} ${quote(e.to)}"""
      }.mkString("\n")

      val mermaid =
        s"""|```mermaid
            |graph LR
            |%% solid --> = dependsOn; dashed -.-> = aggregate
            |$nodeLines
            |$edgeLines
            |```
            |""".stripMargin

      IO.createDirectory(outDir)
      IO.write(outFile, mermaid, append = false)
      streams.value.log.info(s"Wrote ${outFile.getPath}")
      outFile
    }
  )

  private def quote(s: String): String =
    "\"" + s.replace("\"", "'") + "\""
}
