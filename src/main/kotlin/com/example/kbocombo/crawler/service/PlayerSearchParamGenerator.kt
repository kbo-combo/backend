package com.example.kbocombo.crawler.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class PlayerSearchParamGenerator {

    fun createPositionFilterParam(response: ResponseEntity<String>): MultiValueMap<String, String> {
        val body = requireNotNull(response.body)
        val formData = getDefaultForm(body)
        formData.add(SCRIPT_MANAGER_KEY, POSITION_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(EVENT_TARGET_KEY, POSITION_BUTTON)
        formData.add(ASYNC_POST_KEY, "true")
        return formData
    }

    private fun getDefaultForm(body: String): LinkedMultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        val document = Jsoup.parse(body)
        formData.add(SCRIPT_MANAGER_KEY, POSITION_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(VIEW_STATE_KEY, getStateById(VIEW_STATE_KEY, document))
        formData.add(VIEW_STATE_GENERATOR_KEY, getStateById(VIEW_STATE_GENERATOR_KEY, document))
        formData.add(EVENT_VALIDATION_KEY, getStateById(EVENT_VALIDATION_KEY, document))
        formData.add(EVENT_TARGET_KEY, POSITION_BUTTON)
        formData.add(POSITION_BUTTON, "2")
        return formData
    }

    private fun getStateById(key: String, document: Document): String  {
        return document.getElementById(key)?.attr("value")
            ?: throw IllegalArgumentException("State By Id is null")
    }

    fun getCookie(response: ResponseEntity<String>): String? {
        return response.headers[HttpHeaders.SET_COOKIE]?.firstOrNull()
    }

    private companion object {
        private const val PREFIX = "ctl00\$ctl00\$ctl00\$cphContents\$cphContents\$cphContents\$"
        private const val SCRIPT_MANAGER_KEY = "${PREFIX}ScriptManager1"
        private const val POSITION_BUTTON = "${PREFIX}ddlPosition"
        private const val PAGE_KEY = "${PREFIX}hfPage"
        private const val SCRIPT_MANAGER_SUM = "udpRecord|"
        private const val POSITION_FILTER_SCRIPT_MANAGER_VALUE = "${PREFIX}${SCRIPT_MANAGER_SUM}${POSITION_BUTTON}"
        private const val PAGE = "hfPage"
        private const val EVENT_TARGET_KEY = "__EVENTTARGET"
        private const val VIEW_STATE_KEY = "__VIEWSTATE"
        private const val VIEW_STATE_GENERATOR_KEY = "__VIEWSTATEGENERATOR"
        private const val EVENT_VALIDATION_KEY = "__EVENTVALIDATION"
        private const val ASYNC_POST_KEY = "__ASYNCPOST"
    }
}
