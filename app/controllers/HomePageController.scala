/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import controllers.actions._
import javax.inject.Inject
import models.{NormalMode, UserAnswers}
import navigation.Navigator
import pages.RelevantTaxYearPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import renderer.Renderer
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController

import scala.concurrent.ExecutionContext

class HomePageController @Inject()(
    override val messagesApi: MessagesApi,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    sessionRepository: SessionRepository,
    val controllerComponents: MessagesControllerComponents,
    renderer: Renderer,
    navigator: Navigator
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {


  def onPageLoad: Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      renderer.render("homePage.njk").map(Ok(_))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val userAnswer = UserAnswers(request.internalId)
      sessionRepository.set(userAnswer).map{ _ =>
        Redirect(navigator.nextPage(RelevantTaxYearPage,NormalMode,userAnswer))
      }
  }

}
