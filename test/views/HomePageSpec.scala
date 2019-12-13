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
import controllers.routes
import matchers.JsonMatchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.twirl.api.Html
import uk.gov.hmrc.viewmodels.NunjucksSupport
import views.behaviours.ViewBehaviours

class HomePageSpec extends SpecBase with MockitoSugar with NunjucksSupport with JsonMatchers with ViewBehaviours{

  lazy val homePageRoute: String = routes.HomePageController.onPageLoad().url
  implicit val request = FakeRequest(GET, homePageRoute).withCSRFToken

  val html: Html = renderer.render("homePage.njk").futureValue

  behave like normalPage(html, "homePage",
    "p1", "p2.1", "p2.anchor", "p2.2", "p3", "p4.1", "p4.2", "p5",
    "numberOfDays.l1", "numberOfDays.l2", "warning", "helpAndGuidance.h2")

  behave like pageWithCommonMessages(html, "common.statutoryResidenceTest")

  "link" - {
    "information" in {
      assertLinkByUrl(asDocument(html), config.informationUrl)
    }

    "Statutory Residence Test (SRT)" in {
      assertLinkByUrl(asDocument(html), config.statutoryResidenceTestUrl)
    }
  }

  "continue-button" - {
    behave like pageWithSubmitButton(asDocument(html), "continue")
  }
}
