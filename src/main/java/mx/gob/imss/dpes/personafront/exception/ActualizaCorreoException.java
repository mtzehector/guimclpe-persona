package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ActualizaCorreoException extends BusinessException  {

    public final static String ERROR_AL_INVOCAR_SERVICIO_USUARIO = "msg14";

    public final static String ERROR_AL_ENVIAR_CORREO = "msg20";

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public final static String ERROR_ACTUALIZAR_TOKEN = "msg23";

    public ActualizaCorreoException(String causa) {
        super(causa);
    }
}
