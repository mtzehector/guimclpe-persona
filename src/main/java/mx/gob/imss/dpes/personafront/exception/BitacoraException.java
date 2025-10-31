package mx.gob.imss.dpes.personafront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class BitacoraException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_DEL_SERVIDOR = "msg17";

    public final static String ERROR_AL_GUARDAR_EN_BITACORA = "msg22";

    public BitacoraException(String messageKey) {
        super(messageKey);
    }

}
