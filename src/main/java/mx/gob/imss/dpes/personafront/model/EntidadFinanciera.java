/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.model;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import mx.gob.imss.dpes.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author Diego
 */
public class EntidadFinanciera extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long id;
    @Getter @Setter private String nombreComercial;
    @Getter @Setter private String razonSocial;
    @Getter @Setter private String rfc;
    @Getter @Setter private String numeroTelefono;
    @Getter @Setter private String telefonoAtencionClientes;
    @Getter @Setter private String paginaWeb;
    @Getter @Setter private String correoElectronico;
    @Getter @Setter private BigDecimal catPromedio;
    @Getter @Setter private String idDelegacion;
    @Getter @Setter private String idSubdelegacion;
    @Getter @Setter private String registroPatronal;
    @Getter @Setter private Short mclcEstadoEf;
    @Getter @Setter private String mclcMotivoSuspension;
    

}
