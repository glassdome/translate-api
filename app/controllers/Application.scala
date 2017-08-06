package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import io.glassdome.translate._

import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  case class Sentence(
      data: String, 
      mapping: Option[Map[String,String]]
  )
      
  implicit lazy val sentenceFormat = Json.format[Sentence]
  
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def translateSnake() = Action.async(parse.json) { implicit request: Request[JsValue] =>
    Future {
      val sen = request.body.validate[Sentence].get
      Ok(SnakeTranslator.translate(sen.data))
    }
  }
  
  def translateLeet() = Action.async(parse.json) { implicit request: Request[JsValue] =>
    Future {
      val sen = request.body.validate[Sentence].get
      Ok(LeetTranslator.translate(sen.data, sen.mapping getOrElse Map()))
    
    }
  }  
}
