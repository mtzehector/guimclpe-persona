/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author juanf.barragan
 */
@Data
public class HistoricoUsuarioRequest extends BaseModel{
    
    private Long id;
    private String curpModificador;
    private Long cvePersona;
    private String correoElectronico;
    private Long telLocal;
    private Long telCelular;
}
