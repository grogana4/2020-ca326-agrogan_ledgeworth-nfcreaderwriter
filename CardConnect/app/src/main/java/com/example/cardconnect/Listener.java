//this code was primarily worked on by Abe Grogan.
package com.example.cardconnect;

public interface Listener { //interface used to control dialog boxes for read and write to nfc buttons.

    void onDialogDisplayed();

    void onDialogDismissed();
}