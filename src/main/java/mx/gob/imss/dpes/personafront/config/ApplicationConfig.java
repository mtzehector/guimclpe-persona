/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.personafront.config;

/**
 *
 * @author antonio
 */
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.personafront.assembler.PersonaAssembler.class);
        resources.add(mx.gob.imss.dpes.personafront.endpoint.PensionadoEndPoint.class);
        resources.add(mx.gob.imss.dpes.personafront.endpoint.PersonaEndPoint.class);
        resources.add(mx.gob.imss.dpes.personafront.endpoint.PromotorEndPoint.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ActualizaTokenService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.BajaOperadorService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.BajaPromotorService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.BitacoraBackService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.CancelaSolicitudService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ConsultaOperadorDetalleService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ConsultaPersonaDetalleService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ConsultarPersonaService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ConsultarPersonaWithEFService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ConsultarPromotorAsignarService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.CorreoActualizacionService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.DomicilioService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.PersistePersonaService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ReadPensionadoService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.RegistroBitacoraService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.RegistroFrontService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.RegistroHistoricoUsuarioService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.RegistroPensioandoBackService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.RelacionLaboralService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.SolicitudBackService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.UpdatePensionadoService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ValidaRelacionLaboralService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ValidarAdminEFService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ValidatePersonaService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ValidateUserPersonaService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ValidateUserRegistroService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.ActualizaCorreoUsuarioService.class);
        resources.add(mx.gob.imss.dpes.personafront.service.BitacoraPensionadoService.class);

    }

}
