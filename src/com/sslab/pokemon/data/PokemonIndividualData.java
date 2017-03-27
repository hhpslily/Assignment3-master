package com.sslab.pokemon.data;

import javafx.scene.control.ComboBox;

/**
 * Created by jerry on 2017/3/21.
 */
public class PokemonIndividualData {
    //TODO create variables and constructor for this class
    private int id;
    private String nickName;
    private PokemonValueData speciesValue;

    public PokemonIndividualData(int id, String nickName,PokemonValueData valueData)
    {
        this.id = id;
        this.nickName = nickName;
        this.speciesValue = valueData;
    }

    public int getId() {
        return id;
    }
    public String getNickName() {
        return nickName;
    }
    public PokemonValueData getSpeciesValue() {
        return speciesValue;
    }

}
