package com.cyk.adventure_redux;

import java.util.ArrayList;

public class ConsoleResponse
{
    // Temp buffer for the lines being built per query
    private ArrayList<String> lines;

    public ConsoleResponse()
    {
        // Nothing
    }

    public void add (String theString)
    {
        lines.add(theString);
    }

    public void addln (String theString)
    {
        lines.add(theString + "\n");
    }

    public ArrayList<String> getLines() {
        // Limit scope of this so it garbage-collects
        ArrayList<String> returnedLines = new ArrayList<String>(lines);

        // Clear it before returning query
        lines.clear();
        return returnedLines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }
}
