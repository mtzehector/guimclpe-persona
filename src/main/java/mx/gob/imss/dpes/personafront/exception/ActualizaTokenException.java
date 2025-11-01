package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ActualizaTokenException extends BusinessException {

    public final static String ERROR_AL_ACTUALIZAR_TOKEN = "msg19";

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public ActualizaTokenException(String messageKey) {
        super(messageKey);
    }

}