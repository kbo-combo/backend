package com.example.kbocombo.crawler.service

class PlayerSearchParamGenerator {
    private companion object {
        private const val PREFIX = "ctl00\$ctl00\$ctl00\$cphContents\$cphContents\$cphContents\$"
        private const val SCRIPT_MANAGER_KEY = "ScriptManager1"
        private const val PAGE_KEY = "${PREFIX}hfPage"
        private const val POSITION_KEY = "${PREFIX}ddlPosition"
        private const val SCRIPT_MANAGER_SUM = "udpRecord|"
        private const val PAGE = "hfPage"
        private const val EVENT_TARGET_KEY = "__EVENTTARGET"
        private const val VIEW_STATE_KEY = "__VIEWSTATE"
        private const val VIEW_STATE_GENERATOR_KEY = "__VIEWSTATEGENERATOR"
        private const val EVENT_VALIDATION_KEY = "__EVENTVALIDATION"
        private const val ASYNC_POST_KEY = "__ASYNCPOST"
    }
}
