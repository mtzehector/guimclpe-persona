package mx.gob.imss.dpes.personafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.EstadoPersonaEf;

/**
 *
 * @author luisr.rodriguez
 */
@Data
public class BajaOperadorRQ extends BaseModel{
    private Long id;
    private EstadoPersonaEf estadoPersonaEf;
    private Long cveEntidadFinanciera;
    private Long cvePersonalEf;
    private Long baja;
    private Long cveEstadoPersonaEf;
    private String email;
    private String adminCurp;
    private Long sesion;
    private String cveCurp;
}
