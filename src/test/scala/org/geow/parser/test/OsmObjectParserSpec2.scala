package org.geow.parser.test

import org.geow.model._
import org.geow.model.geometry.OsmPoint
import org.geow.parser.impl.OsmXmlParser
import org.geow.parser.impl.OsmXmlParser._
import org.junit.runner._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import org.specs2.runner._

import scala.io.Source

@RunWith(classOf[JUnitRunner])
class OsmObjectParserSpec2 extends Specification with ScalaCheck {

  val props1 = <test id="2465725143" visible="true" version="2" changeset="32423" timestamp="2014-04-16T19:23:01Z" user="test_user" uid="18130" ></test>
  val props2 = <test visible="true" version="2" changeset="32423" timestamp="2014-04-16T19:23:01Z" user="test_user" uid="18130" ></test>
  val props3 = <test id="2465725143" version="2" changeset="32423" timestamp="2014-04-16T19:23:01Z" user="test_user" uid="18130" ></test>
  val props4 = <test id="2465725143" visible="true" changeset="32423" timestamp="2014-04-16T19:23:01Z" user="test_user" uid="18130" ></test>
  val props5 = <test id="2465725143" visible="true" version="2" timestamp="2014-04-16T19:23:01Z" user="test_user" uid="18130" ></test>
  val props6 = <test id="2465725143" visible="true" version="2" changeset="32423"  user="test_user" uid="18130" ></test>
  val props7 = <test id="2465725143" visible="true" version="2" changeset="32423" timestamp="2014-04-16T19:23:01Z" ></test>
  

  import OsmXmlParser._

  "The OsmObjectParser" should {

    "parse the full osm properties" in {

      val attr = props1.attributes
      val actualProps = parseProperties(attr)

      val expectedTimestamp = convertXmlDateToLong("2014-04-16T19:23:01Z")
      val expectedProps = OsmProperties(OsmId(2465725143L), Some(OsmUser("test_user", 18130L)), OsmVersion(expectedTimestamp, 2, 32423, true))

      actualProps must beSuccessfulTry(expectedProps)
    }
    "fail parsing properties upon missing osm id" in {

      val attr = props2.attributes
      val actualProps = parseProperties(attr)

      val expectedTimestamp = convertXmlDateToLong("2014-04-16T19:23:01Z")
      val expectedProps = OsmProperties(OsmId(2465725143L), Some(OsmUser("test_user", 18130L)), OsmVersion(expectedTimestamp, 2, 32423, true))

      actualProps must beFailedTry
    }
    "parse osm properties and set version to \"1\" in case of missing value" in {

      val attr = props4.attributes
      val actualProps = parseProperties(attr)

      val expectedTimestamp = convertXmlDateToLong("2014-04-16T19:23:01Z")
      val expectedProps = OsmProperties(OsmId(2465725143L), Some(OsmUser("test_user", 18130L)), OsmVersion(expectedTimestamp, 2, 32423, true))

      actualProps.get.version.versionNumber must be_==(1)
    }
    "parse osm properties and set changeset to \"1\" in case of missing value" in {

      val attr = props5.attributes
      val actualProps = parseProperties(attr)

      val expectedTimestamp = convertXmlDateToLong("2014-04-16T19:23:01Z")
      val expectedProps = OsmProperties(OsmId(2465725143L), Some(OsmUser("test_user", 18130L)), OsmVersion(expectedTimestamp, 2, 32423, true))

      actualProps.get.version.changeset must be_==(1)
    }
    "parse osm properties and set the timestamp to \"now\" in case of missing value" in {

      val expectedTimestamp = System.currentTimeMillis()

      val attr = props6.attributes
      val actualProps = parseProperties(attr)

      actualProps.get.version.timestamp must be_>=(expectedTimestamp)
    }
    "parse osm properties and set the user to \"None\" in case of missing value" in {

      val expectedTimestamp = System.currentTimeMillis()

      val attr = props7.attributes
      val actualProps = parseProperties(attr)

      actualProps.get.user must beNone
    }
  }

}