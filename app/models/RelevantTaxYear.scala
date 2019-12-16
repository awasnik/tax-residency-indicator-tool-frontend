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

import org.joda.time.DateTime
import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json._
import uk.gov.hmrc.time.{CurrentTaxYear, TaxYear}
import uk.gov.hmrc.viewmodels.Text.Literal
import uk.gov.hmrc.viewmodels._

sealed trait RelevantTaxYear

object RelevantTaxYear extends Enumerable.Implicits with CurrentTaxYear{

  override def now = () => DateTime.now
  val cTaxYear = taxYearFor(today)

 /* case object Py extends WithName("pY") with RelevantTaxYear
  case object Py1 extends WithName("pY-1") with RelevantTaxYear
  case object Py2 extends WithName("pY-2") with RelevantTaxYear
  case object Py3 extends WithName("pY-3") with RelevantTaxYear*/
 case object Py extends WithName(getTaxYear(cTaxYear,1)) with RelevantTaxYear
  case object Py1 extends WithName(getTaxYear(cTaxYear,2)) with RelevantTaxYear
  case object Py2 extends WithName(getTaxYear(cTaxYear,3)) with RelevantTaxYear
  case object Py3 extends WithName(getTaxYear(cTaxYear,4)) with RelevantTaxYear


  val values: Seq[RelevantTaxYear] = Seq(
    Py,
    Py1,
    Py2,
    Py3
  )


/*  val values: Seq[String] = Seq(
    getTaxYear(cTaxYear,1),
    getTaxYear(cTaxYear,2),
    getTaxYear(cTaxYear,3),
    getTaxYear(cTaxYear,4)
  )*/

  def getTaxYear(taxYear: TaxYear, counter: Int):String = {
    (taxYear.currentYear.toInt-counter).toString + (taxYear.currentYear.toInt-(counter-1)).toString.replaceFirst("20","-")
  }

  def radios(form: Form[_])(implicit messages: Messages): Seq[Radios.Item] = {

    val field = form("value")
    val items = Seq(
      Radios.Radio(Literal(getTaxYear(cTaxYear,1)), Py.toString),
      Radios.Radio(Literal(getTaxYear(cTaxYear,2)), Py1.toString),
      Radios.Radio(Literal(getTaxYear(cTaxYear,3)), Py2.toString),
      Radios.Radio(Literal(getTaxYear(cTaxYear,2)), Py3.toString)
    )

    Radios(field, items)
  }

implicit val enumerable: Enumerable[RelevantTaxYear] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
