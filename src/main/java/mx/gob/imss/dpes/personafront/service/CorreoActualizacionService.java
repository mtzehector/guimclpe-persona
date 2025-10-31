/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.personafront.exception.ActualizaCorreoException;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.restclient.CorreoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CorreoActualizacionService extends ServiceDefinition<ActualizacionDatosPensionado,ActualizacionDatosPensionado>{

    @Inject
    @RestClient
    private CorreoClient correoClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<ActualizacionDatosPensionado> execute(Message<ActualizacionDatosPensionado> request) throws BusinessException {

        //log.log(Level.INFO, "CorreoActualizacionService execute");
        //log.log(Level.INFO, "Datos entrada {0}", request);

        Correo correo = new Correo();

        try {
            if (request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico() != null && !request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico().isEmpty()) {
                String token = request.getPayload().getToken();

                String plantilla = String.format(config.getValue("plantillaActualizacionCorreo", String.class),
                        token);

                //try {
                correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
                //} catch (UnsupportedEncodingException ex) {
                //    Logger.getLogger(CorreoActualizacionService.class.getName()).log(Level.SEVERE, null, ex);
                //}

                String correoNuevo = request.getPayload().getPensionadoDatosNuevos().getCorreoElectronico();

                ArrayList<String> correos = new ArrayList<>();
                correos.add(correoNuevo);

                correo.setCorreoPara(correos);
                ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
                correo.setAdjuntos(adjuntos);

                correo.setAsunto("Actualización datos de contacto");
                //log.log(Level.INFO, "Datos de envio correo {0}", correo);

                Response respuesta = correoClient.enviaCorreo(correo);

                if (!(respuesta != null && respuesta.getStatus() == 200))
                    throw new ActualizaCorreoException(ActualizaCorreoException.ERROR_AL_ENVIAR_CORREO);
                //log.log(Level.INFO, ">>>>>>SE ENVIO CORREO DE CONFIRMACION DE ACTUALIZACION DE DATOS={0}", respuesta.getStatus());

                return request;

                //return new Message<>(request.getPayload());
            } else {

                String telOld1 = request.getPayload().getPensionadoDatosAnteriores().getTelLocal() == null ? "" : request.getPayload().getPensionadoDatosAnteriores().getTelLocal();
                String telOld2 = request.getPayload().getPensionadoDatosAnteriores().getTelCelular() == null ? "" : request.getPayload().getPensionadoDatosAnteriores().getTelCelular();
                String telNew1 = request.getPayload().getPensionadoDatosNuevos().getTelLocal() == null ? "" : request.getPayload().getPensionadoDatosNuevos().getTelLocal();
                String telNew2 = request.getPayload().getPensionadoDatosNuevos().getTelCelular() == null ? "" : request.getPayload().getPensionadoDatosNuevos().getTelCelular();


                String plantilla = String.format(config.getValue("plantillaActualizacionTelefono", String.class),
                        telOld1, telOld2, telNew1, telNew2);

                //try {
                correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
                //} catch (UnsupportedEncodingException ex) {
                //    Logger.getLogger(CorreoActualizacionService.class.getName()).log(Level.SEVERE, null, ex);
                //}

                String correoNuevo = request.getPayload().getPensionadoDatosAnteriores().getCorreoElectronico();

                ArrayList<String> correos = new ArrayList<>();
                correos.add(correoNuevo);

                correo.setCorreoPara(correos);
                ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
                correo.setAdjuntos(adjuntos);

                correo.setAsunto("Actualización datos de contacto");

                Response respuesta = correoClient.enviaCorreo(correo);

                if (!(respuesta != null && respuesta.getStatus() == 200))
                    throw new ActualizaCorreoException(ActualizaCorreoException.ERROR_AL_ENVIAR_CORREO);
                //log.log(Level.INFO, ">>>>>>SE ENVIO CORREO DE CONFIRMACION DE ACTUALIZACION DE DATOS={0}", respuesta.getStatus());
                //return request;

                return request;
                //  return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR::CorreoActualizacionService.execute = {0}", e);
            throw new ActualizaCorreoException(ActualizaCorreoException.ERROR_DESCONOCIDO_DEL_SERVIDOR);
        }

    }
    
}
