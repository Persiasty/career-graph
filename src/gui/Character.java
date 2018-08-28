package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Clazz;
import model.Value;

import java.util.Map;

public class Character {
    @FXML
    public Label ww, us, s, odp, zr, intel, sw, ogl, a, z, mg, clazz;

    @FXML
    private ListView<Value> skills, talents;

    @FXML
    public void initialize() { }

    public void setClazz(Clazz clazz) {
        this.clazz.setText(clazz.getName());
        clazz.getProfile().forEach((stat, value) -> {
            Map<String, String> mapping = Clazz.Companion.getGuiMapping();
            if(mapping.containsKey(stat)) {
                try {
                    ((Label)getClass().getDeclaredField(mapping.get(stat)).get(this))
                            .setText(String.valueOf(value == 0 ? "" : value));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        skills.getItems().addAll(clazz.getSkills());
        talents.getItems().addAll(clazz.getTalents());
    }
}
