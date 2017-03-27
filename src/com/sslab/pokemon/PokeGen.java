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
    private JPanel currentSelectedPanel = slot0;
    private JPanel nextSelectedPanel = slot0;
    private ArrayList<JTextField> statFields = new ArrayList<>();

    Pokedex pokedex = new Pokedex("bin/pokemonData.json");
    HashMap<JPanel, PokemonIndividualData> pokemonMap = new HashMap<>();
    int id = -1;
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
                String s[] = str.split(": ",2);
                if(!s[0].equals("--------------------"))
                    id = Integer.parseInt(s[0]);
                else id = -1;
                if(id>0)
                    setPokemonIcon(id,nextSelectedPanel,true);
                else{
                    for(int i=0; i<6; i++)
                        statFields.get(i).setText("0");
                    nickNameField.setText("");
                    nextSelectedPanel.getComponent(0).setVisible(false);
                }
            }
        });
        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentSelectedPanel.setBorder(BorderFactory.createEtchedBorder());
                nextSelectedPanel = (JPanel) e.getComponent();
                nextSelectedPanel.setBorder(BorderFactory.createBevelBorder(1));
                setPokemon(currentSelectedPanel);
                loadPokemon(nextSelectedPanel);
                currentSelectedPanel = nextSelectedPanel;
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
                int[] valArray = new int[6];
                for(int i=0;i<6;i++)
                    valArray[i] = Integer.parseInt(statFields.get(i).getText());
                pokedex.addNewPokemon(id,nickNameField.getText(),valArray);
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
        int[] valArray = new int[6];
        for(int i=0;i<6;i++)
            valArray[i] = Integer.parseInt(statFields.get(i).getText());
        PokemonValueData speciesValue = new PokemonValueData(valArray);
        PokemonIndividualData data = new PokemonIndividualData(id,nickNameField.getText(),speciesValue);
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
            speciesComboBox.setSelectedIndex(data.getId()+1);
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