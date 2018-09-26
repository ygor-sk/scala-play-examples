package sk.ygor.scalactica2d.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
  * Inspired by http://www.warski.org/blog/2012/12/starting-with-scala-macros-a-short-tutorial/
  */
object DebugMacro {

  def debugParameters(params: Any*): String = macro debugParameters_Impl

  def debugParameters_Impl(c: blackbox.Context)(params: c.Expr[Any]*): c.Expr[String] = {
    import c.universe._

    val trees = params.map(param => {
      val paramRep = show(param.tree)
      val paramRepTree = Literal(Constant(paramRep))
      val paramRepExpr = c.Expr[String](paramRepTree)
      reify {
        paramRepExpr.splice + " = " + param.splice
      }
    })
    c.Expr[String](q"""Seq(..$trees).mkString(", ")""")
  }
}
