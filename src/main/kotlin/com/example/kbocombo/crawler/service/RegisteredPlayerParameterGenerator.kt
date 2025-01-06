package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.utils.RegisteredPageTeamConverter
import com.example.kbocombo.domain.Team
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class RegisteredPlayerParameterGenerator {

    fun getParams(response: ResponseEntity<String>): MultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        val body = response.body ?: throw IllegalArgumentException("Initial body is null")
        setDefaultParams(formData, body) { key -> getStateById(key, Jsoup.parse(body)) }
        formData.add(EVENT_TARGET, BUTTON)
        return formData
    }

    fun updateTeam(params: MultiValueMap<String, String>, team: Team): MultiValueMap<String, String> {
        return LinkedMultiValueMap(params).apply {
            this[SELECT_TEAM_KEY] = RegisteredPageTeamConverter.convert(team).name
        }
    }

    private fun setDefaultParams(
        formData: MultiValueMap<String, String>,
        body: String?,
        getState: (String) -> String
    ) {
        formData.add(SCRIPT_MANAGER_KEY, SCRIPT_MANAGER_VALUE)
        formData.add(SELECT_SEARCH_DATE, getDate(body ?: throw IllegalStateException("KBO Stats Body is null")))
        addFormData(formData, VIEW_STATE, getState)
        addFormData(formData, VIEW_STATE_GENERATOR, getState)
        addFormData(formData, EVENT_VALIDATION, getState)
        formData.add("__ASYNCPOST", "true")
    }

    private fun addFormData(formData: MultiValueMap<String, String>, key: String, getValue: (String) -> String) {
        formData.add(key, getValue(key))
    }

    private fun getStateById(key: String, document: Document): String {
        return document.getElementById(key)?.attr("value")
            ?: throw IllegalStateException("State By Id is null")
    }

    private fun getDate(body: String): String {
        return Jsoup.parse(body)
            .getElementById(DATE_ID)
            .attribute("value")
            .value
    }

    companion object {
        private const val DATE_ID = "cphContents_cphContents_cphContents_hfSearchDate"
        private const val PREFIX = "ctl00\$ctl00\$ctl00\$cphContents\$cphContents\$cphContents\$"
        private const val RECORD = "udpRecord|"
        private const val SELECT_TEAM_KEY = "${PREFIX}hfSearchTeam"
        private const val SELECT_SEARCH_DATE = "${PREFIX}hfSearchDate"
        private const val SCRIPT_MANAGER_KEY = "${PREFIX}ScriptManager1"
        private const val BUTTON = "${PREFIX}btnCalendarSelect"
        private const val SCRIPT_MANAGER_VALUE = "$PREFIX$RECORD$BUTTON"
        private const val EVENT_TARGET = "__EVENTTARGET"
        private const val VIEW_STATE = "__VIEWSTATE"
        private const val VIEW_STATE_GENERATOR = "__VIEWSTATEGENERATOR"
        private const val EVENT_VALIDATION = "__EVENTVALIDATION"
    }
}
