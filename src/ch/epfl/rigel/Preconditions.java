package ch.epfl.rigel;

public final class Preconditions {

    private Preconditions() {}

    void checkArgumemt(boolean isTrue){

    if (isTrue != true ){
        throw new IllegalArgumentException() ;
    }


    }



}
