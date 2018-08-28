package gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.ListFilter;
import logic.ParserClazz;
import model.Clazz;
import model.Value;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GuiCtrl {
    public ListView<Clazz> nextProf;
    public TextField search;
    public Label firstClassTree;
    public Label extraClassTree;
    public Label expo;
    public GridPane profileGrid;
    public RadioButton fc, ec;
    public ListView<String> skillList, talentList;

    private ParserClazz parser = new ParserClazz();
    private ListFilter listFilter;

    private List<Clazz> firstClazzes = new ArrayList<>();
    private List<Clazz> extraClazzes = new ArrayList<>();


    private String[] ordinary = "WS BS S T Ag Int WP Fel A W Mag".split(" ");
    private IntegerProperty numExpo = new SimpleIntegerProperty(0);
    private int[] cStats = new int[11];
    private int cRow = 1;

    private Map<Value, Integer> skills = new HashMap<>(), talents = new HashMap<>();

    @FXML
    public void initialize(){
        parser.parse();
        listFilter = new ListFilter(nextProf, search);
        listFilter.setCurrentClazz(parser.getClasses()
                .values().stream()
                .filter(clazz -> clazz.getType().contains("Basic"))
                .collect(Collectors.toList())
        );

        nextProf.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        expo.textProperty().bind(numExpo.asString());

        changeListenerFor(fc, firstClazzes);
        changeListenerFor(ec, extraClazzes);
    }

    private void changeListenerFor(RadioButton radioButton, List<Clazz> currentClazzes) {
        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) return;
            if(currentClazzes.size() > 0){
                Clazz lastClazz = currentClazzes.get(currentClazzes.size() - 1);
                listFilter.setCurrentClazz(filterNextClazzFor(lastClazz));
            } else {
                listFilter.setCurrentClazz(filterNextClazzFor(null));
            }
        });
    }

    public void preview(ActionEvent actionEvent) {
        if(nextProf.getSelectionModel().isEmpty()) return;
        Stage wnd = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/Character.fxml"));
            Scene scene = new Scene(loader.load());

            Clazz selectedClazz = nextProf.getSelectionModel().getSelectedItem();
            ((Character)loader.getController()).setClazz(selectedClazz);

            wnd.setScene(scene);
            wnd.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choose(ActionEvent actionEvent) {
        if(nextProf.getSelectionModel().isEmpty()) return;
        Clazz selectedClazz = nextProf.getSelectionModel().getSelectedItem();

        for (int i = 0; i < ordinary.length; i++) {
            int cVal = cStats[i], nVal = selectedClazz.getProfile().get(ordinary[i]);
            if(cVal >= nVal) {
                profileGrid.getChildren().add(createStat(i, cVal));
                cStats[i] = cVal;
            } else {
                numExpo.setValue(numExpo.getValue() + (((nVal - cVal) / 5) * 100));
                profileGrid.getChildren().add(createStat(i, nVal));
                cStats[i] = nVal;
            }
        }

        selectedClazz.getSkills().forEach(value -> {
            skills.computeIfPresent(value, (s, integer) -> integer + 10);
            skills.putIfAbsent(value, 0);
        });
        selectedClazz.getTalents().forEach(value -> {
            talents.computeIfPresent(value, (s, integer) -> integer + 10);
            talents.putIfAbsent(value, 0);
        });

        refreshLists();

        if(firstClazzes.size() > 0) {
            numExpo.setValue(numExpo.getValue() + 100);
            numExpo.setValue(numExpo.getValue() + (selectedClazz.getSkills().size() * 100));
            numExpo.setValue(numExpo.getValue() + (selectedClazz.getTalents().size() * 100));
        }
        search.setText("");
        cRow++;

        if(fc.isSelected()) {
            firstClazzes.add(selectedClazz);
            firstClassTree.setText(firstClassTree.getText() + selectedClazz.toString() + "\r\n");
        } else {
            extraClazzes.add(selectedClazz);
            extraClassTree.setText(extraClassTree.getText() + selectedClazz.toString() + "\r\n");
        }
        listFilter.setCurrentClazz(filterNextClazzFor(selectedClazz));
    }

    private void refreshLists() {
        skillList.getItems().clear();
        talentList.getItems().clear();

        skillList.getItems().addAll(skills.entrySet().stream()
                .sorted(Comparator.comparing(valueIntegerEntry -> valueIntegerEntry.getKey().toString()))
                .map(valueIntegerEntry -> valueIntegerEntry.getKey().toString() + " (" + valueIntegerEntry.getValue() + ")")
                .collect(Collectors.toList())
        );

        talentList.getItems().addAll(talents.entrySet().stream()
                .sorted(Comparator.comparing(valueIntegerEntry -> valueIntegerEntry.getKey().toString()))
                .map(valueIntegerEntry -> valueIntegerEntry.getKey().toString() + " (" + valueIntegerEntry.getValue() + ")")
                .collect(Collectors.toList())
        );

    }

    private List<Clazz> filterNextClazzFor(Clazz clz) {
        if(clz == null) {
            return parser.getClasses()
                    .values().stream()
                    .filter(clazz -> clazz.getType().contains("Basic"))
                    .collect(Collectors.toList());
        } else {
            return parser.getClasses()
                    .values().stream()
                    .filter(clazz -> (clz.getExits().contains(clazz.getName())
                            || clazz.getEntries().contains(clz.getName()))
                            && !firstClazzes.contains(clazz) && !extraClazzes.contains(clazz)
                    ).collect(Collectors.toList());
        }
    }

    private Label createStat(int col, int value) {
        Label label = new Label(String.valueOf(value == 0 ? "" : value));
        GridPane.setRowIndex(label, cRow);
        GridPane.setColumnIndex(label, col);
        return label;
    }

}
