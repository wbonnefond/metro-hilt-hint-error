package repro.app

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import repro.liba.GreeterA
import repro.libb.GreeterB

/**
 * Uses `AppScope`, not `Singleton`, so Hilt's own library entry points stay out of the graph. The
 * failure this project reproduces happens in dex merging, independent of any graph.
 */
@DependencyGraph(AppScope::class)
interface AppGraph {
  val greeterA: GreeterA
  val greeterB: GreeterB
}
