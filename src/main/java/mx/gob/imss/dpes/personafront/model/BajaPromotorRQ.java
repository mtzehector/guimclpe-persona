/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.EstadoPersonaEf;

/**
 *
 * @author juan.garfias
 */
public class BajaPromotorRQ  extends BaseModel {

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private EstadoPersonaEf estadoPersonaEf;
    @Getter
    @Setter
    private Long cveEntidadFinanciera;
    @Getter
    @Setter
    private Long cvePersonalEf;
    @Getter
    @Setter
    private Long baja;

    @Getter
    @Setter
    private Long cveEstadoPersonaEf;
    
    @Getter
    @Setter
    private String email;
}
