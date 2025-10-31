/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author juan.garfias
 */
public class DocumentoRq extends BaseModel {

    @Getter
    @Setter
    private Long cvePersona;
    @Getter
    @Setter
    private Long tipoDocumento;

    public DocumentoRq(Long cvePersona, Long tipoDocumento) {
        this.cvePersona = cvePersona;
        this.tipoDocumento = tipoDocumento;
    }

    public DocumentoRq() {
    }

    
}
