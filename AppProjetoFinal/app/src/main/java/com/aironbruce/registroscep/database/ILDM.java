package com.aironbruce.registroscep.database;

import com.aironbruce.registroscep.otherclasses.Localizacao;

import java.util.List;

public interface ILDM {
    boolean salvar(Localizacao local);
    boolean editar(Localizacao local);
    boolean apagar(Localizacao local);
    Localizacao carregar(long id);
    List<Localizacao> listar();
}
