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
import matchers.JsonMatchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.twirl.api.Html
import renderer.Renderer
import uk.gov.hmrc.viewmodels.NunjucksSupport
import views.behaviours.ViewBehaviours

class NonTaxResidentResultSpec extends SpecBase with MockitoSugar with NunjucksSupport with JsonMatchers with ViewBehaviours{

  lazy val nonResidentRoute: String = routes.NonTaxResidentResultController.onPageLoad().url
  implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(GET, nonResidentRoute)

  val html: Html = renderer.render("nonTaxResidentResult.njk").futureValue

  behave like normalPage(html, "nonTaxResidentResult",
    "forTaxYear", "taxYear", "info")

  behave like pageWithCommonMessages(html, "common.statutoryResidenceTest", "common.result.p1",
    "common.result.further.info", "common.result.further.info.l1", "common.result.further.info.l2",
    "common.result.further.info.hmrc", "common.result.further.info.l3", "common.result.further.info.accountant")

  "link" - {
    "information" in {
      assertLinkByUrl(asDocument(html), config.contactHmrcUrl)
    }

    "Accountant link" in {
      assertLinkByUrl(asDocument(html), config.taxHelpUrl)
    }

    "Statutory Residence Test (SRT)" in {
      assertLinkByUrl(asDocument(html), config.statutoryResidenceTestUrl)
    }
  }

}
