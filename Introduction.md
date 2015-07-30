# Introduction #

objregex is an experimental regular expression library, written in Java.


# Details #
  * In objregex, the regular expression is matched against an arbitrary sequence of `Object`s instead of a sequence of characters.
  * To do so, each literal token in the regular expression is mapped to a predicate. This predicate determines whether the given input object satisfies the literal token.
  * Also, literal token can reference another regular expression pattern. Moreover, you can make a self-referencing, recursive regular expression.