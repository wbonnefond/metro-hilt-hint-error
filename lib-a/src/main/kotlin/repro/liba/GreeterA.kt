package repro.liba

import dev.zacsweers.metro.Inject

@Inject
public class GreeterA {
  public fun greet(): String = "Hello from lib-a"
}
