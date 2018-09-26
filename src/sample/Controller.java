package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller {

    public static fileHandler food = new fileHandler();

    public static String fileName = "menu.txt";

    @FXML
    AnchorPane rootPane;

    @FXML
    Label close, mFreqLabel, daysLabel, fileLabel, dSizeLabel, minimize, addLabel, delLabel;

    @FXML
    Label close_add, minimize_add, mFreqLabel_add, daysLabel_add, fileLabel_add, dSizeLabel_add;

    @FXML
    Label close_del, minimize_del, mFreqLabel_del, daysLabel_del, fileLabel_del, dSizeLabel_del;

    @FXML
    Label close_conf, minimize_conf, mFreqLabel_conf, daysLabel_conf, fileLabel_conf, dSizeLabel_conf;

    @FXML
    Button createMenu, addConf, delConf, addCancel, delCancel, editCancel, editConf;

    @FXML
    ListView dishList, addList, delList;

    @FXML
    TextArea display;

    @FXML
    BorderPane homePane, addPane, delPane, editPane;

    @FXML
    MenuItem addDishItem, delDishItem, configItem;

    @FXML
    TextField addField_Name, addField_Type, delField, mainSearch, addSearch, delSearch, editDays, editFreq;

    @FXML
    Slider lengthSlider, freqSlider;

    @FXML
    CheckBox useDefault;


    public void initialize() {
        initLists(1);
        initStats();
        initConfigs();
        food.initJokes();
        getMenu();

    }

    public void synchText()
    {
        editDays.setText(Integer.toString((int)lengthSlider.getValue()));
        editFreq.setText(Integer.toString((int)freqSlider.getValue()));
    }

    public void synchSliders()
    {
        if(editDays.getText().isEmpty())
        {
            lengthSlider.setValue(0);
        }

        if(editFreq.getText().isEmpty())
        {
            freqSlider.setValue(0);
        }

        if(food.sanitizeConfigs(food.table.size(), editDays.getText()))
        {
            lengthSlider.setValue(Integer.parseInt(editDays.getText()));
        }

        if(food.sanitizeConfigs(food.table.size(), editFreq.getText()))
        {
            freqSlider.setValue(Integer.parseInt(editFreq.getText()));
        }


    }


    public void changeSettings()
    {
        Boolean days_status, freq_status;

        if(useDefault.isSelected())
        {

            //im sorry for hard coding this but
            //its happening so deal with it
            food.days = 7;
            food.maxFreq = 3;

            initStats();

            cancelMenuConfigs();

            display.setText("Successfully Updated Settings\tUsed: Defaults");
        }
        else
        {
            days_status = food.sanitizeConfigs(food.table.size(),editDays.getText());
            freq_status = food.sanitizeConfigs(food.table.size(),editFreq.getText());

            if(days_status && freq_status)
            {
                food.days = Integer.parseInt(editDays.getText());
                food.maxFreq = Integer.parseInt(editFreq.getText());

                initStats();

                cancelMenuConfigs();

                display.setText("Successfully Updated Settings\tUsed: Custom");
            }
            else
            {

                if(days_status == false)
                {
                    editDays.setText("");
                    editDays.setPromptText("INVALID");
                }

                if(freq_status == false)
                {
                    editFreq.setText("");
                    editFreq.setPromptText("INVALID");
                }

            }

        }
    }


    public void defaultListener()
    {
        if(useDefault.isSelected())
        {
            editDays.setDisable(true);
            editFreq.setDisable(true);

            lengthSlider.setDisable(true);
            freqSlider.setDisable(true);

        }
        else
        {
            editDays.setDisable(false);
            editFreq.setDisable(false);

            lengthSlider.setDisable(false);
            freqSlider.setDisable(false);
        }
    }

    public void searchDishList() {
        String input = mainSearch.getText();

        if(input.compareTo("") != 0)
        {
            dishList.getItems().clear();

            ArrayList newList = food.dynamicSearch(input);


            for (int i = 0; i < newList.size(); i++)
            {

                dishList.getItems().add(newList.get(i));
            }
        }
        else
        {
            initLists(2);
        }

    }

    public void searchDelList() {
        String input = delSearch.getText();

        if(input.compareTo("") != 0)
        {
            delList.getItems().clear();

            ArrayList newList = food.dynamicSearch(input);


            for (int i = 0; i < newList.size(); i++)
            {

                delList.getItems().add(newList.get(i));
            }
        }
        else
        {
            initLists(2);
        }

    }

    public void searchAddList() {
        String input = addSearch.getText();

        if(input.compareTo("") != 0)
        {
            addList.getItems().clear();

            ArrayList newList = food.dynamicSearch(input);


            for (int i = 0; i < newList.size(); i++)
            {

                addList.getItems().add(newList.get(i));
            }
        }
        else
        {
            initLists(2);
        }

    }


    public void deleteDish() {

        String delName;
        int err;

        delName = delField.getText();


        if (delName.compareTo("") == 0) {
            delField.setPromptText("NO INPUT ERROR");
        } else {
            err = food.deleteDish(fileName, delName);

            delPane.setVisible(false);
            homePane.setVisible(true);

            delField.setText("");
            delField.setPromptText("Enter Dish Name");

            if (err == 1) {
                display.setText("Successfully deleted " + delName + " to the database!\n\n");
                initStats();
                initLists(1);
            } else if (err == 2) {
                display.setText(delName + " is not in the database!\n\n");
            } else {
                display.setText("There was an error writing to file\n\n");
            }
        }

    }

    public void addDish() {

        String newName, newType;
        int err,ok=0;

        newName = addField_Name.getText();
        newType = addField_Type.getText();

        dish newDish = new dish(newType, newName);

        if (newName.compareTo("") == 0) {
            addField_Name.setPromptText("NO INPUT ERROR");
            ok = -1;
        }
        if (newType.compareTo("") == 0) {
            addField_Type.setPromptText("NO INPUT ERROR");
            ok = -1;
        }
        if(ok == 0)
        {
            err = food.writeData(fileName, newDish);

            addPane.setVisible(false);
            homePane.setVisible(true);

            addField_Name.setText("");
            addField_Type.setText("");

            addField_Name.setPromptText("Enter Dish Name");
            addField_Type.setPromptText("Enter Dish Category");

            if (err != 1) {
                display.setText("There was an error writing to file\n\n");
            } else {
                display.setText("Successfully added " + newName + " to the database!\n\n");
                initStats();
                initLists(1);
            }

        }
    }


    public void menuDeleteDish() {
        homePane.setVisible(false);
        addPane.setVisible(false);
        editPane.setVisible(false);

        delPane.setVisible(true);

        delCancel.requestFocus();
    }

    public void menuAddDish() {
        homePane.setVisible(false);
        delPane.setVisible(false);
        editPane.setVisible(false);

        addPane.setVisible(true);

        addCancel.requestFocus();
    }

    public void menuConfigs() {
        homePane.setVisible(false);
        addPane.setVisible(false);
        delPane.setVisible(false);


        editPane.setVisible(true);

        initConfigs();
        defaultListener();
    }

    public void cancelMenuConfigs()
    {
        editPane.setVisible(false);
        homePane.setVisible(true);
    }

    public void cancelDeleteDish() {
        delPane.setVisible(false);
        homePane.setVisible(true);

        delField.setText("");
        delField.setPromptText("Enter Dish Name");
    }

    public void cancelAddDish() {
        addPane.setVisible(false);
        homePane.setVisible(true);

        addField_Name.setText("");
        addField_Type.setText("");

        addField_Name.setPromptText("Enter Dish Name");
        addField_Type.setPromptText("Enter Dish Category");
    }



    public void getMenu() {
        food.shuffleArray();
        food.makeMenu();
        display.setText(food.outputMenu());
    }

    public void getJoke() {

        display.setText(food.getJoke());
    }


    public void displaySelect() {
        String out = (String) dishList.getSelectionModel().getSelectedItem();

        dish selected = food.table.get(food.findDish(out));

        display.setText("Name: " + selected.name + "\nType: " + selected.type);
    }

    public void initConfigs()
    {
        lengthSlider.setMax(food.table.size());
        freqSlider.setMax(10);

        lengthSlider.setValue(0);
        freqSlider.setValue(1);

        useDefault.setSelected(false);

        editConf.requestFocus();

        editDays.setText("");
        editFreq.setText("");

        editDays.setPromptText("");
        editFreq.setPromptText("");
    }

    public void initStats() {

        mFreqLabel.setText("Max Frequency: " + food.maxFreq);
        daysLabel.setText("Days: " + food.days);
        dSizeLabel.setText("Database Size: " + food.table.size());
        fileLabel.setText("File Name: \"" + fileName + "\"");

        mFreqLabel_add.setText("Max Frequency: " + food.maxFreq);
        daysLabel_add.setText("Days: " + food.days);
        dSizeLabel_add.setText("Database Size: " + food.table.size());
        fileLabel_add.setText("File Name: \"" + fileName + "\"");

        mFreqLabel_del.setText("Max Frequency: " + food.maxFreq);
        daysLabel_del.setText("Days: " + food.days);
        dSizeLabel_del.setText("Database Size: " + food.table.size());
        fileLabel_del.setText("File Name: \"" + fileName + "\"");

        mFreqLabel_conf.setText("Max Frequency: " + food.maxFreq);
        daysLabel_conf.setText("Days: " + food.days);
        dSizeLabel_conf.setText("Database Size: " + food.table.size());
        fileLabel_conf.setText("File Name: \"" + fileName + "\"");
    }

    public void initLists(int mode) {

        dishList.getItems().clear();
        addList.getItems().clear();
        delList.getItems().clear();


        ObservableList<String> main;
        ObservableList<String> add;
        ObservableList<String> del;

        main = dishList.getItems();
        add = addList.getItems();
        del = delList.getItems();


        if(mode == 1)
        {
            food.table.clear();
            food.readData("menu.txt");
            food.shuffleArray();
        }


        for (int i = 0; i < food.table.size(); i++)
        {

            main.add(food.table.get(i).name);
            add.add(food.table.get(i).name);
            del.add(food.table.get(i).name);

        }

    }



    public void closeApp() {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        stage.close();
    }

    public void minimize() {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        stage.setIconified(true);
    }


    public void closeEnter() {
        close.setStyle("-fx-background-color:#80ffff;");
    }
    public void closeLeave() {
        close.setStyle("-fx-background-color:#a4a6a8;");
    }

    public void minEnter() {
        minimize.setStyle("-fx-background-color:#80ffff;");
    }
    public void minLeave() {
        minimize.setStyle("-fx-background-color:#a4a6a8;");
    }


    public void closeEnter_add() {
        close_add.setStyle("-fx-background-color:#80ffff;");
    }
    public void closeLeave_add() {
        close_add.setStyle("-fx-background-color:#a4a6a8;");
    }

    public void minEnter_add() {
        minimize_add.setStyle("-fx-background-color:#80ffff;");
    }
    public void minLeave_add() {
        minimize_add.setStyle("-fx-background-color:#a4a6a8;");
    }


    public void closeEnter_del() {
        close_del.setStyle("-fx-background-color:#80ffff;");
    }
    public void closeLeave_del() {
        close_del.setStyle("-fx-background-color:#a4a6a8;");
    }

    public void minEnter_del() {
        minimize_del.setStyle("-fx-background-color:#80ffff;");
    }
    public void minLeave_del() {
        minimize_del.setStyle("-fx-background-color:#a4a6a8;");
    }


    public void closeEnter_conf() {
        close_conf.setStyle("-fx-background-color:#80ffff;");
    }
    public void closeLeave_conf() {
        close_conf.setStyle("-fx-background-color:#a4a6a8;");
    }

    public void minEnter_conf() {
        minimize_conf.setStyle("-fx-background-color:#80ffff;");
    }
    public void minLeave_conf() {
        minimize_conf.setStyle("-fx-background-color:#a4a6a8;");
    }



    public void addEnter() {
        addLabel.setStyle("-fx-background-color:#80ffff;");
    }
    public void addLeave() {
        addLabel.setStyle("-fx-background-color:#a4a6a8;");
    }

    public void delEnter() {
        delLabel.setStyle("-fx-background-color:#80ffff;");
    }
    public void delLeave() {
        delLabel.setStyle("-fx-background-color:#a4a6a8;");
    }

}