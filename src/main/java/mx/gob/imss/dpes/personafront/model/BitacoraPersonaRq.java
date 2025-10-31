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
 * @author juanf.barragan
 */
public class BitacoraPersonaRq extends BaseModel{
    
    @Getter
    @Setter
    private Long id;
    
    @Getter
    @Setter
    private Long cvePersonalEF;
    
    @Getter
    @Setter
    private Long cvePersona;
    
    @Getter
    @Setter
    private Long cveRegistroPromotor;
    
    @Getter
    @Setter
    private String actividad;
    
    @Getter
    @Setter
    private String motivo;
    
}
