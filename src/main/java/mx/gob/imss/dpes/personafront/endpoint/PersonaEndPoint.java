package mx.gob.imss.dpes.personafront.endpoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.RegistroRequest;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.EstadoPersonaEf;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaP;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.PersonaResponse;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.interfaces.userdata.model.Usuario;
import mx.gob.imss.dpes.personafront.model.BajaOperadorRQ;
import mx.gob.imss.dpes.personafront.model.BajaPromotorRQ;
import mx.gob.imss.dpes.personafront.model.BitacoraRequest;
import mx.gob.imss.dpes.personafront.model.PersonaOrModel;
import mx.gob.imss.dpes.personafront.model.UsuarioRQ;
import mx.gob.imss.dpes.personafront.restclient.ActualizarRegistroFrontClient;
import mx.gob.imss.dpes.personafront.restclient.DocumentoBackClient;
import mx.gob.imss.dpes.personafront.restclient.PerfilUsuarioClient;
import mx.gob.imss.dpes.personafront.restclient.RegistroBackClient;
import mx.gob.imss.dpes.personafront.service.ActualizaCorreoUsuarioService;
import mx.gob.imss.dpes.personafront.service.BajaOperadorService;
import mx.gob.imss.dpes.personafront.service.BajaPromotorService;
import mx.gob.imss.dpes.personafront.service.BitacoraBackService;
import mx.gob.imss.dpes.personafront.service.ConsultaOperadorDetalleService;
import mx.gob.imss.dpes.personafront.service.ConsultaPersonaDetalleService;
import mx.gob.imss.dpes.personafront.service.ConsultarPersonaService;
import mx.gob.imss.dpes.personafront.service.ConsultarPersonaWithEFService;

import mx.gob.imss.dpes.personafront.service.DomicilioService;
import mx.gob.imss.dpes.personafront.service.PersistePersonaService;
import mx.gob.imss.dpes.personafront.service.RegistroBitacoraService;
import mx.gob.imss.dpes.personafront.service.RegistroFrontService;
import mx.gob.imss.dpes.personafront.service.RegistroPensioandoBackService;
import mx.gob.imss.dpes.personafront.service.RelacionLaboralService;
import mx.gob.imss.dpes.personafront.service.SolicitudBackService;
import mx.gob.imss.dpes.personafront.service.ValidaRelacionLaboralService;
import mx.gob.imss.dpes.personafront.service.ValidarAdminEFService;
import mx.gob.imss.dpes.personafront.service.ValidatePersonaService;
import mx.gob.imss.dpes.personafront.service.ValidateUserPersonaService;
import mx.gob.imss.dpes.personafront.service.ValidateUserRegistroService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author antonio
 */
@Path("/persona")
@RequestScoped
public class PersonaEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    private final static Long CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD = 1L;
    private final static Long CVE_TIPO_PERSONA_EMPLEADO_TERCERO = 2L;
    private final static Long CVE_TIPO_PERSONA_EF_PROMOTOR = 1L;
    private final static Long CVE_TIPO_PERSONA_EF_OPERADOR_EF = 2L;

    @Inject
    private ConsultarPersonaService consultarPersonaService;
    @Inject
    private ConsultarPersonaWithEFService consultarPersonaWithEFService;

    @Inject
    private PersistePersonaService persistePersonaService;
    @Inject
    private DomicilioService domicilioService;
    @Inject
    private ConsultaPersonaDetalleService detalleService;
    @Inject
    private RelacionLaboralService relacionLaboralService;
    @Inject
    private RegistroFrontService registroService;
    @Inject
    private ValidateUserRegistroService validateUserRegistroService;
    @Inject
    private ValidaRelacionLaboralService validaRelacionLaboral;
    @Inject
    private ValidatePersonaService validatePersona;
    @Inject
    private ValidateUserPersonaService validateUserPersona;
    @Inject
    private ValidarAdminEFService validarAdminEFService;
    @Inject
    private SolicitudBackService solBackService;
    @Inject
    private BajaPromotorService bajaProService;
    @Inject
    private BajaOperadorService bajaOpeService;
    @Inject
    private RegistroBitacoraService registroBitacora;
    @Inject
    private RegistroPensioandoBackService regPenBackService;
    @Inject
    private ConsultaOperadorDetalleService detalleOperadorService;
    @Inject
    DocumentoBackClient docBackClient;
    @Inject
    RegistroBackClient regBackCli;
    @Inject
    ActualizarRegistroFrontClient actualizarFrontCli;
    @Context
    private UriInfo uriInfo;
    @Inject
    private BitacoraBackService bitacoraBackService;
    @Inject
    private ActualizaCorreoUsuarioService usuarioService;
    @Inject
    private PerfilUsuarioClient perfilUsuarioService;
    
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/{curp}")
    public Response load(@PathParam("curp") String curp) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.load Request obtener Persona curp= {0}", curp);
        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.load Before consultarPersonaService");
        Message<PersonaResponse> respuesta = consultarPersonaService.execute(new Message<>(persona));
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.load respuesta.getPayload()= {0}", respuesta.getPayload());
        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/{curp}/{cveEntidadFinanciera}")
    public Response loadWithEF(@PathParam("curp") String curp, @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF Request obtener Persona curp= {0}", curp);
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF Request obtener Persona cveEntidadFinanciera= {0}", cveEntidadFinanciera);
        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        Message<PersonaResponse> respuesta = consultarPersonaWithEFService.validatePersonaWithEF(persona,cveEntidadFinanciera,1L);
        //log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF respuesta.getPayload()= {0}", respuesta.getPayload());
        if (respuesta.getPayload() == null) {
            return toResponse(new Message());
        }
        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/{curp}/{cveEntidadFinanciera}/excel")
    public Response loadWithEFExcel(@PathParam("curp") String curp, @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF Request obtener Persona curp= {0}", curp);
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF Request obtener Persona cveEntidadFinanciera= {0}", cveEntidadFinanciera);
        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        Message<PersonaResponse> respuesta = consultarPersonaWithEFService.validatePersonaWithEF(persona,cveEntidadFinanciera,1L);
        //log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF respuesta.getPayload()= {0}", respuesta.getPayload());
        if (respuesta.getPayload() == null) {
            return toResponse(new Message());
        }
        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }
    
        @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/{curp}/{cveEntidadFinanciera}/{cveTipoPersona}")
    public Response loadWithEFByTipoUsuario(
            @PathParam("curp") String curp,
            @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera,
            @PathParam("cveTipoPersona") Long cveTipoPersona) throws BusinessException {
        
        log.log(Level.INFO, ">>> personaFront loadWithEFByTipoUsuario curp= {0}", curp);
        log.log(Level.INFO, ">>> personaFront loadWithEFByTipoUsuario cveEntidadFinanciera= {0}", cveEntidadFinanciera);
        log.log(Level.INFO, ">>> personaFront loadWithEFByTipoUsuario cveTipoPersona= {0}", cveTipoPersona);

        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        Message<PersonaResponse> respuesta = consultarPersonaWithEFService.validatePersonaWithEF(persona,cveEntidadFinanciera,cveTipoPersona);
        //log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadWithEF respuesta.getPayload()= {0}", respuesta.getPayload());
        if (respuesta.getPayload() == null) {
            return toResponse(new Message());
        }
        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/detalle/{curp}")
    public Response loadDetalle(@PathParam("curp") String curp) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadDetalle Request obtener Persona: {0}", curp);
        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        Message<PersonaP> respuesta = detalleService.execute(new Message<>(persona));
        return toResponse(respuesta);
//        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Persistir Persona",
            description = "Persistir Persona")
    @SuppressWarnings("empty-statement")
    public Response create(PersonaOrModel persona) throws BusinessException {
        this.fillPersonaData(persona);
        persona.getPersona().setCveTipoPersonaEntidadFinanciera(CVE_TIPO_PERSONA_EF_PROMOTOR);
        ServiceDefinition[] steps = new ServiceDefinition[3];
        //al ser tipo interno se debe validar la relaciÃ³n laboral

        log.log(Level.INFO, ">>> personaFront PersonaEndPoint. create persona= {0}", persona.getPersona().getCveCurp());
//        log.log(Level.INFO, ">>> personaFront PersonaEndPoint. create persona.getPersona()= {0}", persona.getPersona());
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint create persona.getPersona().getCveTipoEmpleado()= {0}", persona.getPersona().getCveTipoEmpleado());
        if (Objects.isNull(persona.getPersona().getCveTipoEmpleado())) {
            persona.getPersona().setCveTipoEmpleado(0L);
            steps = new ServiceDefinition[]{
                validateUserRegistroService,
                validatePersona,
                persistePersonaService,
                registroService
            };
        }

        if (persona.getPersona().getId() != null && persona.getPersona().getId() > 0) {
            steps = getStepsForPromotorModification(persona);
        } else {
            if (persona.getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_TERCERO) == 0) {
                
                log.log(Level.INFO, ">>> personaFront INSIDE CVE_TIPO_PERSONA_EMPLEADO_TERCERO= {0}",
                        CVE_TIPO_PERSONA_EMPLEADO_TERCERO);
                
                steps = new ServiceDefinition[]{
                    validateUserRegistroService,
                    validatePersona,
                    persistePersonaService,
                    registroService,
                    bitacoraBackService
                };
            } else {
                if (persona.getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD) == 0) {
                    
                    log.log(Level.INFO, ">>> personaFront INSIDE CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD= {0}",
                            CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD);
                    
                    steps = new ServiceDefinition[]{
                        validateUserRegistroService,
                        validatePersona,
                        relacionLaboralService, 
                        validaRelacionLaboral, 
                        persistePersonaService, 
                        registroService,
                        bitacoraBackService
                    };
                } else {
                    persona.getPersona().setCveTipoEmpleado(0L);
                    steps = new ServiceDefinition[]{
                        validateUserRegistroService,
                        validatePersona,
                        persistePersonaService,
                        registroService,
                        bitacoraBackService
                    };
                }
            }
        }

        Message<PersonaOrModel> response = persistePersonaService.executeSteps(steps, new Message<>(persona));
        
        if (!Message.isException(response)) {
            return toResponse(response);
        }

        return toResponse(response);
    }

    public ServiceDefinition[] getStepsForPromotorModification(PersonaOrModel request) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint getStepsForPromotorModification persona.getPersona().getId()= {0}", request.getPersona().getId());
        
        ServiceDefinition[] steps = null;
        //0= email NOR tipoEmpleado changes
        //1=Only email change
        //2=Only tipoEmpleado change
        //3=email and tipoEmpleado change
        switch (validatePersona.isNewPersona(request)) {
            case 0:
                steps = new ServiceDefinition[]{persistePersonaService};
                break;
            case 1:
                steps = new ServiceDefinition[]{validateUserRegistroService, persistePersonaService, registroService, usuarioService};
                break;
            case 2:
                if (request.getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD) == 0) {
                    log.log(Level.INFO, ">>> personaFront getStepsForPromotorModification CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD= {0}", CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD);
                    steps = new ServiceDefinition[]{relacionLaboralService,persistePersonaService};
                }
                else{
                    steps = new ServiceDefinition[]{persistePersonaService};
                }
                break;
            case 3:
                if (request.getPersona().getCveTipoEmpleado().compareTo(CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD) == 0) {
                    log.log(Level.INFO, ">>> personaFront getStepsForPromotorModification CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD= {0}", CVE_TIPO_PERSONA_EMPLEADO_ENTIDAD);
                    steps = new ServiceDefinition[]{validateUserRegistroService,relacionLaboralService,persistePersonaService, registroService, usuarioService};
                }
                else{
                    steps = new ServiceDefinition[]{validateUserRegistroService, persistePersonaService, registroService, usuarioService};
                }
                break;
            default:
                steps = new ServiceDefinition[]{persistePersonaService};
                break;

        }
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint getStepsForPromotorModification steps= {0}", steps.toString());
        return steps;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Persistir Persona",
            description = "Persistir Persona")
    @SuppressWarnings("empty-statement")
    @Path("/registro/operadorEF")
    public Response createOperadorEF(RegistroRequest registroReq) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.createOperadorEF registroReq= {0}", registroReq);
        PersonaOrModel persona = this.fillPersonaData(registroReq);
        persona.getPersona().setCveTipoPersonaEntidadFinanciera(CVE_TIPO_PERSONA_EF_OPERADOR_EF);

        ServiceDefinition[] steps = new ServiceDefinition[3];
        //al ser tipo interno se debe validar la relaciÃ³n laboral
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.createOperadorEF persona.getPersona()= {0}", persona.getPersona());

        if (Objects.isNull(persona.getPersona().getCveTipoEmpleado())) {
            persona.getPersona().setCveTipoEmpleado(0L);
            steps = new ServiceDefinition[]{validateUserRegistroService, validatePersona, persistePersonaService, registroService};
        }
        if (persona.getPersona().getCveTipoEmpleado() == 1) {
            steps = new ServiceDefinition[]{validateUserRegistroService, validatePersona, relacionLaboralService, persistePersonaService, registroService};
        } else {
            persona.getPersona().setCveTipoEmpleado(0L);
            steps = new ServiceDefinition[]{validateUserRegistroService, validatePersona, persistePersonaService, registroService};
        }

        Message<PersonaOrModel> response = persistePersonaService.executeSteps(steps, new Message<>(persona));
        if (!Message.isException(response)) {
            return toResponse(response);
        }
        return toResponse(response);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Persistir Persona",
            description = "Persistir Persona")
    @SuppressWarnings("empty-statement")
    @Path("/validate-persona")
    public Integer validatePersona(RegistroRequest registroReq) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.validatePersona registroReq= {0}", registroReq);
        PersonaOrModel persona = this.fillPersonaData(registroReq);
        return validateUserPersona.validateUserPersona(new Message<>(persona));
    }

    public void fillPersonaData(PersonaOrModel persona) throws BusinessException {
        EstadoPersonaEf estadoPersona = new EstadoPersonaEf();
        estadoPersona.setId(1L);
        persona.getPersona().setCveEstadoPersonaEf(estadoPersona);
        persona.getPersona().getCveEstadoPersonaEf().setId(persona.getPersona().getEstadoPersonaEf());
        persona.getLaboralRequest().setNss(persona.getPersona().getNumNss());
        persona.getRegistroRequest().setCorreo(persona.getPersona().getCorreoElectronico());
        persona.getRegistroRequest().setCorreoConfirmar(persona.getPersona().getCorreoElectronico());
        persona.getRegistroRequest().setCurp(persona.getPersona().getCveCurp());
        persona.getRegistroRequest().setCvePerfil(persona.getPersona().getCvePerfil());
        persona.getRegistroRequest().setNss(persona.getPersona().getNumNss());
        try {
            persona.getRegistroRequest().setNumTelefono(Long.parseLong(persona.getPersona().getTelCelular()));
        } catch (Exception e) {
            e.printStackTrace();
            persona.getRegistroRequest().setNumTelefono(0L);
            //throw new PersonaNoRegistradaException(PersonaNoRegistradaException.TELEFONO);
        }
    }

    public PersonaOrModel fillPersonaData(RegistroRequest registroReq) throws BusinessException {
        PersonaOrModel persona = new PersonaOrModel();
        persona.getPersona().setEstadoPersonaEf(1L);
        persona.getPersona().setNumNss(registroReq.getNss());
        persona.getPersona().setCorreoElectronico(registroReq.getCorreo());
        persona.getPersona().setCveCurp(registroReq.getCurp());
        persona.getPersona().setCvePerfil(registroReq.getCvePerfil());
        persona.getPersona().setNumNss(registroReq.getNss());
        persona.getPersona().setCveEntidadFinanciera(registroReq.getCveEntidadFinanciera());
        String numTelStr = null;
        if (registroReq.getNumTelefono() != null) {
            Long numTel = registroReq.getNumTelefono();
            try {
                numTelStr = numTel.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        persona.getPersona().setTelCelular(numTelStr);
        persona.getPersona().setNumEmpleado(registroReq.getNumEmpleado());
        persona.getPersona().setRegistroPatronal(registroReq.getRegistroPatronal());
        persona.getPersona().setRfc(registroReq.getRfc());
        persona.getPersona().setCveTipoEmpleado(1L);
        Integer cveSexo = 1;
        String cveSexoStr = "0";
        Short sexo = 0;
        if (registroReq.getSexo() != null) {
            try {
                sexo = Short.parseShort(registroReq.getSexo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        persona.getPersona().setCveSexo(sexo);
        persona.getPersona().setNombre(registroReq.getNombres());
        persona.getPersona().setSegundoApellido(registroReq.getApellido2());
        persona.getPersona().setPrimerApellido(registroReq.getApellido1());
        //10/11/1972
        Date fecnac = new Date();
        if (registroReq.getFechNac() != null && registroReq.getFechNac().length() > 0) {
            try {
                fecnac = new SimpleDateFormat("dd/MM/yyyy").parse(registroReq.getFechNac());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        persona.getPersona().setFecNacimiento(fecnac);
        persona.getPersona().setCvePersonalEf(1L);
        persona.getPersona().setCveEstadoVital(5L);
        EstadoPersonaEf estadoPersona = new EstadoPersonaEf();
        estadoPersona.setId(1L);
        persona.getPersona().setCveEstadoPersonaEf(estadoPersona);
        persona.getPersona().getCveEstadoPersonaEf().setId(persona.getPersona().getEstadoPersonaEf());
        persona.getLaboralRequest().setNss(persona.getPersona().getNumNss());
        persona.getRegistroRequest().setCorreo(persona.getPersona().getCorreoElectronico());
        persona.getRegistroRequest().setCorreoConfirmar(persona.getPersona().getCorreoElectronico());
        persona.getRegistroRequest().setCurp(persona.getPersona().getCveCurp());
        persona.getRegistroRequest().setCvePerfil(persona.getPersona().getCvePerfil());
        persona.getRegistroRequest().setNss(persona.getPersona().getNumNss());
        persona.getRegistroRequest().setRfc(persona.getPersona().getRfc());
        try {
            persona.getRegistroRequest().setNumTelefono(Long.parseLong(persona.getPersona().getTelCelular()));
        } catch (Exception e) {
            e.printStackTrace();
            persona.getRegistroRequest().setNumTelefono(0L);
            //throw new PersonaNoRegistradaException(PersonaNoRegistradaException.TELEFONO);
        }
        return persona;
    }

    @POST
    @Path("/validarNuevoPensionado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarNuevoPensionado(RegistroRequest registroReq) throws BusinessException {

        PersonaOrModel pm = new PersonaOrModel();
        RegistroRequest rr = new RegistroRequest();
        PersonaP persona = new PersonaP();
        persona.setCorreoElectronico(registroReq.getCorreo());
        persona.setCveCurp(registroReq.getCurp());
        pm.setPersona(persona);
        rr.setCorreo(registroReq.getCorreo());
        rr.setCurp(registroReq.getCurp());
        rr.setNss(registroReq.getNss());
        pm.setRegistroRequest(rr);
        ServiceDefinition[] steps = {validateUserRegistroService, validatePersona};
        Message<PersonaOrModel> response = validateUserRegistroService.executeSteps(steps, new Message<>(pm));
        if (Message.isException(response)) {
            log.log(Level.INFO, "Response catch: {0}", response);
            return toResponse(response);
        }
        return toResponse(response);

    }

    @POST
    @Path("/validarNuevoAdminEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarNuevoAdminEF(RegistroRequest registroReq) throws BusinessException {

        PersonaOrModel pm = new PersonaOrModel();
        RegistroRequest rr = new RegistroRequest();
        PersonaP persona = new PersonaP();
        persona.setCorreoElectronico(registroReq.getCorreo());
        persona.setCveCurp(registroReq.getCurp());
        pm.setPersona(persona);
        rr.setCorreo(registroReq.getCorreo());
        rr.setCurp(registroReq.getCurp());
        pm.setRegistroRequest(rr);

        ServiceDefinition[] steps = new ServiceDefinition[3];
        Message<PersonaOrModel> response = null;
        if (registroReq.getCurp() != null) {
            steps = new ServiceDefinition[]{validarAdminEFService};
            response = validateUserRegistroService.executeSteps(steps, new Message<>(pm));

        } else if (registroReq.getCorreo() != null) {
            steps = new ServiceDefinition[]{validateUserRegistroService, validarAdminEFService};
            response = validateUserRegistroService.executeSteps(steps, new Message<>(pm));

        }

        if (Message.isException(response)) {
            log.log(Level.INFO, "Response catch: {0}", response);
            return toResponse(response);
        }

        return toResponse(response);

    }

        
    @POST
    @Path("/bajaPromotor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bajaPromotor(BajaPromotorRQ promotor) throws BusinessException {
         
        EstadoPersonaEf ep = new EstadoPersonaEf();
        ep.setId(promotor.getCveEstadoPersonaEf());
        promotor.setEstadoPersonaEf(ep);
        log.log(Level.INFO, "bajaPromotor RQ: {0}", promotor);
        UsuarioRQ u = new UsuarioRQ();
        u.setNomUsuario(promotor.getEmail());
           u.setIndActivo(1);
        bajaProService.execute( new Message<>(promotor));
         
        if (promotor.getEstadoPersonaEf().getId()== 2 || 
                promotor.getEstadoPersonaEf().getId() == 3) {
           Solicitud solicitud = new Solicitud();
           solicitud.setCvePromotor(promotor.getCvePersonalEf());
           solicitud.setCveEntidadFinanciera(promotor.getCveEntidadFinanciera());
           solBackService.execute( new Message<>(solicitud));
           u.setIndActivo(0);  
        }
        regPenBackService.execute( new Message<>(u));
        
        return Response.accepted().build();
    }
    
    @POST
    @Path("/bajaOperador")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bajaOperador(BajaOperadorRQ operador) throws BusinessException {
        EstadoPersonaEf ep = new EstadoPersonaEf();
        ep.setId(operador.getCveEstadoPersonaEf());
        operador.setEstadoPersonaEf(ep);
        
        UsuarioRQ u = new UsuarioRQ();
        u.setNomUsuario(operador.getEmail());
        u.setIndActivo(1);
        
        bajaOpeService.execute(new Message<>(operador));
        
        if(operador.getCveEstadoPersonaEf() == 2 
                || operador.getCveEstadoPersonaEf() == 3){
            u.setIndActivo(0); 
        }
        
        regPenBackService.execute( new Message<>(u));
         
        if(operador.getCveEstadoPersonaEf() == 1 
                || operador.getCveEstadoPersonaEf() == 2 
                || operador.getCveEstadoPersonaEf() == 3){
            BitacoraRequest bitacoraRequest = new BitacoraRequest();
            bitacoraRequest.setCurp(operador.getAdminCurp());
            bitacoraRequest.setSesion(operador.getSesion());
            String motivoBaja =""; 
            motivoBaja = operador.getBaja() == 2 ? "por comportamiento irregular" 
                    : "por término de la relación laboral";
            switch(String.valueOf(operador.getCveEstadoPersonaEf())){
                case "1":
                    bitacoraRequest.setTipo(34L);
                    break;
                case "2":
                    bitacoraRequest.setTipo(33L);
                    bitacoraRequest.setDescripcion("Suspensión " + motivoBaja);
                    break;
                case "3":
                    bitacoraRequest.setTipo(32L);
                    bitacoraRequest.setDescripcion("Baja "  + motivoBaja);
                    break;
            }
            registroBitacora.execute(new Message<BitacoraRequest>(bitacoraRequest));
        }
        return Response.accepted().build();
    }
    
        
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener la persona por curp",
            description = "Obtener la persona por curp")
    @Path("/detalle-operador/{curp}")
    public Response loadDetalleOperador(@PathParam("curp") String curp) throws BusinessException {
        log.log(Level.INFO, ">>> personaFront PersonaEndPoint.loadDetalle Request obtener Persona: {0}", curp);
        Persona persona = new Persona();
        persona.setCurp(curp.toUpperCase());

        Message<PersonaP> respuesta = detalleOperadorService.execute(new Message<>(persona));
        return toResponse(respuesta);
//        return Response.ok(uriInfo.getAbsolutePath()).entity(respuesta.getPayload()).build();

    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update-operador")
    public Response updateOperador(PersonaP operador) throws BusinessException {

        log.log(Level.INFO, ">>> updateOperador: {0}", operador);

        PersonaP persona = persistePersonaService.personaById(operador);
        
        log.log(Level.INFO, ">>>  PersonaP persona : {0}", persona);
    
        if (!persona.getCorreoElectronico().equals(operador.getCorreoElectronico())) {
        
            log.log(Level.INFO, ">>> CORREOS DIFERENTES: {0}", operador);
            
            Message<PersonaOrModel> request = new Message<>();
            RegistroRequest rr =new RegistroRequest();
            rr.setCorreo(operador.getCorreoElectronico());
            rr.setCorreoConfirmar(operador.getCorreoElectronico());
            PersonaOrModel pom = new PersonaOrModel();
            pom.setRegistroRequest(rr);
            
            request.setPayload(pom);   
            log.log(Level.INFO, ">>> request: {0}", request);
            validateUserRegistroService.execute(request);
            log.log(Level.INFO, ">>> validateUserRegistroService: {0}", request);
            UsuarioRQ rq = new UsuarioRQ();

            rq.setNomUsuario(operador.getCorreoElectronico());
            rq.setNomUsuarioOld(persona.getCorreoElectronico());
                        
            log.log(Level.INFO, ">>> updateNomUsuario: {0}", rq);

            regBackCli.updateNomUsuario(rq);
        }
        if (operador.getCveEstadoPersonaEf() == null && operador.getEstadoPersonaEf() != null ){
            operador.setCveEstadoPersonaEf(new EstadoPersonaEf(operador.getEstadoPersonaEf()));
            operador.getCveEstadoPersonaEf().setDesEstadoPersonaEf("");
        }
        persistePersonaService.updatePersona(operador);
                
        Documento doc = new Documento();            
        doc.setId(operador.getDocumentoIdentOficial().getId());
        doc.setCvePersona( operador.getId() );
        docBackClient.limpiarDocs(doc);
        docBackClient.asignarDocumento(doc);
        
        if (!persona.getCorreoElectronico().equals(operador.getCorreoElectronico())) {
            log.log(Level.INFO, ">>> PENDIENTE MANDAR CORREO TOKEN: {0}", operador);
            actualizarFrontCli.create(new RegistroRequest(operador.getCorreoElectronico()));
        }
        
        Response response = regBackCli.obtenerUsuarioByIdPersona(persona.getId());
        Usuario usuario = response.readEntity(Usuario.class);
        perfilUsuarioService.updatePerfil(usuario.getId(), operador.getFirmaCartaRecibo());
        
        return Response.accepted().build();
    }
    
}
