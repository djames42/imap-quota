package com.djames42.services;

public class Meter {
    public static String meter(Float percent, char meterBar) {
        Integer terminalWidth=82,posLeft,posRight;
        String output=null, padLeft=null, padRight=null;
        String lpad = String.valueOf(' '), rpad= String.valueOf(' ');

        if (percent > 99.99) percent= 99.99F;
        posLeft= (int) (terminalWidth.floatValue() * (percent / 100.0) - 5.00);
        if (posLeft < 3) posLeft=3;
        if (posLeft > (terminalWidth)-11) posLeft=terminalWidth-11;
        posRight = terminalWidth - posLeft - 11;
        if (posLeft < 4) {
            lpad= "";
            posRight++;
        }
        if (posRight < 0) posRight=0;
        if (posRight == 0) {
            rpad = "";
            posLeft++;
        }
        padLeft = new String(new char[posLeft-3]).replace('\0',meterBar);
        padRight = new String(new char[posRight]).replace('\0',meterBar);
        output="|- " + padLeft + String.format(lpad + "%06.2f" + rpad, percent) + padRight + " -|";
        return output;
    }
}
