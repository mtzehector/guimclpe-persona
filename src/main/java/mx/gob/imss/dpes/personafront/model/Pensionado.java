/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.gob.imss.dpes.personafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author luisr.rodriguez
 */
@Data
public class Pensionado extends BaseModel {
    private String correoElectronico; 
    private String cveCurp;
    private String cvePersona;
    private String cveSexo;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String telCelular;
    private String telLocal;
    private String numNss;
    private String id;
}
