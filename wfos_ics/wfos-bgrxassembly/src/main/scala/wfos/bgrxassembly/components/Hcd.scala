package wfos.bgrxassembly.components

import csw.params.commands.{CommandIssue, Setup}
import csw.params.core.generics.{Parameter}

// The trait now takes a generic type `T` to support both Int and Double
trait Hcd[T] {
  def validateParameters(setup: Setup): Either[CommandIssue, Parameter[T]]

  def inRange(parameter: Parameter[T], minVal: Parameter[T], maxVal: Parameter[T]): Either[CommandIssue, Parameter[T]]
}
