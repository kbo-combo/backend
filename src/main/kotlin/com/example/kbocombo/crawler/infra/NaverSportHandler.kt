package com.example.kbocombo.crawler.infra

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.application.NaverSportClient
import com.example.kbocombo.crawler.utils.toTeamFilterCode
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.player.vo.Team
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class NaverSportHandler(
    private val naverSportClient: NaverSportClient,
    private val gameRepository: GameRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    /**
     * -- 오늘 경기에 안타 친 선수 찾기 --
     * 1. 오늘 진행 중인 경기 가져오기
     * 2. 해당 경기에서 홈팀, 어웨이팀 코드를 기반으로 게임 코드 생성
     * 3. 생성한 게임 코드를 기반으로 오늘 경기 기록 조회
     * 4. 안타를 기록한 선수가 있으면 기록지에 추가. -> 오늘의 안타 이벤트 발행 -> 해당 선수를 투표한 사용자 콤보 달성
     */
    fun run(startDate: LocalDate) {
        val games = gameRepository.findAllByStartDate(startDate)
            .filter { it.isRunning() }

        games.forEach { game ->
            val gameCode = generateGameCode(game.homeTeam, game.awayTeam, startDate)
            analyzeGameRecord(game, gameCode)
        }
    }

    private fun analyzeGameRecord(game: Game, gameCode: String) {
        val gameRecordJsonData = naverSportClient.getLiveGameRecord(gameCode = gameCode)
        val gameRecord = Gson().fromJson(gameRecordJsonData, NaverSportApiResponse::class.java)

        if (gameRecord.isFailed()) {
            logInfo("${gameCode}에 대한 경기 기록 조회에 실패했습니다.")
            return
        }

        if (gameRecord.isGameCanceled()) {
            logInfo("${gameCode}에 대한 경기는 취소되었습니다.")
            return
        }
        val awayTeamHitters = gameRecord.result.recordData.battersBoxscore.away
        val homeTeamHitters = gameRecord.result.recordData.battersBoxscore.home
        awayTeamHitters.forEach { checkIfHitExist(it, game) }
        homeTeamHitters.forEach { checkIfHitExist(it, game) }
    }

    /**
     * 게임 코드 생성 규칙
     * ex) 2024-06-29, SSG:두산, 잠실야구장 -> 20240629 + SK + OB + 0 + 2024 (정규시즌)
     * ex) 2024-10-11, KT:LG, 잠실야구장 -> 33331011 + KT + LG + 0  + 2024 (플레이오프) <일단 보류>
     */
    private fun generateGameCode(
        homeTeam: Team,
        awayTeam: Team,
        startDate: LocalDate,
    ): String {
        val formattedDate = startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val awayTeamCode = toTeamFilterCode(awayTeam)
        val homeTeamCode = toTeamFilterCode(homeTeam)
        return "${formattedDate}${awayTeamCode}${homeTeamCode}" + "0" + startDate.year
    }

    private fun checkIfHitExist(batter: Batter, game: Game) {
        if (batter.isHitCompleted()) {
            val playerCode = batter.playerCode
            eventPublisher.publishEvent(HitterHitRecordedEvent(game.id, playerCode))
        }
        return
    }
}

data class NaverSportApiResponse(
    val code: Int,
    val success: Boolean,
    val result: Result
) {
    fun isFailed(): Boolean = !success

    fun isGameCanceled(): Boolean = result.recordData.battersBoxscore.home.isEmpty()
            || result.recordData.battersBoxscore.away.isEmpty()
}

data class Result(
    val recordData: RecordData
)

data class RecordData(
    val battersBoxscore: BattersBoxscore
)

// 네이버 스포츠에서 Batter 키워드로 사용함. (혼란방지)
data class BattersBoxscore(
    val away: List<Batter>,
    val home: List<Batter>
)

data class Batter(
    val bb: Int,
    val inn9: String,
    val batOrder: Int,
    val playerCode: String,
    val run: Int,
    val hr: Int,
    val inn24: String,
    val inn23: String,
    val inn22: String,
    val hit: Int,
    val inn21: String,
    val pos: String,
    val hra: String,
    val inn25: String,
    val inn2: String,
    val inn1: String,
    val inn4: String,
    val inn3: String,
    val inn6: String,
    val inn20: String,
    val inn5: String,
    val inn8: String,
    val inn7: String,
    val kk: Int,
    val ab: Int,
    val inn19: String,
    val inn18: String,
    val hasPlayerEnd: Boolean,
    val inn13: String,
    val inn12: String,
    val inn11: String,
    val inn10: String,
    val inn17: String,
    val inn16: String,
    val inn15: String,
    val inn14: String,
    val name: String,
    val rbi: Int
) {
    fun isHitCompleted(): Boolean = hit > 0
}

data class HitterHitRecordedEvent(
    val gameId: Long,
    val playerCode: String
)
