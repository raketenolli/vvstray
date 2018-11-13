package de.oliver_arend.VVStray;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

class StationTextFilterator implements TextFilterator<Station> {
    @Override
    public void getFilterStrings(List<String> baseList, Station station) {
        baseList.add(station.getName());
    }
}