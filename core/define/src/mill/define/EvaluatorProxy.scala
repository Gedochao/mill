package mill.define

import mill.api.*
import mill.api.internal.*

final class EvaluatorProxy(var delegate0: () => Evaluator) extends Evaluator {
  private def delegate = delegate0()
  override def allowPositionalCommandArgs = delegate.allowPositionalCommandArgs
  override def selectiveExecution = delegate.selectiveExecution
  override def workspace = delegate.workspace
  override def baseLogger = delegate.baseLogger
  override def outPath = delegate.outPath
  override def codeSignatures = delegate.codeSignatures
  override def rootModule = delegate.rootModule
  override def workerCache = delegate.workerCache
  override def env = delegate.env
  override def effectiveThreadCount = delegate.effectiveThreadCount
  override def offline: Boolean = delegate.offline

  def withBaseLogger(newBaseLogger: Logger): Evaluator = delegate.withBaseLogger(newBaseLogger)

  def resolveSegments(
      scriptArgs: Seq[String],
      selectMode: SelectMode,
      allowPositionalCommandArgs: Boolean,
      resolveToModuleTasks: Boolean
  ): mill.api.Result[List[Segments]] = {
    delegate.resolveSegments(
      scriptArgs,
      selectMode,
      allowPositionalCommandArgs,
      resolveToModuleTasks
    )
  }

  def resolveTasks(
      scriptArgs: Seq[String],
      selectMode: SelectMode,
      allowPositionalCommandArgs: Boolean = false,
      resolveToModuleTasks: Boolean = false
  ): mill.api.Result[List[Task.Named[?]]] = {
    delegate.resolveTasks(scriptArgs, selectMode, allowPositionalCommandArgs, resolveToModuleTasks)
  }
  def resolveModulesOrTasks(
      scriptArgs: Seq[String],
      selectMode: SelectMode,
      allowPositionalCommandArgs: Boolean = false,
      resolveToModuleTasks: Boolean = false
  ): mill.api.Result[List[Either[Module, Task.Named[?]]]] = {
    delegate.resolveModulesOrTasks(
      scriptArgs,
      selectMode,
      allowPositionalCommandArgs,
      resolveToModuleTasks
    )
  }
  def plan(tasks: Seq[Task[?]]): Plan = delegate.plan(tasks)

  def groupAroundImportantTargets[T](topoSortedTargets: mill.define.internal.TopoSorted)(
      important: PartialFunction[
        Task[?],
        T
      ]
  ): MultiBiMap[T, Task[?]] = delegate.groupAroundImportantTargets(topoSortedTargets)(important)

  /**
   * Collects all transitive dependencies (targets) of the given targets,
   * including the given targets.
   */
  def transitiveTargets(sourceTargets: Seq[Task[?]]): IndexedSeq[Task[?]] =
    delegate.transitiveTargets(sourceTargets)

  /**
   * Takes the given targets, finds all the targets they transitively depend
   * on, and sort them topologically. Fails if there are dependency cycles
   */
  def topoSorted(transitiveTargets: IndexedSeq[Task[?]]): mill.define.internal.TopoSorted =
    delegate.topoSorted(transitiveTargets)

  def execute[T](
      targets: Seq[Task[T]],
      reporter: Int => Option[CompileProblemReporter] = _ => Option.empty[CompileProblemReporter],
      testReporter: TestReporter = TestReporter.DummyTestReporter,
      logger: Logger = baseLogger,
      serialCommandExec: Boolean = false,
      selectiveExecution: Boolean = false
  ): Evaluator.Result[T] = {
    delegate.execute(
      targets,
      reporter,
      testReporter,
      logger,
      serialCommandExec,
      selectiveExecution
    )
  }

  def evaluate(
      scriptArgs: Seq[String],
      selectMode: SelectMode,
      reporter: Int => Option[CompileProblemReporter] = _ => None,
      selectiveExecution: Boolean = false
  ): mill.api.Result[Evaluator.Result[Any]] = {
    delegate.evaluate(scriptArgs, selectMode, reporter, selectiveExecution)
  }
  def close = delegate0 = null

  def selective = delegate.selective
}
