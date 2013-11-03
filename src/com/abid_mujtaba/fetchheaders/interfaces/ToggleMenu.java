package com.abid_mujtaba.fetchheaders.interfaces;

/**
 * Describes an interface that must be implemented by any activity that wants to toggle the menu (enable and disable it).
 * The toggling is triggered by child fragments (usually).
 */

public interface ToggleMenu
{
    void enableMenu();

    void disableMenu();
}
