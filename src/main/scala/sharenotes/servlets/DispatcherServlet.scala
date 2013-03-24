package sharenotes.servlets

import sharenotes.model.User
import sharenotes.util.Ofy
import unfiltered.request._
import unfiltered.response._

case class Twirl(html: twirl.api.Html) extends ComposeResponse(HtmlContent ~> ResponseString(html.body))

class App extends unfiltered.filter.Plan {
  import QParams._

  def intent = {
    case GET(Path("/")) => {
      val userToSave = new User("test@test.com", "password", "Testy Testerson")
      Ofy.save.entity(userToSave).now()
      val user = Ofy.load.`type`(classOf[User]).id("test@test.com").get
      Ok ~> Twirl(html.welcome.render(user))
    }

  }
}