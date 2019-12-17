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

package models

import models.RelevantTaxYear.{taxYearFor, today}
import org.joda.time.DateTime
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.libs.json.{JsError, JsString, Json}

class RelevantTaxYearSpec extends FreeSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "RelevantTaxYear" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(RelevantTaxYear.values)

      forAll(gen) {
        relevantTaxYear =>

          JsString(relevantTaxYear.toString).validate[RelevantTaxYear].asOpt.value mustEqual relevantTaxYear
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!RelevantTaxYear.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[RelevantTaxYear] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(RelevantTaxYear.values)

      forAll(gen) {
        relevantTaxYear =>

          Json.toJson(relevantTaxYear) mustEqual JsString(relevantTaxYear.toString)
      }
    }

    "should have the correct year" in {
      def now = () => DateTime.now
      val cTaxYear = taxYearFor(today)
      assert(RelevantTaxYear.getTaxYear(cTaxYear,1) == "2018-19")
    }

    "should not have the correct year" in {
      def now = () => DateTime.now
      val cTaxYear = taxYearFor(today)
      assert(RelevantTaxYear.getTaxYear(cTaxYear,3) != "2018-19")
    }
  }
}
