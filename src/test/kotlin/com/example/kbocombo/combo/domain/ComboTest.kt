package com.example.kbocombo.combo.domain

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import java.time.LocalDateTime

class ComboTest : StringSpec({

    "Combo 생성에 성공한다" {
        val startDateTime = LocalDateTime.parse("2025-03-28T18:30:00")
        val game = getGame(startDateTime)

        shouldNotThrow<Throwable> {
            Combo(
                memberId = 1L,
                game = game,
                playerId = 1L,
                now = LocalDateTime.parse("2025-03-27T18:30:00")
            )
        }
    }

    listOf(3L, 4L)
        .forEach { dayGap ->
            "게임 시작 2일 이내에만 콤보 생성이 가능하다" {
                val startDateTime = LocalDateTime.parse("2025-03-28T18:30:00")
                val game = getGame(startDateTime)

                val exception = shouldThrow<IllegalArgumentException> {
                    Combo(
                        memberId = 1L,
                        playerId = 1L,
                        game = game,
                        now = startDateTime.minusDays(dayGap)
                    )
                }

                exception.message shouldContain "게임 시작 2일 전부터 등록할 수 있습니다."
            }
        }

    listOf(9L, 10L)
        .forEach { minGap ->
            "게임 시작 최소 10분전에만 콤보 생성이 가능하다" {
                val startDateTime = LocalDateTime.parse("2025-03-28T18:30:00")
                val game = getGame(startDateTime)

                val exception = shouldThrow<IllegalArgumentException> {
                    Combo(
                        memberId = 1L,
                        playerId = 1L,
                        game = game,
                        now = startDateTime.minusMinutes(minGap)
                    )
                }

                exception.message shouldContain "게임 시작 10분 이내에만 등록할 수 있습니다."
            }
        }

    listOf(9L, 10L)
        .forEach { minGap ->
            "게임 시작 최소 10분전에만 콤보 삭제가 가능하다" {
                val startDateTime = LocalDateTime.parse("2025-03-28T18:30:00")
                val game = getGame(startDateTime)
                val combo = Combo(
                    memberId = 1L,
                    playerId = 1L,
                    game = game,
                    now = startDateTime.minusMinutes(30)
                )


                val exception = shouldThrow<IllegalArgumentException> { combo.checkDelete(startDateTime.minusMinutes(minGap)) }

                exception.message shouldContain "게임 시작 10분 이내에만 삭제할 수 있습니다."
            }
        }
})

private fun getGame(startDateTime: LocalDateTime) = fixture.giveMeKotlinBuilder<Game>()
    .setExp(Game::startDateTime, startDateTime)
    .sample()
