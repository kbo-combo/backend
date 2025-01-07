package com.example.kbocombo

import com.example.kbocombo.crawler.service.PlayerSearchPageReader
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class PlayerSearchPageReaderTest @Autowired constructor(val finder: PlayerSearchPageReader) {

    @Test
    fun `모든 팀 선수들을 페이징 처리하며 잘 가져온다`() {
        val actual = finder.findAll()

        val webIds = actual.map { it.webId }

        // 은퇴 or 트레이드 아마 올해는 없을거고 이름이 뒷페이지인 선수들로 테스트
        // 최형우
        assert(webIds.contains(WebId(72443)))

        // 황재균
        assert(webIds.contains(WebId(76313)))

        // 한유섬
        assert(webIds.contains(WebId(62895)))

        // 황성빈
        assert(webIds.contains(WebId(50500)))

        // 황준서
        assert(webIds.contains(WebId(54729)))

        // 한재승
        assert(webIds.contains(WebId(51994)))

        // 최주환
        assert(webIds.contains(WebId(76267)))

        // 이호성
        assert(webIds.contains(WebId(53455)))

        // 홍창기
        assert(webIds.contains(WebId(66108)))

        // 홍건희
        assert(webIds.contains(WebId(61643)))

        assert(webIds.size == webIds.toSet().size)
    }
}
