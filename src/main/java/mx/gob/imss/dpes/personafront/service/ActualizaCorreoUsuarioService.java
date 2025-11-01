package mx.gob.imss.dpes.personafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;
import mx.gob.imss.dpes.personafront.exception.ActualizaCorreoException;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.model.UsuarioRQ;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ActualizaCorreoUsuarioService extends ServiceDefinition<PersonaOrModel, PersonaOrModel> {

    @Inject
    private PersistePersonaService personaService;
    @Inject
    @RestClient
    private RegistroBackClient usuarioClient;

    @Override
    public Message<PersonaOrModel> execute(Message<PersonaOrModel> request) throws BusinessException {

        UsuarioRQ usuarioRQ = new UsuarioRQ();
        usuarioRQ.setNomUsuario(request.getPayload().getPersona().getCorreoElectronico());

        try {
            Response response = usuarioClient.obtenerUsuarioById(request.getPayload().getPersona().getId());
            if(response != null && response.getStatus() == 200){
                UserData usuario = response.readEntity(UserData.class);
                usuarioRQ.setNomUsuarioOld(usuario.getNOM_USUARIO());
                usuarioClient.updateNomUsuario(usuarioRQ);
            }
            return new Message<>(request.getPayload());
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ActualizaCorreoUsuarioService = {0}", e);
        }

        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new ActualizaCorreoException(ActualizaCorreoException.ERROR_AL_INVOCAR_SERVICIO_USUARIO),
                null
        );
    }
}
