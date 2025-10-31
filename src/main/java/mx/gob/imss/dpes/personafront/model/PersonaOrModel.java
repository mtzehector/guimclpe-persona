/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.RegistroRequest;
import mx.gob.imss.dpes.interfaces.relacionlaboral.model.RelacionLaboralIn;
import mx.gob.imss.dpes.interfaces.relacionlaboral.model.RelacionLaboralOut;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class PersonaOrModel extends BaseModel {
    private PersonaP persona = new PersonaP();
    private Long cvePerfil;
    private RelacionLaboralIn laboralRequest = new RelacionLaboralIn();
    private RelacionLaboralOut laboralResponse = new RelacionLaboralOut();
    private RegistroRequest registroRequest = new RegistroRequest();
    private String registroBitacora;
}
