package org.kelseymountain.agc

import org.antlr.v4.runtime.tree.TerminalNode

/**
 * This trait describes the interface to error reporting objects. Different implementations
 * report errors in different ways. By centralizing the way errors are reported, changes can be
 * made to this important functionality without modifying the bulk of the compiler where errors
 * are detected.
 *
 * Conventions for error reporting:
 * - Messages are human-readable.
 * - Messages start with an uppercase letter.
 * - Messages do *not* end with a period.
 * - Messages are concise but lean towards being grammatically correct when feasible.
 */
trait Reporter:

  /**
   * Used to report an error condition. If an error is detected, code cannot be generated.
   *
   * @param errorNode The parse tree node where the error is to be indicated.
   * @param message The human-readable message describing the error.
   */
  def reportSourceError(errorNode: TerminalNode, message: String): Unit

  /**
   * Used to report a warning condition. Warnings do not prevent code from being generated.
   *
   * @param warningNode The parse tree node where the warning is to be indicated.
   * @param message THe human-readable message describing the warning.
   */
  def reportSourceWarning(warningNode: TerminalNode, message: String): Unit
