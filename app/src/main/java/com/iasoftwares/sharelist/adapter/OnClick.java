package com.iasoftwares.sharelist.adapter;

public interface OnClick {
    public void DeletaItem(int position);

    public void EditarItem(int position);

    public void EscolheLista(int position);

    public void desmarcaItem(int position);

    public void marcaItem(int position);
}
