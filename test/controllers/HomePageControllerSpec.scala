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

import base.SpecBase
import models.{NormalMode}
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.Html
import repositories.SessionRepository

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class HomePageControllerSpec extends SpecBase with MockitoSugar {

  "HomePage Controller" - {

    "return OK and the correct view for a GET" in {

      when(mockRenderer.render(any(), any())(any()))
        .thenReturn(Future.successful(Html("")))

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      val request = FakeRequest(GET, routes.HomePageController.onPageLoad().url)
      val templateCaptor = ArgumentCaptor.forClass(classOf[String])

      val result = route(application, request).value

      status(result) mustEqual OK

      verify(mockRenderer, times(1)).render(templateCaptor.capture(), any())(any())

      templateCaptor.getValue mustEqual "homePage.njk"

      application.stop()
    }

    "must redirect to the next page when valid data is submitted and user session should be created" in {

      val application = applicationBuilder().build()

      val relevantTaxYearPageRoute = routes.RelevantTaxYearController.onPageLoad(NormalMode).url

      val request = FakeRequest(POST, routes.HomePageController.onSubmit().url)

      val result = Await.result(route(application, request).value, 5 seconds)

      injected[SessionRepository].get("id").futureValue must not be None

      result.header.status mustEqual SEE_OTHER

      redirectLocation(Future.successful(result)).value mustEqual relevantTaxYearPageRoute

      application.stop()
    }
  }
}
