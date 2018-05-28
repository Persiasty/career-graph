data class Clazz(val name: String, val type: String) {
    companion object {
        val mpStats = "WS BS S T Ag Int WP Fel".split(" ")
        val spStats = "A W SB TB M Mag IP FP".split(" ")
    }

    private val m_profile = HashMap<String, Int>()
    val profile
        get() = m_profile.toMap()

    fun setMainProfile(profileString: String) {
        val list = profileString.split(" ").map {
            val ret = it.replace("+", "")
                    .replace("%", "")
            if(!ret.matches("\\d+".toRegex()))
                return@map "0"
            return@map ret
        }.toList()
        list.forEachIndexed { id, value ->
            if(list.size > 8 && id > 0)
                m_profile.put(mpStats[id - 1], value.toInt())
            else if(list.size == 8)
                m_profile.put(mpStats[id], value.toInt())
        }
    }

    fun setSecProfile(profileString: String) {
        val list = profileString.split(" ").map {
            val ret = it.replace("+", "")
            if(!ret.matches("\\d+".toRegex()))
                return@map "0"
            return@map ret
        }.toList()

        list.forEachIndexed { id, value ->
            if(list.size > 8 && id > 0)
                m_profile.put(spStats[id - 1], value.toInt())
            else if(list.size == 8)
                m_profile.put(spStats[id], value.toInt())
        }
    }

    override fun toString(): String {
        return "$name ($type): $profile"
    }
}