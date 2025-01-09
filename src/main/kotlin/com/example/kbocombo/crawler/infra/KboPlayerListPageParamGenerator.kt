package com.example.kbocombo.crawler.infra

import com.example.kbocombo.crawler.utils.toTeamFilterCode
import com.example.kbocombo.player.vo.Team
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class KboPlayerListPageParamGenerator {

    fun generateTeamFilterParam(response: ResponseEntity<String>, team: Team): MultiValueMap<String, String> {
        val body = requireNotNull(response.body)
        val formData = getFilterDefaultForm(body, team)
        formData.add(SCRIPT_MANAGER_KEY, TEAM_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(EVENT_TARGET_KEY, TEAM_BUTTON)
        return formData
    }

    fun generatePageParam(response: ResponseEntity<String>, page: Int, team: Team): MultiValueMap<String, String> {
        val body = requireNotNull(response.body)
        val formData = extractPageParam(body, team)
        val buttonSuffix = getPageButtonSuffix(page)
        formData.add(EVENT_TARGET_KEY, PAGE_BUTTON_PREFIX + buttonSuffix)
        formData.add(SCRIPT_MANAGER_KEY, PAGE_SCRIPT_MANAGER_VALUE + buttonSuffix)
        return formData
    }

    private fun getFilterDefaultForm(body: String, team: Team): LinkedMultiValueMap<String, String> {
        val formData = extractFilterParam(body, team)
        formData.add(SCRIPT_MANAGER_KEY, TEAM_FILTER_SCRIPT_MANAGER_VALUE)
        formData.add(EVENT_TARGET_KEY, TEAM_BUTTON)
        return formData
    }

    private fun extractFilterParam(body: String, team: Team): LinkedMultiValueMap<String, String> {
        val document = Jsoup.parse(body)
        return createFormDataWithExtractor(team, { key -> getStateById(key, document) })
    }

    private fun extractPageParam(body: String, team: Team): LinkedMultiValueMap<String, String> {
        return createFormDataWithExtractor(team, { key -> getStateValue(key, body) })
    }

    private fun getPageButtonSuffix(page: Int): String {
        return when (page % 5) {
            1 -> if (page == 1) "btnNo1" else "btnNext"
            2 -> "btnNo2"
            3 -> "btnNo3"
            4 -> "btnNo4"
            0 -> "btnNo5"
            else -> error("잘못된 Page")
        }
    }

    private fun createFormDataWithExtractor(
        team: Team,
        extractor: (String) -> String
    ): LinkedMultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add(VIEW_STATE_KEY, extractor(VIEW_STATE_KEY))
        formData.add(VIEW_STATE_GENERATOR_KEY, extractor(VIEW_STATE_GENERATOR_KEY))
        formData.add(EVENT_VALIDATION_KEY, extractor(EVENT_VALIDATION_KEY))
        formData.add(ASYNC_POST_KEY, "true")
        formData.add(TEAM_BUTTON, toTeamFilterCode(team))
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
        private const val TEAM_BUTTON = "${PREFIX}ddlTeam"
        private const val PAGE_BUTTON_PREFIX = "${PREFIX}ucPager\$"
        private const val SCRIPT_MANAGER_SUM = "udpRecord|"
        private const val TEAM_FILTER_SCRIPT_MANAGER_VALUE = "$PREFIX$SCRIPT_MANAGER_SUM$TEAM_BUTTON"
        private const val PAGE_SCRIPT_MANAGER_VALUE = "$PREFIX$SCRIPT_MANAGER_SUM$PAGE_BUTTON_PREFIX"
        private const val EVENT_TARGET_KEY = "__EVENTTARGET"
        private const val VIEW_STATE_KEY = "__VIEWSTATE"
        private const val VIEW_STATE_GENERATOR_KEY = "__VIEWSTATEGENERATOR"
        private const val EVENT_VALIDATION_KEY = "__EVENTVALIDATION"
        private const val ASYNC_POST_KEY = "__ASYNCPOST"
    }
}
