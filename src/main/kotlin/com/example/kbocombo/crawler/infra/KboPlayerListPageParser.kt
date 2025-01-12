package com.example.kbocombo.crawler.infra

import com.example.kbocombo.player.vo.PlayerPosition
import com.example.kbocombo.player.vo.WebId
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class KboPlayerListPageParser {

    fun parse(response: ResponseEntity<String>): List<Pair<WebId, PlayerPosition>> {
        val body = requireNotNull(response.body)
        val document = Jsoup.parse(body)

        return document.select("td a")
            .mapNotNull { parseElement(it) }
    }

    private fun parseElement(element: Element): Pair<WebId, PlayerPosition>? {
        val href = element.attr("href")
        return when {
            isPitcher(href) -> extractWebId(href) to PlayerPosition.PITCHER
            isHitter(href) -> extractWebId(href) to PlayerPosition.HITTER
            else -> null
        }
    }

    private fun isPitcher(href: String): Boolean = href.contains("PitcherDetail")
    private fun isHitter(href: String): Boolean = href.contains("HitterDetail")
    private fun extractWebId(href: String) = WebId(href.substringAfter("playerId="))
}
