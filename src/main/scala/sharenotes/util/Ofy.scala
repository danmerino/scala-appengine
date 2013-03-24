package sharenotes.util

import com.google.appengine.api.datastore.{ Entity, Transaction }
import com.google.appengine.api.datastore.ReadPolicy.Consistency
import com.googlecode.objectify.cmd.{ Deleter, Saver, Loader, LoadType }
import com.googlecode.objectify.{ ObjectifyService, Work, ObjectifyFactory, Objectify, TxnType }
import com.googlecode.objectify.util.cmd.LoaderWrapper
import util.DynamicVariable

import sharenotes.model.User

/* Provides a loader that has a kind method that defers to type since type is reserved in scala */
class ScalaLoader(val loader: Loader) extends LoaderWrapper[ScalaLoader](loader) {
  def kind[E](kind: Class[E]): LoadType[E] = loader.`type`(kind)
}

/* Provides a threadlocal version of Objectify, lazily instantiated, one per thread
 * Kudos to Jorrit Posthuma at http://thoughtsofthree.com/2012/04/scala-objectify4/ for the code */

trait Ofy extends Objectify {

  def ofy: Objectify

  def execute[R](txnType: TxnType, work: Work[R]): R = ofy.execute(txnType, work)
  def load: ScalaLoader = new ScalaLoader(ofy.load)
  def save: Saver = ofy.save
  def delete: Deleter = ofy.delete
  def toEntity(pojo: Any): Entity = ofy.toEntity(pojo)
  def toPojo[T](entity: Entity) = ofy.toPojo(entity)
  def getTxn: Transaction = ofy.getTxn
  def getFactory: ObjectifyFactory = ofy.getFactory
  def consistency(policy: Consistency): Ofy = new OfyWrapper(ofy.consistency(policy))
  def deadline(value: java.lang.Double): Ofy = new OfyWrapper(ofy.deadline(value))
  def cache(value: Boolean): Ofy = new OfyWrapper(ofy.cache(value))
  def transaction(): Ofy = new OfyWrapper(ofy.transaction())
  def transact[R](work: Work[R]): R = ofy.transact(work)
  def transactNew[R](limitTries: Int, work: Work[R]): R = ofy.transactNew(limitTries, work)
  def transactNew[R](work: Work[R]): R = ofy.transactNew(work)
  def transactionless: Objectify = ofy.transactionless
  def clear = ofy.clear
  def setWrapper(ofy: Objectify) = ofy.setWrapper(ofy)
}

class OfyWrapper(val base: Objectify) extends Ofy {

  base.setWrapper(this)

  override def ofy: Objectify = base

}

object Ofy extends Ofy {

  ObjectifyService.register(classOf[User])

  def ofy: Objectify = ObjectifyService.factory.begin()

}
