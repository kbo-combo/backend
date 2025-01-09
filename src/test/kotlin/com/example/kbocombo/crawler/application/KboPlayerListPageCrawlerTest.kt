package com.example.kbocombo.crawler.application

import com.example.kbocombo.crawler.infra.KboPlayerListPageCrawler
import com.example.kbocombo.domain.player.vo.WebId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KboPlayerListPageCrawlerTest @Autowired constructor(
    private val sut: KboPlayerListPageCrawler
) : FunSpec({

    context("모든 선수를 팀별로 페이징해서 잘 가져온다") {
        val webIds = sut.getPlayers().map { it.webId }
        test("(KIA) 최형우의 WebId를 포함한다") {
            webIds shouldContain WebId(72443)
        }

        test("(삼성) 이호성의 WebId를 포함한다") {
            webIds shouldContain WebId(53455)
        }

        test("(LG) 홍창기의 WebId를 포함한다") {
            webIds shouldContain WebId(66108)
        }

        test("(두산) 홍건희의 WebId를 포함한다") {
            webIds shouldContain WebId(61643)
        }

        test("(KT) 황재균의 WebId를 포함한다") {
            webIds shouldContain WebId(76313)
        }

        test("(SSG) 한유섬의 WebId를 포함한다") {
            webIds shouldContain WebId(62895)
        }

        test("(롯데) 황성빈의 WebId를 포함한다") {
            webIds shouldContain WebId(50500)
        }

        test("(한화) 황준서의 WebId를 포함한다") {
            webIds shouldContain WebId(54729)
        }

        test("(NC) 한재승의 WebId를 포함한다") {
            webIds shouldContain WebId(51994)
        }

        test("(키움) 최주환의 WebId를 포함한다") {
            webIds shouldContain WebId(76267)
        }

        test("WebId 리스트에 중복이 없어야 한다") {
            webIds shouldHaveSize webIds.toSet().size
        }
    }
})
