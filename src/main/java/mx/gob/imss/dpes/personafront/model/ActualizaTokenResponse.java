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
public class ActualizaTokenResponse extends BaseModel{
    private String curp;
    private String nss;
    private Long numTelefono;
    private String correo;
    private String correoConfirmar;
    private String vigenciaToken;
    private String token;
}
