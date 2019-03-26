package org.pchapin.augusta

import org.slem.IRTree._

class CodeGenerator(CFG: ControlFlowGraph, symbolTable: SymbolTable, reporter: Reporter) {

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

    // The entryBlock needs to be augmented with L_Alloc instructions to allocate items in the
    // symbol table. A mapping from the symbol name to the corresponding L_Alloc instruction
    // needs to also be created. Probably a separate block should be created for the L_Alloc.

    val generatedBlocks = for (node <- graph.nodes) yield {
      makeBasicBlockAST(node)
    }
    L_FunctionDefinition(L_VoidType(), generatedBlocks.toList, "main")
  }

}
