package client.ui.gui;

import common.Artikel;
import common.Massengutartikel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArtikelTabelModel extends AbstractTableModel {
    private List<Artikel> artikel;
    private String[] spaltenNamen = {"Name","Nummer","Preis","Bestand (Packungsgroesse)"};

    public ArtikelTabelModel(List<Artikel> aktuelleArtikel){
        artikel = new ArrayList<>();
        artikel.addAll(aktuelleArtikel);
    }

    public void setArtikel(List<Artikel> aktuelleArtikel){
        artikel.clear();
        artikel.addAll(aktuelleArtikel);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount(){
        return artikel.size();
    }

    @Override
    public int getColumnCount() {
        return spaltenNamen.length;
    }

    @Override
    public String getColumnName(int column){
        return spaltenNamen[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artikel gewaehltesArtikel = artikel.get(rowIndex);
        switch (columnIndex){
            case 0:
                return gewaehltesArtikel.getName();
            case 1:
                return gewaehltesArtikel.getNummer();
            case 2:
                return gewaehltesArtikel.getPreis();
            case 3:
                if(gewaehltesArtikel instanceof Massengutartikel m){
                    return m.getBestand() + " (" +
                            m.getPackungsgroesse() + ")";
                }
                return gewaehltesArtikel.getBestand();
            default:
                return null;
        }
    }
}
