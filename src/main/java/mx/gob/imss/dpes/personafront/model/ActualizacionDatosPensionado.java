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
public class ActualizacionDatosPensionado extends BaseModel{
    private Pensionado pensionadoDatosAnteriores;
    private Pensionado pensionadoDatosNuevos;
    private String curp;
    private String numSesion;
    private String token;
}
