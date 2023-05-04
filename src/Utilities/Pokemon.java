package Utilities;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Jasmine Rodriguez
 * @id U0000010605
 * @date 4/17/23
 * Pokemon class where Pokemon objects are initialized
 */
public class Pokemon implements Serializable {
    private UUID pokemonID;
    private String pokemonType, name;
    private boolean checkedIn;

    //There is no default constructor. Pokemon must have a type and name
    public Pokemon(String pokemonType, String name) {
        this.pokemonType = pokemonType;
        this.name = name;

        pokemonID = UUID.randomUUID();
    }

    public UUID getPokemonID() {
        return pokemonID;
    }

    public String getPokemonType() {
        return pokemonType;
    }

    public String getName() {
        return name;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void checkIn() {
        checkedIn = true;
    }

    public void checkOut()
    {
        checkedIn = false;
    }

}