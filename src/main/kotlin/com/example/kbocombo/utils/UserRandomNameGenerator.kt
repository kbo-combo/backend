package com.example.kbocombo.utils

import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
class UserRandomNameGenerator {

    fun generate(): String {
        return "${getRandomPrefix()} ${getRandomSuffix()}_${getUuidByLength(6)}"
    }

    private fun getRandomPrefix(): String {
        val randomIndex = Random.Default.nextInt(PREFIX.size)
        return PREFIX[randomIndex]
    }

    private fun getRandomSuffix(): String {
        val randomIndex = Random.Default.nextInt(SUFFIX.size)
        return SUFFIX[randomIndex]
    }

    private fun getUuidByLength(length: Int): String {
        return UUID.randomUUID().toString().substring(0 until length)
    }

    companion object {
        val PREFIX = listOf(
            "유니폼 사고 싶은", "잘생긴", "멋있는", "사랑스러운", "자랑스러운", "FA로 데려오고 싶은", "기세가 좋은",
            "팬심 저격하는", "영구결번이 될", "명예의 전당급인", "수비가 기가막힌", "팬서비스 끝내주는", "올스타전에 뽑힐",
            "팀의 주장감인", "투혼을 보여주는", "상대팀이 두려워하는", "메이저리그에서 탐내는", "우아한", "골든글러브를 받을",
            "우승 DNA를 가진", "미스터 옥토버", "강렬한 눈빛의", "현재이자 미래인", "팬들의 자부심인", "슈퍼스타"
        )

        val SUFFIX = listOf(
            "이범호", "심재학", "김도영", "최형우", "양현종", "이의리", "윤영철", "나성범", "정해영", "김선빈",
            "박진만", "이종열", "구자욱", "김영웅", "이재현", "김지찬", "원태인", "오승환", "강민호", "김헌곤",
            "염경엽", "차명석", "박해민", "김현수", "홍창기", "문보경", "손주영", "임찬규", "장현식", "오스틴",
            "이승엽", "김태룡", "김재환", "양의지", "여동건", "곽빈", "김택연", "최준호", "양석환", "강승호",
            "이강철", "고영표", "소형준", "강백호", "배정대", "로하스", "쿠에바스", "장성우", "문상철", "박영현",
            "이숭용", "김재현", "김광현", "최정", "박성한", "노경은", "서진용", "한유섬", "박지환", "최지훈",
            "김태형", "노진혁", "유강남", "한현희", "성민규", "박세웅", "전준우", "김원중", "나승엽", "황성빈",
            "김경문", "손혁", "류현진", "문동주", "노시환", "김서현", "최재훈", "안치홍", "문현빈", "정은원",
            "이호준", "임선남", "구창모", "박민우", "신민혁", "김주원", "김휘집", "김영규", "손아섭", "박건우",
            "홍원기", "고형욱", "주승우", "김윤하", "김건희", "김동헌", "송성문", "김태진", "이주형", "정현우",
        )
    }
}
