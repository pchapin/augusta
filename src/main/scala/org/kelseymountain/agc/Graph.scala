package org.kelseymountain.agc

/**
 * Represents a graph type class with nodes of type `N` and labeled edges with a label type `L`.
 */
trait Graph[N, L]:
  def nodes: Set[N]
  def successors(node: N): Map[L, N]
