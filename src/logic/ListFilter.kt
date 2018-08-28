package logic

import javafx.scene.control.ListView
import javafx.scene.control.TextField
import model.Clazz

class ListFilter(val listView: ListView<Clazz>, val searchBox: TextField) {
    private var currentFilter = ""
    var currentClazz: List<Clazz> = listOf()
        set(value) {
            field = value
            doFilter()
        }
    init {
        searchBox.textProperty().addListener { observable, oldValue, newValue ->
            currentFilter = newValue.toLowerCase()
            doFilter()
        }
    }

    private fun doFilter() {
        listView.items.apply {
            clear()
            addAll(currentClazz.filter { s -> s.name.toLowerCase().contains(currentFilter) }.sortedBy { it.name })
        }
    }
}