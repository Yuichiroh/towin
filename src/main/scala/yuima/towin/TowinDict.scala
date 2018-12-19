package yuima.towin

import java.io.File

import yuima.util.IO
import yuima.util.control._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source

object TowinDict {
  private var rules = mutable.Map[String, mutable.Map[String, List[String]]]()

  /** extracts suffixes of conjugation forms for all conjugation types from UniDic lex.csv, and creates object dump.
    *
    * An example of a line in lex.csv is the following:
    * あからもふ,8261,8467,8350,動詞,一般,*,*,五段-マ行,意志推量形,アカラム,赤らむ,あからもふ,アカラモー,あからむ,アカラム,和,*,*,*,*,*,*,用,アカラモフ,アカラム,アカラモウ,アカラム,"3,0",C1,M1@1,12784854987055718,46511
    *
    * This object extracts a suffix by calculate diff of a surface form and a lemma form.
    */
  def main(args: Array[String]): Unit = {
    val data = Source.fromFile(args(0)).getLines()
    val out = args(1)

    for (line <- data) {
      val Array(surface, _, _, _, pos, pos1, pos2, pos3, infType, infForm, _, _, _, _, lemma, _*) = line.split(",")
      if (infType != "*") {
        val rule = rules.getOrElseUpdate(createQuery(lemma, pos, infType), mutable.Map[String, List[String]]())
        val curr = rule.getOrElse(infForm, Nil)
        rule.update(infForm, (surface :: curr).distinct)
      }
    }

    for (oos <- IO.Out.oos(out)) {
      oos.writeObject(rules)
    }
  }

  def load(path: String): Unit = load(new File(path))

  def load(file: File): Unit = {
    rules = {
      for (ois <- IO.In.ois(file)) yield {
        ois.readObject().asInstanceOf[mutable.Map[String, mutable.Map[String, List[String]]]]
      }
    }
  }

  def allInflectionsOf(lemma: String, pos: String, infType: String): Map[String, List[String]] = {
    val key = createQuery(lemma, pos, infType)
    rules.getOrElse(key, Map.empty[String, List[String]]).toMap
  }

  def inflectionOf(lemma: String, pos: String, infType: String)(infForm: String): List[String] = {
    val key = createQuery(lemma, pos, infType)
    rules(key)(infForm)
  }

  def createQuery(lemma: String, pos: String, infType: String) = s"$lemma:$pos:$infType"

  def stemming(w1: String, w2: String): String = stemming(w1.toList, w2.toList)

  @tailrec
  def stemming(w1: List[Char], w2: List[Char], chars: List[Char] = Nil): String = {
    if (w1.isEmpty || w2.isEmpty) chars.reverse.mkString
    else {
      w1.head match {
        case c if c == w2.head => stemming(w1.tail, w2.tail, c :: chars)
        case _ => chars.reverse.mkString
      }
    }
  }
}
