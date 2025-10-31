package mx.gob.imss.dpes.personafront.endpoint;

import java.util.Date;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.enums.TipoServicioEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.personafront.exception.PersonaPensionadoException;
import mx.gob.imss.dpes.personafront.model.ActualizacionDatosPensionado;
import mx.gob.imss.dpes.personafront.model.Pensionado;
import mx.gob.imss.dpes.personafront.service.*;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 * @author luisr.rodriguez
 */
@Path("/pensionado")
@RequestScoped
public class PensionadoEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {
    @Inject
    private ReadPensionadoService readPensionadoService;
    @Inject
    private UpdatePensionadoService actualizaDatosContacto;
    @Inject
    private CancelaSolicitudService cancelaSolicitudes;
    @Inject
    private ActualizaTokenService actualizaToken;
    @Inject
    private CorreoActualizacionService enviaCorreo;
    //@Inject
    //private RegistroHistoricoUsuarioService registraHistorico;
    @Inject
    private BitacoraPensionadoService bitacoraPensionadoService;
    @Inject
    private EventoBitacoraInterfazService eventoBitacoraInterfazService;
    @Context
    private UriInfo uriInfo;

    private static final String SERVICIO_ACTUALIZAR_DATOS = "/personaFront/webresources/pensionado/actualizarDatosContacto";

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener pensionado por curp o nss",
            description = "Obtener pensionado por curp o nss")
    @Path("/getByCurpOrNss/{curp}/{nss}")
    public Response getByCurpOrNss(@PathParam("curp") String curp, @PathParam("nss") String numNss) 
            throws BusinessException {
        Message<Pensionado> request = new Message<>(new Pensionado());
        request.getPayload().setCveCurp(curp);
        request.getPayload().setNumNss(numNss);
        log.log(Level.INFO, ">>> personaFront PensionadoEndPoint.loadByCurpOrNss "
                    + "Request: {0}", request.getPayload());
        Message<Pensionado> response = readPensionadoService.execute(request);
        log.log(Level.INFO, ">>> personaFront PensionadoEndPoint.load response: {0}", 
                response.getPayload());

        if (response.getPayload() == null) return toResponse(new Message());
            
        return Response.ok(uriInfo.getAbsolutePath()).entity(response.getPayload()).build();  
    }

    private boolean esValidoMensajeActualizarDatosContacto(ActualizacionDatosPensionado actualizacionDatosPensionado) {
        try {
            if(
                actualizacionDatosPensionado != null &&
                actualizacionDatosPensionado.getCurp() != null &&
                !actualizacionDatosPensionado.getCurp().trim().isEmpty() &&
                actualizacionDatosPensionado.getNumSesion() != null &&
                !actualizacionDatosPensionado.getNumSesion().trim().isEmpty() &&
                Long.parseLong(actualizacionDatosPensionado.getNumSesion()) > 0L &&
                actualizacionDatosPensionado.getPensionadoDatosAnteriores() != null &&
                actualizacionDatosPensionado.getPensionadoDatosAnteriores().getCvePersona() != null &&
                !actualizacionDatosPensionado.getPensionadoDatosAnteriores().getCvePersona().trim().isEmpty() &&
                Long.parseLong(actualizacionDatosPensionado.getPensionadoDatosAnteriores().getCvePersona()) > 0L &&
                actualizacionDatosPensionado.getPensionadoDatosNuevos() != null &&
                (
                    (
                        actualizacionDatosPensionado.getPensionadoDatosNuevos().getCorreoElectronico() != null &&
                        !actualizacionDatosPensionado.getPensionadoDatosNuevos().getCorreoElectronico().trim().isEmpty()
                    ) ||
                    (
                        actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelLocal() != null &&
                        !actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelLocal().trim().isEmpty() &&
                        Long.parseLong(actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelLocal()) > 0
                    ) ||
                    (
                        actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelCelular() != null &&
                        !actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelCelular().trim().isEmpty() &&
                        Long.parseLong(actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelCelular()) > 0
                    )
                )
            )
                return true;
        } catch (Exception e) {
            log.log(Level.SEVERE,
                "PensionadoEndPoint.esValidoMensajeActualizarDatosContacto - actualizacionDatosPensionado = ["
                + actualizacionDatosPensionado + "]", e);
        }

        return false;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualiza datos de contacto",
            description = "Actualiza datos de contacto")
    @Path("/actualizarDatosContacto")
    public Response actualizarDatosContacto(@Context HttpHeaders headers,
        ActualizacionDatosPensionado actualizacionDatosPensionado) {
        Response response = null;

        boolean exito = false;
        boolean almacenarBitacora = false;
        Long tiempoInicial = 0L;
        Long tiempoEjecucion = 0L;
        String tokenSeguridad = null;

        try {
            tiempoInicial = new Date().getTime();

            if(!esValidoMensajeActualizarDatosContacto(actualizacionDatosPensionado))
                return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new PersonaPensionadoException(PersonaPensionadoException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                    null));

            tokenSeguridad = this.obtenerTokenSeguridad(headers.getRequestHeaders());
            actualizacionDatosPensionado.setToken(tokenSeguridad);

            almacenarBitacora = true;

            ServiceDefinition[] steps = new ServiceDefinition[4];
            steps = new ServiceDefinition[]{
                    actualizaDatosContacto,
                    cancelaSolicitudes,
                    //registraHistorico,
                    bitacoraPensionadoService,
                    actualizaToken,
                    enviaCorreo
            };
            Message<ActualizacionDatosPensionado> actualizacionDatosPensionadoMessage =
                actualizaDatosContacto.executeSteps(steps, new Message<>(actualizacionDatosPensionado));

            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            exito = true;

            response = toResponse(actualizacionDatosPensionadoMessage);
        } catch (BusinessException e) {
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            response = toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new PersonaPensionadoException(PersonaPensionadoException.ERROR_AL_ACTUALIZAR_DATOS_PENSIONADO),
                    null
            ));
        } catch (Exception e) {
            tiempoEjecucion = new Date().getTime() - tiempoInicial;
            log.log(Level.SEVERE, "ERROR::ValidaExisteSolicitudEnEstadosEndPoint.actualizarDatosContacto - " +
                    "actualizacionDatosPensionado = [" + actualizacionDatosPensionado + "]", e);

            response = toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new PersonaPensionadoException(PersonaPensionadoException.ERROR_DESCONOCIDO_DEL_SERVIDOR),
                    null
            ));
        } finally {
            if(almacenarBitacora) {
                BitacoraInterfaz bitacoraInterfaz = new BitacoraInterfaz();
                bitacoraInterfaz.setIdTipoServicio(TipoServicioEnum.SIPRE2.getId());
                bitacoraInterfaz.setExito(exito ? 1 : 0);
                bitacoraInterfaz.setSesion(actualizacionDatosPensionado.getNumSesion() != null ?
                        Long.valueOf(actualizacionDatosPensionado.getNumSesion()) : 0L);
                bitacoraInterfaz.setEndpoint(SERVICIO_ACTUALIZAR_DATOS);
                bitacoraInterfaz.setDescRequest(obtieneSolicitudActualizacionDatosPensionado(actualizacionDatosPensionado));
                bitacoraInterfaz.setReponseEndpoint(exito ?
                        "EXITO" : "FALLO");
                bitacoraInterfaz.setNumTiempoResp(tiempoEjecucion);
                bitacoraInterfaz.setAltaRegistro(tiempoInicial != null ? new Date(tiempoInicial) : new Date());
                bitacoraInterfaz.setToken(tokenSeguridad);

                try {
                    eventoBitacoraInterfazService.execute(new Message<BitacoraInterfaz>(bitacoraInterfaz));
                } catch (BusinessException bei) {
                    //Este escenario nunca debería pasar
                    log.log(Level.WARNING,
                        "ERROR PensionadoEndPoint.actualizarDatosContacto error al guardar la bitacoraInterfaz = [" +
                        bitacoraInterfaz + "]", bei);
                }
            }
        }

        return response;
    }

    private String obtieneSolicitudActualizacionDatosPensionado(
        ActualizacionDatosPensionado actualizacionDatosPensionado) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"numSesion\": \"").
        append(actualizacionDatosPensionado.getNumSesion()).
        append("\", \"curp\": \"").
        append(actualizacionDatosPensionado.getCurp()).
        append("\", \"pensionadoDatosAnteriores\": {").
        append("\"cvePersona\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosAnteriores().getCvePersona()).
        append("\", \"telLocal\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosAnteriores().getTelLocal()).
        append("\", \"telCelular\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosAnteriores().getTelCelular()).
        append("\", \"correoElectronico\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosAnteriores().getCorreoElectronico()).
        append("\"}, \"pensionadoDatosNuevos\": {").
        append("\"telLocal\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelLocal()).
        append("\", \"telCelular\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosNuevos().getTelCelular()).
        append("\", \"correoElectronico\": \"").
        append(actualizacionDatosPensionado.getPensionadoDatosNuevos().getCorreoElectronico()).
        append("\"}}");

        return sb.toString();
    }
}