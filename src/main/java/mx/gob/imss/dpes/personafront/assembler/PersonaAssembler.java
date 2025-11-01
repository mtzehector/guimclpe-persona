package mx.gob.imss.dpes.personafront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaResponse;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class PersonaAssembler extends BaseAssembler<PersonaP, PersonaResponse> {
    
    @Override
    public PersonaResponse assemble(PersonaP source) {
        
        PersonaResponse response = new PersonaResponse();
        response.setCveCurp(source.getCveCurp());
        response.setNombre(source.getNombre());
        response.setPrimerApellido(source.getPrimerApellido());
        response.setSegundoApellido(source.getSegundoApellido());
        response.setDesEstadoPersonaEf(source.getCveEstadoPersonaEf() != null ? source.getCveEstadoPersonaEf().getDesEstadoPersonaEf() : null);
        response.setCveEntidadFinanciera(source.getCveEntidadFinanciera());
        response.setCorreoElectronico(source.getCorreoElectronico());
        response.setTelLocal(source.getTelLocal());
        response.setTelCelular(source.getTelCelular());
        response.setCvePersona(source.getId());
        response.setImgB64(source.getImgB64());
        response.setCveSexo(source.getCveSexo().intValue());
        response.setIdEstadoPersonaEF(source.getCveEstadoPersonaEf() != null ? 
                source.getCveEstadoPersonaEf().getId() != null ?  source.getCveEstadoPersonaEf().getId() : 0L 
                : 0L);
        response.setBaja(source.getBaja() != null ? source.getBaja() : null);
        return response;
    }
    
}
