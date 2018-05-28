data class Clazz(val name: String, val type: String) {
    companion object {
        val mpStats = "WS BS S T Ag Int WP Fel".split(" ")
        val spStats = "A W SB TB M Mag IP FP".split(" ")
    }

    private val m_profile = HashMap<String, Int>()
    val profile
        get() = m_profile.toMap()

    private val m_skills = HashSet<String>()
    val skills
        get() = m_skills.toSet()

    private val m_talents = HashSet<String>()
    val talents
        get() = m_talents.toSet()

    private val m_trappings = HashSet<String>()
    val trappings
        get() = m_trappings.toSet()

    private val m_entries = HashSet<String>()
    val entries
        get() = m_entries.toSet()

    private val m_exits = HashSet<String>()
    val exits
        get() = m_exits.toSet()


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

    fun setSkills(skillsString: String) {
        m_skills.addAll(
                skillsString.replace("\\([^\\)]+\\)".toRegex(), "")
                        .split(",|\r?\n".toRegex())
                        .map { it.trim() }
                        .filter { it.length > 2 }
                        .toList()
        )
    }
    fun setTalents(talentsString: String) {
        m_talents.addAll(
                talentsString.replace("\\([^\\)]+\\)".toRegex(), "")
                        .split(",|\r?\n".toRegex())
                        .map { it.trim() }
                        .filter { it.length > 2 }
                        .toList()
        )
    }
    fun setTrappings(trappingsString: String) {
        m_trappings.addAll(trappingsString.split(",|\r?\n".toRegex()).map { it.trim() }.toList())
    }
    fun setEntries(entriesString: String) {
        m_entries.addAll(entriesString.split(",|\r?\n".toRegex()).map { it.trim() }.toList())
    }
    fun setExits(exitsString: String) {
        m_exits.addAll(exitsString.split(",|\r?\n".toRegex()).map { it.trim() }.toList())
    }

    override fun toString(): String {
        return "$name ($type): $profile, $skills, $talents"
    }
}