import java.io.FileInputStream
import java.io.FileReader
import java.io.StreamTokenizer
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap
import kotlin.collections.HashSet

val firstPattern = "(?<type>[ A-Za-z/]+)\r\n(?<name>[ A-Za-z]+)\r\n"
val mainProfile = "WS BS S T Ag Int WP Fel\r\n(?<mp>\\s?((–|—)\\s?|(\\+(\\d+)%\\s?)){8})"
val secProfile = "A W SB TB M Mag IP FP\r\n(?<sp>\\s?((–|—)\\s?|(\\+(\\d+)\\s?)){8})"

val skillPattern = "Skills:(?<skills>([\\s\\w,()\\/’!&*:.–-])+)" +
        "Talents:(?<talents>([\\s\\w,()\\/’!&*:.–-])+)" +
        "Trappings:(?<trappings>([\\s\\w,()\\/’!&*:.–-])+)" +
        "Career Entries:(?<entries>([\\s\\w,()\\/’!&*:.–-])+)" +
        "Career Exits:(?<exits>([ \\w,()\\/’!&*:.–-])+)\r?\n"

fun main(vararg args:String){
    val classes = HashMap<String, Clazz>()
    val skills = HashSet<String>()
    val talents = HashSet<String>()

    Scanner(FileInputStream("res/data.raw")).use {
        it.useDelimiter("\\d+\r\nCareers")
        while (it.hasNext()) {
            val chDesc = it.next()
            val professionMt = Pattern.compile(firstPattern).matcher(chDesc)
            if (professionMt.find()) {
                val char = Clazz(professionMt.group("name"), professionMt.group("type"))
                val profileMt = Pattern.compile(mainProfile).matcher(chDesc)
                if (profileMt.find()) {
                    char.setMainProfile(profileMt.group("mp"))
                } else
                    System.err.println("${char.name} main profile not found")

                val secMt = Pattern.compile(secProfile).matcher(chDesc)
                if (secMt.find()) {
                    char.setSecProfile(secMt.group("sp"))
                } else
                    System.err.println("${char.name} second profile not found")

                val skillsMt = Pattern.compile(skillPattern).matcher(chDesc)
                if (skillsMt.find()) {
                    char.setSkills(skillsMt.group("skills"))
                    char.setTalents(skillsMt.group("talents"))
                    char.setTrappings(skillsMt.group("trappings"))
                    char.setEntries(skillsMt.group("entries"))
                    char.setExits(skillsMt.group("exits"))
                } else
                    System.err.println("${char.name} skill tree not found")

                skills.addAll(char.skills)
                talents.addAll(char.talents)
                classes.put(char.name, char)
                //println(char)
            }
        }
        println(skills.toList().sorted())
        println("++++++++++++++++++++++")
        println(talents.toList().sorted())
    }

    Scanner(FileInputStream("res/list.raw")).use {
        while (it.hasNextLine()) {
            val cname =  it.nextLine()
            if(!classes.containsKey(cname))
                System.err.println("$cname NOT FOUND")
        }
    }
}