package com.github.lpandzic.junit.bdd;

/**
* @author Lovro Pandzic
*/
class TargetAlreadyDestroyedException extends Exception {

    TargetAlreadyDestroyedException(String message) {

        super(message);
    }
}
