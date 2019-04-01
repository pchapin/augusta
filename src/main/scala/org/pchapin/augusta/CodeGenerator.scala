package org.pchapin.augusta

import org.slem.IRTree._

import scala.collection.mutable

class CodeGenerator(CFG: ControlFlowGraph, symbolTable: SymbolTable, reporter: Reporter) {

  val locationMap: LocationMap = LocationMap()

  private val LLVM = new LLVMGenerator(symbolTable, reporter)

  def makeAST: L_FunctionDefinition = {
    val ControlFlowGraph(_, graph, _) = CFG

    def makeBasicBlockAST(block: BasicBlock): L_Block = {
      val assignments = for (statement <- block.assignments) yield {
        LLVM.visit(statement)
      }
      // TODO: Fix the terminator instruction and label.
      L_Block(assignments, L_Ret(0: Long), "labelMe")
    }

    // TODO: Create an L_Block filled with L_Alloc instructions for each of the variables.
    // TODO: Add the L_Alloc instructions to locationMap as well.

    val generatedBlocks = for (node <- graph.nodes) yield {
      makeBasicBlockAST(node)
    }
    L_FunctionDefinition(L_VoidType(), generatedBlocks.toList, "main")
  }

}
