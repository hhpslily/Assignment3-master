package com.sslab.pokemon.data;

import javafx.scene.control.ComboBox;

/**
 * Created by jerry on 2017/3/21.
 */
public class PokemonIndividualData implements Comparable<PokemonIndividualData>
{
    //TODO create variables and constructor for this class
    private int id;
    private String nickName;
    private String speciesName;
    private PokemonValueData speciesValue;

    public PokemonIndividualData(int id, String nickName,PokemonValueData valueData,String speciesName)
    {
        this.id = id;
        this.nickName = nickName;
        this.speciesValue = valueData;
        this.speciesName = speciesName;
    }

    public int getId() {
        return id;
    }
    public String getNickName() {
        return nickName;
    }
    public String getSpeciesName() {
        return speciesName;
    }
    public PokemonValueData getSpeciesValue() {
        return speciesValue;
    }

    public int compareTo(PokemonIndividualData o) {
        return this.getId()-o.getId();
    }
}
