package client.ui.gui;

import common.Ereignis;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EreignisTabelModel extends AbstractTableModel {
    private List<Ereignis> ereignis;
    private String[] spaltenNamen = {"Datum","Person","Ereignis"};

    public EreignisTabelModel(List<Ereignis> aktuellesEreignis){
        ereignis = new ArrayList<>();
        ereignis.addAll(aktuellesEreignis);
    }

    public void setEreignis(List<Ereignis> aktuellesEreignis){
        ereignis.clear();
        ereignis.addAll(aktuellesEreignis);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount(){
        return ereignis.size();
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
        Ereignis gewaehltesEreignis = ereignis.get(rowIndex);
        switch (columnIndex){
            case 0:
                return gewaehltesEreignis.getDatum();
            case 1:
                return gewaehltesEreignis.getPerson();
            case 2:
                return gewaehltesEreignis.getStatus();
            default:
                return null;
        }
    }
}
