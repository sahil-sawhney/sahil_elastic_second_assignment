package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.JsonFileServices

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(service:JsonFileServices) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {

    implicit request =>
    Ok("my application rocks").withHeaders("Access-Control-Allow-Origin" -> "*")
  }

  def fetchJson(filePath: String)=Action{

    implicit request =>
      Ok(service.fetchJson(filePath)).withHeaders("Access-Control-Allow-Origin" -> "*")
  }

  def generateJson(folderPath: String)=Action{

    implicit request =>
      Ok(service.generateJson(folderPath)).withHeaders("Access-Control-Allow-Origin" -> "*")
  }

}
