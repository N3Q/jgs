package de.unifreiburg.cs.proglang.jgs.util

import java.util.stream.{Stream, StreamSupport}
import java.util.{Optional, Spliterator, Spliterators}

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by fennell on 3/9/16.
  */
object Interop {

  def asJavaOptional[T](o: Option[T]): Optional[T] =
    o.map(Optional.of(_)).getOrElse(Optional.empty())

  def asJavaOptional[T](o : Try[Option[T]]): Optional[T] =
    o.map(asJavaOptional(_)).getOrElse(Optional.empty())

  def asScalaOption[T](o: Optional[T]): Option[T] =
    o.map[Option[T]](new java.util.function.Function[T, Option[T]] {
      override def apply(t: T): Option[T] = Some(t)
    }).orElse(None)

  def asScalaIterator[T](i : java.util.Collection[T]) : Iterator[T] = i.iterator().asScala
  def asScalaIterator[T](i : java.util.Iterator[T]) : Iterator[T] = i.asScala

  def asJavaStream[T](i : Iterator[T]) : Stream[T] = {
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(i.asJava, Spliterator.ORDERED), false)
  }

  def asScalaSet[T](s : java.util.Set[T]) : Set[T] = s.asScala.toSet

}
