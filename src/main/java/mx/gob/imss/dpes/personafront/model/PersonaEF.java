/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.interfaces.persona.model.DelegacionPersonalEF;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class PersonaEF extends BaseModel {

    private Long id;
    private String cveCurp;
    private String numEmpleado;
    private Long cveEntidadFinanciera;
    private String cveDelegacion;
    private Long cveEstadoPersonaEf;
    private Long cveTipoPersonaEf;
    private Integer indRegistrado;
    private List<DelegacionPersonalEF> delegaciones;
    
    @Getter
    @Setter
    private String imgB64;

}
