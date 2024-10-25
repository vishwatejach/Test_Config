package wfos.bgrxassembly.components

import csw.params.commands.{CommandIssue, Setup}
import csw.params.commands.CommandIssue.{ParameterValueOutOfRangeIssue, MissingKeyIssue}
import csw.params.core.generics.{Parameter}
import wfos.lgmhcd.LgmInfo

// LgmHcd specifies that it works with Double
class Lgmhcd extends Hcd[Double] {

  // Implement validateParameters for Double
  override def validateParameters(setup: Setup): Either[CommandIssue, Parameter[Double]] = {
    val issueOrAccepted = for {
      targetGratingPosition <- setup.get(LgmInfo.targetGratingPositionKey).toRight(MissingKeyIssue("Target Grating Position key not found"))
      _                     <- inRange(targetGratingPosition, LgmInfo.minTargetGrating, LgmInfo.maxTargetGrating)
    } yield targetGratingPosition
    issueOrAccepted
  }

  // Implement the range check for Double
  override def inRange(
      parameter: Parameter[Double],
      minVal: Parameter[Double],
      maxVal: Parameter[Double]
  ): Either[CommandIssue, Parameter[Double]] = {
    val min = minVal.head
    val max = maxVal.head
    if (parameter.head >= min && parameter.head <= max) Right(parameter)
    else Left(ParameterValueOutOfRangeIssue(s"${parameter.keyName} should be in range of $min and $max"))
  }

}
