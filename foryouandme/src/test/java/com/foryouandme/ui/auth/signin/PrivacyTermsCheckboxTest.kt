package com.foryouandme.ui.auth.signin

import com.google.common.truth.Truth
import org.junit.Test

class PrivacyTermsCheckboxTest {

    @Test
    fun testPrivacyTermsText() {
        testParsing("Test Privacy and Terms Text")
    }

    @Test
    fun testTermsPrivacyText() {
        testParsing("Test Terms and Privacy Text")
    }

    @Test
    fun testPrivacyText() {
        testParsing("Test Privacy Text", terms = "")
    }

    @Test
    fun testTermsText() {
        testParsing("Test Terms Text", privacy = "")
    }

    @Test
    fun testText() {
        testParsing("Test Text", privacy = "", terms = "")
    }


    private fun testParsing(
        text: String,
        privacy: String = PRIVACY,
        terms: String = TERMS
    ) {

        val result = getPrivacyTermsAnnotatedString(text, privacy, terms)
        Truth.assertThat(result.text).isEqualTo(text)
    }


    companion object {

        private const val PRIVACY = "Privacy"

        private const val TERMS = "Terms"

    }

}