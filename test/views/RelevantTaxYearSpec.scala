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

package views

import base.SpecBase
import config.FrontendAppConfig
import controllers.actions.{DataRequiredAction, DataRequiredActionImpl, FakeIdentifierAction, IdentifierAction}
import controllers.routes
import forms.RelevantTaxYearFormProvider
import matchers.JsonMatchers
import models.requests.OptionalDataRequest
import models.{NormalMode, RelevantTaxYear}
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.twirl.api.Html
import renderer.Renderer
import uk.gov.hmrc.viewmodels.NunjucksSupport
import views.behaviours.{QuestionViewBehaviours, ViewBehaviours}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.test.CSRFTokenHelper._

class RelevantTaxYearSpec extends SpecBase with MockitoSugar with NunjucksSupport with
  JsonMatchers with ViewBehaviours with QuestionViewBehaviours{


  lazy val relevantTaxYearRoute: String = routes.RelevantTaxYearController.onPageLoad(NormalMode).url
  implicit val request = FakeRequest(GET, relevantTaxYearRoute).withCSRFToken
  val formProvider = new RelevantTaxYearFormProvider()
  val form = formProvider()


  val json = Json.obj(
    "form"   -> form,
    "mode"   -> NormalMode,
    "radios"  -> RelevantTaxYear.radios(form)
  )

  val html: Html = renderer.render("relevantTaxYear.njk" ,json).futureValue

    behave like normalPage(html, "relevantTaxYear",
    "pY", "pY-1", "pY-2","pY-3")

  behave like optionsPage[RelevantTaxYear](
    renderer.render, "relevantTaxYear.njk", form, "value", RelevantTaxYear.radios)

  /*behave like pageWithCommonMessages(html, "common.statutoryResidenceTest", "common.result.p1",
    "common.result.further.info", "common.result.further.info.l1", "common.result.further.info.l2",
    "common.result.further.info.hmrc", "common.result.further.info.l3", "common.result.further.info.accountant")*/



}
