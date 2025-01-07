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

    fun getCookie(response: ResponseEntity<String>): String? {
        return response.headers[HttpHeaders.SET_COOKIE]?.firstOrNull()
    }

    fun createPositionFilterParam(response: ResponseEntity<String>): MultiValueMap<String, String> {
        val body = requireNotNull(response.body)
        val formData = getFilterDefaultForm(body)
        formData.add(SCRIPT_MANAGER_KEY, POSITION_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(EVENT_TARGET_KEY, POSITION_BUTTON)
        return formData
    }

    fun createPageParam(response: ResponseEntity<String>, page: Int, position: String): MultiValueMap<String, String> {
        val body = requireNotNull(response.body)
        val formData = extractPageParam(body)
        formData.add(EVENT_TARGET_KEY, PAGE_BUTTON)
        formData.add(SCRIPT_MANAGER_KEY, PAGE_SCRIPT_MANAGER_VALUE)
        formData.add(POSITION_BUTTON, "2")
        return formData
    }

    private fun getFilterDefaultForm(body: String): LinkedMultiValueMap<String, String> {
        val formData = extractFilterParam(body)
        formData.add(SCRIPT_MANAGER_KEY, POSITION_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(EVENT_TARGET_KEY, POSITION_BUTTON)
        formData.add(POSITION_BUTTON, "2")
        return formData
    }

    private fun extractFilterParam(body: String): LinkedMultiValueMap<String, String> {
        val document = Jsoup.parse(body)
        return createFormDataWithExtractor { key -> getStateById(key, document) }
    }

    private fun extractPageParam(body: String): LinkedMultiValueMap<String, String> {
        return createFormDataWithExtractor { key -> getStateValue(key, body) }
    }

    private fun createFormDataWithExtractor(
        extractor: (String) -> String
    ): LinkedMultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add(VIEW_STATE_KEY, extractor(VIEW_STATE_KEY))
        formData.add(VIEW_STATE_GENERATOR_KEY, extractor(VIEW_STATE_GENERATOR_KEY))
        formData.add(EVENT_VALIDATION_KEY, extractor(EVENT_VALIDATION_KEY))
        formData.add(ASYNC_POST_KEY, "true")
        return formData
    }

    private fun getStateValue(key: String, body: String): String {
        val viewStateStart = "|hiddenField|$key|"
        val viewStateEnd = "|"
        val startIndex = body.indexOf(viewStateStart) + viewStateStart.length
        val endIndex = body.indexOf(viewStateEnd, startIndex)
        return body.substring(startIndex, endIndex)
    }

    private fun getStateById(key: String, document: Document): String {
        return document.getElementById(key)?.attr("value")
            ?: throw IllegalArgumentException("State By Id is null")
    }

    private companion object {
        private const val PREFIX = "ctl00\$ctl00\$ctl00\$cphContents\$cphContents\$cphContents\$"
        private const val SCRIPT_MANAGER_KEY = "${PREFIX}ScriptManager1"
        private const val POSITION_BUTTON = "${PREFIX}ddlPosition"
        private const val PAGE_BUTTON = "${PREFIX}ucPager\$btnNo5"
        private const val SCRIPT_MANAGER_SUM = "udpRecord|"
        private const val POSITION_FILTER_SCRIPT_MANAGER_VALUE = "${PREFIX}${SCRIPT_MANAGER_SUM}${POSITION_BUTTON}"
        private const val PAGE_SCRIPT_MANAGER_VALUE = "${PREFIX}${SCRIPT_MANAGER_SUM}${PAGE_BUTTON}"
        private const val EVENT_TARGET_KEY = "__EVENTTARGET"
        private const val VIEW_STATE_KEY = "__VIEWSTATE"
        private const val VIEW_STATE_GENERATOR_KEY = "__VIEWSTATEGENERATOR"
        private const val EVENT_VALIDATION_KEY = "__EVENTVALIDATION"
        private const val ASYNC_POST_KEY = "__ASYNCPOST"
    }
}
