import java.io.FileInputStream
import java.io.FileReader
import java.io.StreamTokenizer
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap

val firstPattern = "(?<type>[ A-Za-z/]+)\r\n(?<name>[ A-Za-z]+)\r\n"
val mainProfile = "WS BS S T Ag Int WP Fel\r\n(?<mp>\\s?((–|—)\\s?|(\\+(\\d+)%\\s?)){8})"
val secProfile = "A W SB TB M Mag IP FP\r\n(?<sp>\\s?((–|—)\\s?|(\\+(\\d+)\\s?)){8})"

fun main(vararg args:String){
    val classes = HashMap<String, Clazz>()

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
                classes.put(char.name, char)
                println(char)
            }
        }
    }

    Scanner(FileInputStream("res/list.raw")).use {
        while (it.hasNextLine()) {
            val cname =  it.nextLine()
            if(!classes.containsKey(cname))
                System.err.println("$cname NOT FOUND")
        }
    }
}