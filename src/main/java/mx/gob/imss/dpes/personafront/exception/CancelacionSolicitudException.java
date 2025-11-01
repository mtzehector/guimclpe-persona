package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CancelacionSolicitudException extends BusinessException {

    public final static String ERROR_AL_CANCELAR_SOLICITUD_DEL_PENSIONADO = "msg16";

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public CancelacionSolicitudException(String messageKey) {
        super(messageKey);
    }

}
