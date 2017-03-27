package com.sslab.pokemon;

import com.sslab.pokemon.data.PokemonIndividualData;
import com.sslab.pokemon.data.PokemonSpeciesData;
import com.sslab.pokemon.data.PokemonValueData;
import com.sslab.pokemon.sprite.PokemonSprite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by jerry on 2017/3/19.
 */
public class PokeGen {
    private JComboBox speciesComboBox;
    private JPanel root;

    private JButton deleteButton;
    private JButton saveButton;

    private JPanel slot0;
    private JPanel slot1;
    private JPanel slot2;
    private JPanel slot3;
    private JPanel slot4;
    private JPanel slot5;
    private JTextField nickNameField;
    private JTextField hpField;
    private JTextField atkField;
    private JTextField defField;
    private JTextField spAtkField;
    private JTextField spDefField;
    private JTextField speedField;
    private JPanel lastSelectedPanel = null;
    private JPanel currentSelectedPanel = null;
    private ArrayList<JTextField> statFields = new ArrayList<>();

    Pokedex pokedex = new Pokedex("bin/pokemonData.json");
    HashMap<JPanel, PokemonIndividualData> pokemonMap = new HashMap<>();
    int id = -1;
    String speciesName = null;
    public PokeGen() {
        //TODO: initialize statFields to zero
        statFields.add(hpField);
        statFields.add(atkField);
        statFields.add(defField);
        statFields.add(spAtkField);
        statFields.add(spDefField);
        statFields.add(speedField);
        for(int i=0;i<6;i++)
            statFields.get(i).setText("0");
        nickNameField.setText("");
        speciesComboBox.addItem("--------------------");
        for (int i = 0;i<pokedex.getPokemonSize();i++) {
            //TODO: Add items into combobox. Each item should be a concat string of pokemon id and name from pokedex
            String s;
            s = Integer.toString(pokedex.getPokemonData(i).getId());
            s = s.concat(": ");
            s = s.concat(pokedex.getPokemonData(i).getSpeciesName());
            speciesComboBox.addItem(s);
        }
        speciesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = (String) speciesComboBox.getSelectedItem();
                String s[] = str.split(":");
                for(String ss:s)
                    speciesName = ss;
                if(!s[0].equals("--------------------"))
                    id = Integer.parseInt(s[0]);
                else id = -1;
                if(id>0)
                    setPokemonIcon(id,currentSelectedPanel,true);
                else{
                    for(int i=0; i<6; i++)
                        statFields.get(i).setText("0");
                    nickNameField.setText("");
                    currentSelectedPanel.getComponent(0).setVisible(false);
                }
            }
        });
        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //TODO: set the last slot's border to default
                if(lastSelectedPanel!=null) lastSelectedPanel.setBorder(BorderFactory.createEtchedBorder());
                //TODO get the currentSelected slot
                currentSelectedPanel = (JPanel) e.getComponent();
                //TODO: set the currentSelected slot's border
                currentSelectedPanel.setBorder(BorderFactory.createBevelBorder(1));
                //TODO: save the data of the previous slot
                setPokemon(lastSelectedPanel);
                //TODO: load the data of the current slot
                loadPokemon(currentSelectedPanel);
                //TODO: update the lastSelected slot
                lastSelectedPanel = currentSelectedPanel;
            }
        };
        slot0.addMouseListener(listener);
        slot1.addMouseListener(listener);
        slot2.addMouseListener(listener);
        slot3.addMouseListener(listener);
        slot4.addMouseListener(listener);
        slot5.addMouseListener(listener);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPokemon(currentSelectedPanel);
                PokemonIndividualData data = pokemonMap.get(currentSelectedPanel);
                pokedex.addNewPokemon(data);
                try {
                    pokedex.saveFile("morris_new_pokemon.json");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speciesComboBox.setSelectedIndex(0);
                for(int i=0; i<6; i++)
                    statFields.get(i).setText("0");
                nickNameField.setText("");
                currentSelectedPanel.getComponent(0).setVisible(false);
            }
        });
    }

    public void setPokemon(JPanel panel) {
        if(panel==null) return;
        int[] valArray = new int[6];
        for(int i=0;i<6;i++)
            valArray[i] = Integer.parseInt(statFields.get(i).getText());
        PokemonValueData speciesValue = new PokemonValueData(valArray);
        PokemonIndividualData data = new PokemonIndividualData(id,nickNameField.getText(),speciesValue,speciesName);
        pokemonMap.put(panel,data);
    }

    public void loadPokemon(JPanel panel) {
        if(!pokemonMap.containsKey(panel)){
            speciesComboBox.setSelectedIndex(0);
            for(int i=0;i<6;i++)
                statFields.get(i).setText("0");
            nickNameField.setText("");
        }
        else {
            PokemonIndividualData data = pokemonMap.get(panel);
            id = data.getId();
            String s;
            s = Integer.toString(id);
            s = s.concat(":");
            s = s.concat(data.getSpeciesName());
            speciesComboBox.setSelectedItem(s);
            nickNameField.setText(data.getNickName());
            int[] valArray = data.getSpeciesValue().getValArray();
            for(int i=0;i<6;i++)
                statFields.get(i).setText(Integer.toString(valArray[i]));
        }
    }
    private void setPokemonIcon(int id,JPanel panel,boolean visible)
    {
        ImageIcon icon = new ImageIcon(PokemonSprite.getSprite(id));
        JLabel label = (JLabel) panel.getComponent(0);
        label.setIcon(icon);
        label.setVisible(visible);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("PokeGen");
        frame.setContentPane(new PokeGen().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}