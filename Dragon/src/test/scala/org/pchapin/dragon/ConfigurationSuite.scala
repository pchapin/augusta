//-----------------------------------------------------------------------
// FILE    : ConfigurationSuite.scala
// SUBJECT : Tests for class ConfigurationSettings
// AUTHOR  : (C) Copyright 2018 by Peter C. Chapin <peter@pchapin.org>
//
//-----------------------------------------------------------------------
package org.pchapin.dragon

import org.scalatest.*
import funsuite.*
import matchers.*

class ConfigurationSuite extends AnyFunSuite with Assertions with should.Matchers {

  test("Basic configuration") {
    // TODO: Enhance the ConfigurationSettings test.

    val configurableItems =
      Map( "StringItem"  -> ConfigurationSettings.basicStringValidator  _,
           "BooleanItem" -> ConfigurationSettings.basicBooleanValidator _,
           "IntegerItem" -> ConfigurationSettings.basicIntegerValidator _)

    val currentSettings = new ConfigurationSettings(configurableItems)
    assert(currentSettings("StringItem" ).isEmpty)
    assert(currentSettings("BooleanItem").isEmpty)
    assert(currentSettings("IntegerItem").isEmpty)

    currentSettings.setDefaults(Map("StringItem" -> "Hello", "BooleanItem" -> "F"))
    assert(currentSettings("StringItem" ).contains("Hello"))
    assert(currentSettings("BooleanItem").contains("false"))
    assert(currentSettings("IntegerItem").isEmpty)

    currentSettings.readConfigurationFile(
      "Dragon" + java.io.File.separator + "testData" + java.io.File.separator + "example.cfg")
    assert(currentSettings("StringItem" ).contains("Item1"))
    assert(currentSettings("BooleanItem").contains("true" ))
    assert(currentSettings("IntegerItem").contains("1234" ))
  }

}
