
Chapter 1 Notes
===============

The solution here uses the "classic" approach of creating a separate case class for each
non-terminal in the grammar. This follows the sketch (in ML) in MCIML. The advantage to this
approach is that each case class can have fields that are unique to it. Thus if special
information needs to be associated with a particular AST node type that is easy to do.

However, a disadvantage to this approach is that if one only needs to match a particular AST
node type it is still necessary to write a match expression with lots of cases to "pass through"
the recursion properly. Since each case class is a little different a single default case can't
easily handle them all uniformly.

This problem does not arise in the "straight-line" language presented in Chapter 1 of MCIML
because that language is so small. For example, there are only four "pass-through" cases in the
method maximumPrintArgumentCount. However, in a large language like Ada, with perhaps hundreds
of AST node types, the burden of writing so many trivial cases in such a method would be
significant. In short the classic approach to handling an AST in functional languages doesn't
scale.

An alternative approach that I've used in other compiler like programs I've written is to define
a single case class to handle all AST node types and indicate the specific kind of AST node as a
field in the case class. For example

    case class ASTNode(tokenType: Int, children: List[ASTNode])

In that case one can write match expressions such as

    node match {
      case ASTNode(1, children) => // Handle token (or pseudo-token) #1
      case ASTNode(2, children) => // Handle token (or pseudo-token) #2
      case ASTNode(_, children) =>
        // Recursively process all the ASTNodes in the children list.
    }

This means that all AST nodes are treated equally (which could be awkward) but it allows one to
collapse the trivial recursive step for the uninteresting node types into a single case.

It occurs to me that Scala might be able to combine the two approaches and get the best of both
worlds. This is untested code

    case class ASTNode(tokenType: Int, children: List[ASTNode])
    case class SpecializedNode(
      tokenType: Int,
      children : List[ASTNode],
      special  : Int) extends ASTNode(tokenType, children)

    node match {
      case SpecializedNode(1, children, special) => // Handle specialized version of token #1
      case ASTNode(_, children) =>
        // Recursively process all the ASTNodes in the children list.
    }

Note that in the above it would be possible to create a SpecializedNode that wraps around any
token type. That might or might not be a desirable feature. One might be able to restrict
SpecializedNode to only apply to certain token types by using a tokenType field in
SpecializedNode that was a subtype of the one used in ASTNode.

I should experiment with some of these approaches by perhaps re-implementing the straight-line
interpreter using these ideas.
