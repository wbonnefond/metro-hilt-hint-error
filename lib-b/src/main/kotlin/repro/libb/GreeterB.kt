package repro.libb

import dev.zacsweers.metro.Inject

@Inject
public class GreeterB {
  public fun greet(): String = "Hello from lib-b"
}
